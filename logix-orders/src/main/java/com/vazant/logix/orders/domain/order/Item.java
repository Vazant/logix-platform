package com.vazant.logix.orders.domain.order;

import com.vazant.logix.orders.domain.common.BaseEntity;
import com.vazant.logix.orders.domain.common.Updatable;
import com.vazant.logix.orders.domain.product.Product;
import com.vazant.logix.orders.domain.shared.Money;
import com.vazant.logix.orders.infrastructure.utils.JiltBuilder;
import com.vazant.logix.shared.Constants;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Entity representing an item in an order.
 * <p>
 * Stores product, quantity, unit price, and supports updating from another instance.
 */
@Entity
@Table(name = Constants.ENTITY_ITEM)
public class Item extends BaseEntity implements Updatable<Item> {

  /**
   * Default constructor for JPA.
   */
  protected Item() {}

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

  /**
   * Constructs a new Item with all fields.
   *
   * @param order the order this item belongs to
   * @param product the product
   * @param quantity the quantity
   * @param unitPrice the unit price
   */
  @JiltBuilder
  public Item(Order order, Product product, int quantity, Money unitPrice) {
    this.order = order;
    this.product = product;
    this.quantity = quantity;
    this.unitPrice = unitPrice;
  }

  /**
   * Returns the order this item belongs to.
   *
   * @return the order
   */
  public Order getOrder() {
    return order;
  }

  /**
   * Sets the order for this item.
   *
   * @param order the order
   */
  public void setOrder(Order order) {
    this.order = order;
  }

  /**
   * Returns the product for this item.
   *
   * @return the product
   */
  public Product getProduct() {
    return product;
  }

  /**
   * Sets the product for this item.
   *
   * @param product the product
   */
  public void setProduct(Product product) {
    this.product = product;
  }

  /**
   * Returns the quantity of the product in this item.
   *
   * @return the quantity
   */
  public int getQuantity() {
    return quantity;
  }

  /**
   * Sets the quantity for this item.
   *
   * @param quantity the quantity
   */
  public void setQuantity(int quantity) {
    if (quantity < 0) {
      throw new IllegalArgumentException("Quantity must be non-negative");
    }
    this.quantity = quantity;
  }

  /**
   * Returns the unit price for this item.
   *
   * @return the unit price
   */
  public Money getUnitPrice() {
    return unitPrice;
  }

  /**
   * Sets the unit price for this item.
   *
   * @param unitPrice the unit price
   */
  public void setUnitPrice(Money unitPrice) {
    this.unitPrice = unitPrice;
  }

  /**
   * Returns the total price for this item (unit price * quantity).
   *
   * @return the total price
   */
  public Money getTotalPrice() {
    return new Money(
        unitPrice.getAmount().multiply(BigDecimal.valueOf(quantity)), unitPrice.getCurrency());
  }

  /**
   * Increases the quantity of this item by the specified amount.
   *
   * @param amount the amount to increase
   */
  public void increaseQuantity(int amount) {
    if (amount <= 0) {
      throw new IllegalArgumentException("Increase amount must be positive");
    }
    this.quantity += amount;
  }

  /**
   * Returns the subtotal for this item (unit price * quantity).
   *
   * @return the subtotal
   */
  public Money getSubtotal() {
    return unitPrice.multiply(quantity);
  }

  /**
   * Updates this item from another instance.
   *
   * @param updated the updated item
   */
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
