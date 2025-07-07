package com.vazant.logix.orders.domain.product;

import com.vazant.logix.orders.infrastructure.utils.JiltBuilder;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

/**
 * Value object representing the physical dimensions and weight of a product.
 * <p>
 * Immutable and embeddable in JPA entities.
 */
@Embeddable
public class Dimensions {

  @NotNull(message = "Width must not be null")
  @PositiveOrZero(message = "Width must be positive or zero")
  private final BigDecimal width;

  @NotNull(message = "Height must not be null")
  @PositiveOrZero(message = "Height must be positive or zero")
  private final BigDecimal height;

  @NotNull(message = "Length must not be null")
  @PositiveOrZero(message = "Length must be positive or zero")
  private final BigDecimal length;

  @NotNull(message = "Weight must not be null")
  @PositiveOrZero(message = "Weight must be positive or zero")
  private final BigDecimal weight;

  /**
   * Default constructor for JPA, initializes all fields to zero.
   */
  protected Dimensions() {
    this.width = BigDecimal.ZERO;
    this.height = BigDecimal.ZERO;
    this.length = BigDecimal.ZERO;
    this.weight = BigDecimal.ZERO;
  }

  /**
   * Constructs a new Dimensions object with the specified values.
   *
   * @param width the width
   * @param height the height
   * @param length the length
   * @param weight the weight
   */
  @JiltBuilder
  public Dimensions(BigDecimal width, BigDecimal height, BigDecimal length, BigDecimal weight) {
    this.width = width;
    this.height = height;
    this.length = length;
    this.weight = weight;
  }

  /**
   * Returns the width.
   *
   * @return the width
   */
  public BigDecimal getWidth() {
    return width;
  }

  /**
   * Returns the height.
   *
   * @return the height
   */
  public BigDecimal getHeight() {
    return height;
  }

  /**
   * Returns the length.
   *
   * @return the length
   */
  public BigDecimal getLength() {
    return length;
  }

  /**
   * Returns the weight.
   *
   * @return the weight
   */
  public BigDecimal getWeight() {
    return weight;
  }
}
