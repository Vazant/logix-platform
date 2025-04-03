package com.vazant.logix.orders.domain.product;

import com.vazant.logix.orders.common.BaseEntity;
import com.vazant.logix.orders.sdk.utils.JiltBuilder;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "products")
public class Product extends BaseEntity {

  @NotBlank(message = "Product name must not be blank")
  private String name;
  @Valid
  @OneToMany(
      mappedBy = "product",
      cascade = jakarta.persistence.CascadeType.ALL,
      orphanRemoval = true)
  private List<ProductPrice> prices = new ArrayList<>();
  @Size(max = 1000, message = "Description must be at most 1000 characters")
  @Column(length = 1000)
  private String description;
  @NotBlank(message = "SKU Code must not be blank")
  @Column(unique = true, nullable = false)
  private String skuCode;
  @Valid @Embedded private Dimensions dimensions;
  @PositiveOrZero(message = "Stock quantity must be zero or positive")
  private int stockQuantity;
  @NotNull(message = "Category ID must not be null")
  private UUID categoryId;
  private UUID imageId;

  @JiltBuilder
  public Product(
      String name,
      List<ProductPrice> prices,
      String description,
      String skuCode,
      Dimensions dimensions,
      int stockQuantity,
      UUID categoryId,
      UUID imageId) {
    this.name = name;
    this.prices = prices;
    this.description = description;
    this.skuCode = skuCode;
    this.dimensions = dimensions;
    this.stockQuantity = stockQuantity;
    this.categoryId = categoryId;
    this.imageId = imageId;
  }

  protected Product() {}

  public List<ProductPrice> getPrices() {
    return prices;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public String getSkuCode() {
    return skuCode;
  }

  public Dimensions getDimensions() {
    return dimensions;
  }

  public int getStockQuantity() {
    return stockQuantity;
  }

  public UUID getCategoryId() {
    return categoryId;
  }

  public UUID getImageId() {
    return imageId;
  }
}
