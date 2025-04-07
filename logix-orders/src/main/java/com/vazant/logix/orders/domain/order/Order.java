package com.vazant.logix.orders.domain.order;

import com.vazant.logix.orders.domain.common.BaseEntity;
import com.vazant.logix.orders.domain.common.Updatable;
import com.vazant.logix.orders.domain.customer.Customer;
import com.vazant.logix.orders.domain.event.DomainEventPublisher;
import com.vazant.logix.orders.domain.event.OrderStatusChangedEvent;
import com.vazant.logix.orders.domain.product.Product;
import com.vazant.logix.orders.domain.shared.Currency;
import com.vazant.logix.orders.domain.shared.Money;
import com.vazant.logix.orders.infrastructure.utils.JiltBuilder;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

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
  @Column(nullable = false)
  private String warehouseId;

  @NotNull @Valid @Embedded private Money total;

  @Size(max = 255)
  private String description;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private OrderStatus status;

  protected Order() {}

  @JiltBuilder
  public Order(Customer customer, String warehouseId, Money total, String description) {
    this.customer = Objects.requireNonNull(customer);
    this.warehouseId = Objects.requireNonNull(warehouseId);
    this.total = Objects.requireNonNull(total);
    this.description = description;
    this.status = OrderStatus.CREATED;
  }

  public Customer getCustomer() {
    return customer;
  }

  public String getWarehouseId() {
    return warehouseId;
  }

  public Money getTotal() {
    return total;
  }

  public String getDescription() {
    return description;
  }

  public OrderStatus getStatus() {
    return status;
  }

  public List<Item> getItems() {
    return Collections.unmodifiableList(items); // защищаем от внешней мутации
  }

  public void updateStatus(OrderStatus newStatus) {
    Objects.requireNonNull(newStatus);
    if (!this.status.canTransitionTo(newStatus)) {
      throw new IllegalArgumentException(
          "Невозможно изменить статус из " + this.status + " на " + newStatus);
    }
    OrderStatus previousStatus = this.status;
    this.status = newStatus;

    OrderStatusChangedEvent event =
        new OrderStatusChangedEvent(this.getUuid().toString(), previousStatus, newStatus);
    DomainEventPublisher.publish(event);
  }

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

    Item item =
        ItemBuilder.item()
            .order(this)
            .product(product)
            .quantity(quantity)
            .unitPrice(unitPrice)
            .build();

    items.add(item);
    recalculateTotal();
  }

  public void removeItem(Product product) {
    boolean removed = items.removeIf(item -> item.getProduct().equals(product));
    if (removed) recalculateTotal();
  }

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

  public Money calculateTotal(Currency currency) {
    return items.stream().map(Item::getSubtotal).reduce(Money.zero(currency), Money::add);
  }

  private void recalculateTotal() {
    this.total = calculateTotal(total.getCurrency());
  }

  @Override
  public void doUpdate(Order updated) {
    this.description = updated.getDescription();
    this.warehouseId = updated.getWarehouseId();
    this.customer = updated.getCustomer();

    if (!this.status.equals(updated.getStatus())) {
      this.updateStatus(updated.getStatus());
    }
  }
}
