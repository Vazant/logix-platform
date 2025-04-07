package com.vazant.logix.orders.domain.order;

import com.vazant.logix.orders.domain.common.BaseEntity;
import com.vazant.logix.orders.domain.common.Updatable;
import com.vazant.logix.orders.domain.product.Product;
import com.vazant.logix.orders.domain.shared.Money;
import com.vazant.logix.orders.infrastructure.utils.JiltBuilder;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
public class Item extends BaseEntity implements Updatable<Item> {

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "order_uuid", nullable = false)
  private Order order;

  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "product_uuid", nullable = false)
  private Product product;

  @Min(1)
  @Column(nullable = false)
  private int quantity;

  @NotNull @Valid @Embedded private Money unitPrice;

  protected Item() {}

  @JiltBuilder
  public Item(Order order, Product product, int quantity, Money unitPrice) {
    this.order = order;
    this.product = product;
    this.quantity = quantity;
    this.unitPrice = unitPrice;
  }

  public Order getOrder() {
    return order;
  }

  public void setOrder(Order order) {
    this.order = order;
  }

  public Product getProduct() {
    return product;
  }

  public void setProduct(Product product) {
    this.product = product;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    if (quantity < 0) {
      throw new IllegalArgumentException("Quantity must be non-negative");
    }
    this.quantity = quantity;
  }

  public Money getUnitPrice() {
    return unitPrice;
  }

  public void setUnitPrice(Money unitPrice) {
    this.unitPrice = unitPrice;
  }

  public Money getTotalPrice() {
    return new Money(
        unitPrice.getAmount().multiply(BigDecimal.valueOf(quantity)), unitPrice.getCurrency());
  }

  public void increaseQuantity(int amount) {
    if (amount <= 0) {
      throw new IllegalArgumentException("Increase amount must be positive");
    }
    this.quantity += amount;
  }

  public Money getSubtotal() {
    return unitPrice.multiply(quantity);
  }

  @Override
  public void doUpdate(Item updated) {
    if (updated.getProduct() != null) {
      this.setProduct(updated.getProduct());
    }

    if (updated.getUnitPrice() != null) {
      this.setUnitPrice(updated.getUnitPrice());
    }

    this.setQuantity(updated.getQuantity());
  }
}
