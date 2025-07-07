package com.vazant.logix.orders.domain.product;

import com.vazant.logix.orders.domain.common.BaseEntity;
import com.vazant.logix.orders.domain.common.Updatable;
import com.vazant.logix.orders.domain.organization.Organization;
import com.vazant.logix.orders.infrastructure.utils.JiltBuilder;
import com.vazant.logix.shared.Constants;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

/**
 * Entity representing a product in the system.
 * <p>
 * Stores product details, prices, dimensions, stock, and organization association.
 * Supports updating from another instance and price management.
 */
@Entity
@Table(name = Constants.ENTITY_PRODUCT)
public class Product extends BaseEntity implements Updatable<Product> {

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

  @NotNull(message = "Organization must not be null")
  @ManyToOne(fetch = jakarta.persistence.FetchType.LAZY)
  @JoinColumn(name = "organization_id", nullable = false)
  private Organization organization;

  /**
   * Constructs a new Product with all fields.
   *
   * @param name the product name
   * @param prices the list of prices
   * @param description the product description
   * @param skuCode the SKU code
   * @param dimensions the product dimensions
   * @param stockQuantity the stock quantity
   * @param categoryId the category ID
   * @param imageId the image ID
   * @param organization the organization
   */
  @JiltBuilder
  public Product(
      String name,
      List<ProductPrice> prices,
      String description,
      String skuCode,
      Dimensions dimensions,
      int stockQuantity,
      UUID categoryId,
      UUID imageId,
      Organization organization) {
    this.name = name;
    this.description = description;
    this.skuCode = skuCode;
    this.dimensions = dimensions;
    this.stockQuantity = stockQuantity;
    this.categoryId = categoryId;
    this.imageId = imageId;
    this.organization = organization;
    updatePrices(prices);
  }

  /**
   * Default constructor for JPA.
   */
  protected Product() {}

  /**
   * Updates this product from another instance.
   *
   * @param updated the updated product
   */
  @Override
  public void doUpdate(Product updated) {
    this.name = updated.name;
    this.description = updated.description;
    this.skuCode = updated.skuCode;
    this.dimensions = updated.dimensions;
    this.categoryId = updated.categoryId;
    this.imageId = updated.imageId;
    this.organization = updated.organization;
    this.changeStockQuantity(updated.getStockQuantity());
    this.updatePrices(updated.getPrices());
  }

  /**
   * Updates the prices for this product.
   *
   * @param updatedPrices the new list of prices
   */
  public void updatePrices(List<ProductPrice> updatedPrices) {
    this.prices.clear();
    if (updatedPrices != null) {
      updatedPrices.forEach(this::addPrice);
    }
  }

  /**
   * Adds a price to this product.
   *
   * @param price the price to add
   */
  public void addPrice(ProductPrice price) {
    if (price == null) throw new IllegalArgumentException("Price cannot be null");
    price.setProduct(this);
    this.prices.add(price);
  }

  /**
   * Changes the stock quantity for this product.
   *
   * @param newQuantity the new stock quantity
   */
  public void changeStockQuantity(int newQuantity) {
    if (newQuantity < 0) throw new IllegalArgumentException("Stock quantity cannot be negative");
    this.stockQuantity = newQuantity;
  }

  /**
   * Returns the list of prices for this product.
   *
   * @return the list of prices
   */
  public List<ProductPrice> getPrices() {
    return List.copyOf(prices);
  }

  /**
   * Returns the product name.
   *
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the product description.
   *
   * @return the description
   */
  public String getDescription() {
    return description;
  }

  /**
   * Returns the SKU code for this product.
   *
   * @return the SKU code
   */
  public String getSkuCode() {
    return skuCode;
  }

  /**
   * Returns the dimensions of this product.
   *
   * @return the dimensions
   */
  public Dimensions getDimensions() {
    return dimensions;
  }

  /**
   * Returns the stock quantity for this product.
   *
   * @return the stock quantity
   */
  public int getStockQuantity() {
    return stockQuantity;
  }

  /**
   * Returns the category ID for this product.
   *
   * @return the category ID
   */
  public UUID getCategoryId() {
    return categoryId;
  }

  /**
   * Returns the image ID for this product.
   *
   * @return the image ID
   */
  public UUID getImageId() {
    return imageId;
  }

  /**
   * Returns the organization for this product.
   *
   * @return the organization
   */
  public Organization getOrganization() {
    return organization;
  }

  /**
   * Sets the product name.
   *
   * @param name the name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Sets the product description.
   *
   * @param description the description
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Sets the SKU code for this product.
   *
   * @param skuCode the SKU code
   */
  public void setSkuCode(String skuCode) {
    this.skuCode = skuCode;
  }

  /**
   * Sets the dimensions of this product.
   *
   * @param dimensions the dimensions
   */
  public void setDimensions(Dimensions dimensions) {
    this.dimensions = dimensions;
  }

  /**
   * Sets the stock quantity for this product.
   *
   * @param stockQuantity the stock quantity
   */
  public void setStockQuantity(int stockQuantity) {
    this.stockQuantity = stockQuantity;
  }

  /**
   * Sets the category ID for this product.
   *
   * @param categoryId the category ID
   */
  public void setCategoryId(UUID categoryId) {
    this.categoryId = categoryId;
  }

  /**
   * Sets the image ID for this product.
   *
   * @param imageId the image ID
   */
  public void setImageId(UUID imageId) {
    this.imageId = imageId;
  }

  /**
   * Sets the organization for this product.
   *
   * @param organization the organization
   */
  public void setOrganization(Organization organization) {
    this.organization = organization;
  }
}
