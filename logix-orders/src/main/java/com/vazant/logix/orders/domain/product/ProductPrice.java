package com.vazant.logix.orders.domain.product;

import com.vazant.logix.orders.domain.common.BaseEntity;
import com.vazant.logix.orders.domain.common.Updatable;
import com.vazant.logix.orders.domain.shared.Money;
import com.vazant.logix.orders.infrastructure.utils.JiltBuilder;
import com.vazant.logix.shared.Constants;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

/**
 * Entity representing a price for a product.
 * <p>
 * Stores the price value and the associated product, and supports updating from another instance.
 */
@Entity
@Table(name = Constants.ENTITY_PRODUCT_PRICE)
public class ProductPrice extends BaseEntity implements Updatable<ProductPrice> {

  @NotNull(message = "Product must not be null")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_uuid", nullable = false)
  private Product product;

  @NotNull(message = "Price must not be null")
  @Valid
  @Embedded
  private Money price;

  /**
   * Default constructor for JPA.
   */
  protected ProductPrice() {}

  /**
   * Constructs a new ProductPrice with the specified product and price.
   *
   * @param product the product
   * @param price the price value
   */
  @JiltBuilder
  public ProductPrice(Product product, Money price) {
    this.product = product;
    this.price = price;
  }

  /**
   * Returns the associated product.
   *
   * @return the product
   */
  public Product getProduct() {
    return product;
  }

  /**
   * Sets the associated product.
   *
   * @param product the product
   */
  public void setProduct(Product product) {
    this.product = product;
  }

  /**
   * Returns the price value.
   *
   * @return the price
   */
  public Money getPrice() {
    return price;
  }

  /**
   * Sets the price value.
   *
   * @param price the price
   */
  public void setPrice(Money price) {
    this.price = price;
  }

  /**
   * Updates this product price from another instance.
   *
   * @param updated the updated product price
   */
  @Override
  public void doUpdate(ProductPrice updated) {
    this.price = updated.price;
    this.product = updated.product;
  }
}
