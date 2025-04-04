package com.vazant.logix.orders.domain.shared;

import com.vazant.logix.orders.infrastructure.utils.JiltBuilder;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.util.Objects;

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
    if (amount == null) {
      throw new IllegalArgumentException("Amount must not be null");
    }
    if (currency == null) {
      throw new IllegalArgumentException("Currency must not be null");
    }
    if (amount.compareTo(BigDecimal.ZERO) < 0) {
      throw new IllegalArgumentException("Amount must not be negative");
    }
    this.amount = amount;
    this.currency = currency;
  }

  public static Money zero(Currency currency) {
    return new Money(BigDecimal.ZERO, currency);
  }

  public Money add(Money other) {
    if (other == null) {
      throw new IllegalArgumentException("Cannot add null Money");
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

  public Money subtract(Money other) {
    if (other == null) {
      throw new IllegalArgumentException("Cannot subtract null Money");
    }
    if (!this.currency.equals(other.currency)) {
      throw new IllegalArgumentException(
          "Cannot subtract Money values with different currencies: "
              + this.currency
              + " and "
              + other.currency);
    }
    BigDecimal result = this.amount.subtract(other.amount);
    if (result.compareTo(BigDecimal.ZERO) < 0) {
      throw new IllegalArgumentException("Resulting amount cannot be negative");
    }
    return new Money(result, this.currency);
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public Currency getCurrency() {
    return currency;
  }

  public Money multiply(int multiplier) {
    if (multiplier < 0) {
      throw new IllegalArgumentException("Multiplier must not be negative");
    }
    return new Money(this.amount.multiply(BigDecimal.valueOf(multiplier)), this.currency);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Money money)) return false;
    return amount.equals(money.amount) && currency.equals(money.currency);
  }

  @Override
  public int hashCode() {
    return Objects.hash(amount, currency);
  }

  @Override
  public String toString() {
    return "Money{" + "amount=" + amount + ", currency=" + currency + '}';
  }
}
