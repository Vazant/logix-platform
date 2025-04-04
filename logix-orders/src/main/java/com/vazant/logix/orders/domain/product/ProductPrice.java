package com.vazant.logix.orders.domain.product;

import com.vazant.logix.orders.domain.common.BaseEntity;
import com.vazant.logix.orders.domain.common.Updatable;
import com.vazant.logix.orders.domain.shared.Money;
import com.vazant.logix.orders.infrastructure.utils.JiltBuilder;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "product_prices")
public class ProductPrice extends BaseEntity implements Updatable<ProductPrice> {

  @NotNull(message = "Product must not be null")
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_id", nullable = false)
  private Product product;

  @NotNull(message = "Price must not be null")
  @Valid
  @Embedded
  private Money price;

  protected ProductPrice() {}

  @JiltBuilder
  public ProductPrice(Product product, Money price) {
    this.product = product;
    this.price = price;
  }

  public Product getProduct() {
    return product;
  }

  public void setProduct(Product product) {
    this.product = product;
  }

  public Money getPrice() {
    return price;
  }

  public void setPrice(Money price) {
    this.price = price;
  }

  @Override
  public void doUpdate(ProductPrice updated) {
    this.price = updated.price;
    this.product = updated.product;
  }
}
