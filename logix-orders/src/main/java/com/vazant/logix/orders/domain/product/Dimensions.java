package com.vazant.logix.orders.domain.product;

import com.vazant.logix.orders.infrastructure.utils.JiltBuilder;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

@Embeddable
public class Dimensions {

  @NotNull(message = "Width must not be null")
  @PositiveOrZero(message = "Width must be positive or zero")
  private BigDecimal width;

  @NotNull(message = "Height must not be null")
  @PositiveOrZero(message = "Height must be positive or zero")
  private BigDecimal height;

  @NotNull(message = "Length must not be null")
  @PositiveOrZero(message = "Length must be positive or zero")
  private BigDecimal length;

  @NotNull(message = "Weight must not be null")
  @PositiveOrZero(message = "Weight must be positive or zero")
  private BigDecimal weight;

  protected Dimensions() {}

  @JiltBuilder
  public Dimensions(BigDecimal width, BigDecimal height, BigDecimal length, BigDecimal weight) {
    this.width = width;
    this.height = height;
    this.length = length;
    this.weight = weight;
  }

  public BigDecimal getWidth() {
    return width;
  }

  public BigDecimal getHeight() {
    return height;
  }

  public BigDecimal getLength() {
    return length;
  }

  public BigDecimal getWeight() {
    return weight;
  }
}
