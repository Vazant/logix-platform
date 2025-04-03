package com.vazant.logix.orders.domain.order;

import com.vazant.logix.orders.common.BaseEntity;
import com.vazant.logix.orders.domain.customer.Customer;
import com.vazant.logix.orders.domain.product.Product;
import com.vazant.logix.orders.domain.shared.Currency;
import com.vazant.logix.orders.domain.shared.Money;
import com.vazant.logix.orders.sdk.utils.JiltBuilder;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order extends BaseEntity {

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "customer_id", nullable = false)
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

  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Item> items;

  protected Order() {}

  @JiltBuilder
  public Order(Customer customer, String warehouseId, Money total, String description) {
    this.customer = customer;
    this.warehouseId = warehouseId;
    this.total = total;
    this.description = description;
    this.items = new ArrayList<>();
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
    return items;
  }

  public void updateStatus(OrderStatus newStatus) {
    this.status = newStatus;
  }

  public void addItem(Item newItem) {
    if (newItem == null) {
      throw new IllegalArgumentException("Item must not be null");
    }

    for (Item existing : items) {
      if (existing.getProduct().equals(newItem.getProduct())) {
        existing.increaseQuantity(newItem.getQuantity());
        return;
      }
    }

    newItem.setOrder(this);
    items.add(newItem);
  }

  public void removeItemByProduct(Product product) {
    if (product == null) {
      throw new IllegalArgumentException("Product must not be null");
    }

    items.removeIf(item -> item.getProduct().equals(product));
  }

  public void updateItemQuantity(Product product, int newQuantity) {
    if (product == null) throw new IllegalArgumentException("Product must not be null");
    if (newQuantity < 0) throw new IllegalArgumentException("Quantity must be non-negative");

    for (Item item : items) {
      if (item.getProduct().equals(product)) {
        if (newQuantity == 0) {
          items.remove(item);
        } else {
          item.setQuantity(newQuantity);
        }
        return;
      }
    }

    throw new IllegalArgumentException("Item with product not found in order");
  }

  public Money calculateTotal(Currency currency) {
    return items.stream().map(Item::getSubtotal).reduce(Money.zero(currency), Money::add);
  }
}
