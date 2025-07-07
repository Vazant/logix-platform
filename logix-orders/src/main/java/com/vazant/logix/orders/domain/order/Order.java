package com.vazant.logix.orders.domain.order;

import com.vazant.logix.orders.domain.common.BaseEntity;
import com.vazant.logix.orders.domain.common.Updatable;
import com.vazant.logix.orders.domain.customer.Customer;
import com.vazant.logix.orders.domain.event.DomainEventPublisher;
import com.vazant.logix.orders.domain.event.OrderStatusChangedEvent;
import com.vazant.logix.orders.domain.organization.Organization;
import com.vazant.logix.orders.domain.product.Product;
import com.vazant.logix.orders.domain.shared.Currency;
import com.vazant.logix.orders.domain.shared.Money;
import com.vazant.logix.orders.infrastructure.utils.JiltBuilder;
import com.vazant.logix.orders.domain.order.ItemBuilder;
import com.vazant.logix.shared.Constants;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Entity representing an order in the system.
 * <p>
 * Stores customer, organization, items, status, and supports business logic for item management and status transitions.
 */
@Entity
@Table(name = "orders")
public class Order extends BaseEntity implements Updatable<Order> {

  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
  private final List<Item> items = new ArrayList<>();

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "customer_uuid", nullable = false)
  private Customer customer;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "organization_uuid", nullable = false)
  private Organization organization;

  @NotNull
  @Column(nullable = false)
  private String warehouseId;

  @NotNull @Valid @Embedded private Money total;

  @Size(max = 255)
  private String description;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private OrderStatus status;

  @Column(name = "order_date")
  private LocalDateTime orderDate;

  /**
   * Default constructor for JPA.
   */
  protected Order() {}

  /**
   * Constructs a new Order with the specified customer, organization, warehouse, total, and description.
   *
   * @param customer the customer
   * @param organization the organization
   * @param warehouseId the warehouse ID
   * @param total the total amount
   * @param description the order description
   */
  @JiltBuilder
  public Order(Customer customer, Organization organization, String warehouseId, Money total, String description) {
    this.customer = Objects.requireNonNull(customer);
    this.organization = Objects.requireNonNull(organization);
    this.warehouseId = Objects.requireNonNull(warehouseId);
    this.total = Objects.requireNonNull(total);
    this.description = description;
    this.status = OrderStatus.CREATED;
    this.orderDate = LocalDateTime.now();
  }

  /**
   * Returns the customer for this order.
   *
   * @return the customer
   */
  public Customer getCustomer() {
    return customer;
  }

  /**
   * Sets the customer for this order.
   *
   * @param customer the customer
   */
  public void setCustomer(Customer customer) {
    this.customer = customer;
  }

  /**
   * Returns the organization for this order.
   *
   * @return the organization
   */
  public Organization getOrganization() {
    return organization;
  }

  /**
   * Sets the organization for this order.
   *
   * @param organization the organization
   */
  public void setOrganization(Organization organization) {
    this.organization = organization;
  }

  /**
   * Returns the warehouse ID for this order.
   *
   * @return the warehouse ID
   */
  public String getWarehouseId() {
    return warehouseId;
  }

  /**
   * Sets the warehouse ID for this order.
   *
   * @param warehouseId the warehouse ID
   */
  public void setWarehouseId(String warehouseId) {
    this.warehouseId = warehouseId;
  }

  /**
   * Returns the total amount for this order.
   *
   * @return the total amount
   */
  public Money getTotal() {
    return total;
  }

  /**
   * Sets the total amount for this order.
   *
   * @param total the total amount
   */
  public void setTotal(Money total) {
    this.total = total;
  }

  /**
   * Returns the description for this order.
   *
   * @return the description
   */
  public String getDescription() {
    return description;
  }

  /**
   * Sets the description for this order.
   *
   * @param description the description
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Returns the status of this order.
   *
   * @return the order status
   */
  public OrderStatus getStatus() {
    return status;
  }

  /**
   * Sets the status of this order.
   *
   * @param status the order status
   */
  public void setStatus(OrderStatus status) {
    this.status = status;
  }

  /**
   * Returns the order date.
   *
   * @return the order date
   */
  public LocalDateTime getOrderDate() {
    return orderDate;
  }

  /**
   * Sets the order date.
   *
   * @param orderDate the order date
   */
  public void setOrderDate(LocalDateTime orderDate) {
    this.orderDate = orderDate;
  }

  /**
   * Returns the list of items in this order.
   *
   * @return the list of items
   */
  public List<Item> getItems() {
    return Collections.unmodifiableList(items);
  }

  /**
   * Updates the status of this order, publishing an event if the transition is valid.
   *
   * @param newStatus the new status
   */
  public void updateStatus(OrderStatus newStatus) {
    Objects.requireNonNull(newStatus);
    if (!this.status.canTransitionTo(newStatus)) {
      throw new IllegalArgumentException(
          "Cannot change status from " + this.status + " to " + newStatus);
    }
    OrderStatus previousStatus = this.status;
    this.status = newStatus;

    OrderStatusChangedEvent event =
        new OrderStatusChangedEvent(this.getUuid().toString(), previousStatus, newStatus);
    DomainEventPublisher.publish(event);
  }

  /**
   * Adds an item to this order and recalculates the total.
   *
   * @param item the item to add
   */
  public void addItem(Item item) {
    Objects.requireNonNull(item);
    item.setOrder(this);
    items.add(item);
    recalculateTotal();
  }

  /**
   * Adds an item to this order by product, quantity, and unit price. If the product already exists, increases its quantity.
   *
   * @param product the product
   * @param quantity the quantity
   * @param unitPrice the unit price
   */
  public void addItem(Product product, int quantity, Money unitPrice) {
    Objects.requireNonNull(product);
    if (quantity <= 0) throw new IllegalArgumentException("Quantity must be positive");

    for (Item existing : items) {
      if (existing.getProduct().equals(product)) {
        existing.increaseQuantity(quantity);
        recalculateTotal();
        return;
      }
    }

    Item item = ItemBuilder.item()
        .order(this)
        .product(product)
        .quantity(quantity)
        .unitPrice(unitPrice)
        .build();

    items.add(item);
    recalculateTotal();
  }

  /**
   * Removes an item from this order by product and recalculates the total.
   *
   * @param product the product to remove
   */
  public void removeItem(Product product) {
    boolean removed = items.removeIf(item -> item.getProduct().equals(product));
    if (removed) recalculateTotal();
  }

  /**
   * Updates the quantity of an item in this order. Removes the item if the new quantity is zero.
   *
   * @param product the product
   * @param newQuantity the new quantity
   */
  public void updateItemQuantity(Product product, int newQuantity) {
    for (Item item : items) {
      if (item.getProduct().equals(product)) {
        if (newQuantity == 0) {
          items.remove(item);
        } else {
          item.setQuantity(newQuantity);
        }
        recalculateTotal();
        return;
      }
    }
    throw new IllegalArgumentException("Item with product not found in order");
  }

  /**
   * Calculates the total amount for this order in the specified currency.
   *
   * @param currency the currency
   * @return the total amount
   */
  public Money calculateTotal(Currency currency) {
    return items.stream().map(Item::getSubtotal).reduce(Money.zero(currency), Money::add);
  }

  private void recalculateTotal() {
    this.total = calculateTotal(total.getCurrency());
  }

  /**
   * Updates this order from another instance.
   *
   * @param updated the updated order
   */
  @Override
  public void doUpdate(Order updated) {
    this.description = updated.getDescription();
    this.warehouseId = updated.getWarehouseId();
    this.customer = updated.getCustomer();
    this.organization = updated.getOrganization();

    if (!this.status.equals(updated.getStatus())) {
      this.updateStatus(updated.getStatus());
    }
  }
}
