package com.vazant.logix.orders.domain.shared;

import com.vazant.logix.orders.sdk.utils.JiltBuilder;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

@Embeddable
public class Money {

  @NotNull(message = "Amount must not be null")
  @PositiveOrZero(message = "Amount must be positive or zero")
  private BigDecimal amount;

  @NotNull(message = "Currency must not be null")
  @Enumerated(EnumType.STRING)
  private Currency currency;

  protected Money() {}

  @JiltBuilder
  public Money(BigDecimal amount, Currency currency) {
    this.amount = amount;
    this.currency = currency;
  }

  public static Money zero(Currency currency) {
    return new Money(BigDecimal.ZERO, currency);
  }

  public Money add(Money other) {
    if (other == null) {
      return this;
    }
    if (this.amount.compareTo(BigDecimal.ZERO) == 0) {
      return new Money(other.amount, other.currency);
    }
    if (other.amount.compareTo(BigDecimal.ZERO) == 0) {
      return this;
    }

    if (!this.currency.equals(other.currency)) {
      throw new IllegalArgumentException(
          "Cannot add Money values with different currencies: "
              + this.currency
              + " and "
              + other.currency);
    }
    return new Money(this.amount.add(other.amount), this.currency);
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public Currency getCurrency() {
    return currency;
  }

  public Money multiply(int multiplier) {
    return new Money(this.amount.multiply(BigDecimal.valueOf(multiplier)), this.currency);
  }
}
