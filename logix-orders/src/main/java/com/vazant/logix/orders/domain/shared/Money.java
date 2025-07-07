package com.vazant.logix.orders.domain.shared;

import com.vazant.logix.orders.infrastructure.utils.JiltBuilder;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * Value object representing a monetary amount and its currency.
 * <p>
 * Provides arithmetic operations and validation for monetary values.
 */
@Embeddable
public class Money {

  @NotNull(message = "Amount must not be null")
  @PositiveOrZero(message = "Amount must be positive or zero")
  private BigDecimal amount;

  @NotNull(message = "Currency must not be null")
  @Enumerated(EnumType.STRING)
  private Currency currency;

  /**
   * Default constructor for JPA.
   */
  protected Money() {}

  /**
   * Constructs a new Money object with the specified amount and currency.
   *
   * @param amount the monetary amount (must be non-negative)
   * @param currency the currency
   * @throws IllegalArgumentException if amount is null, negative, or currency is null
   */
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

  /**
   * Returns a Money object with zero amount for the specified currency.
   *
   * @param currency the currency
   * @return a Money object with zero amount
   */
  public static Money zero(Currency currency) {
    return new Money(BigDecimal.ZERO, currency);
  }

  /**
   * Adds another Money object to this one.
   *
   * @param other the other Money object
   * @return the sum
   * @throws IllegalArgumentException if other is null or currencies do not match
   */
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

  /**
   * Subtracts another Money object from this one.
   *
   * @param other the other Money object
   * @return the difference
   * @throws IllegalArgumentException if other is null, currencies do not match, or result is negative
   */
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

  /**
   * Returns the monetary amount.
   *
   * @return the amount
   */
  public BigDecimal getAmount() {
    return amount;
  }

  /**
   * Returns the currency.
   *
   * @return the currency
   */
  public Currency getCurrency() {
    return currency;
  }

  /**
   * Multiplies the amount by the specified multiplier.
   *
   * @param multiplier the multiplier (must be non-negative)
   * @return the result
   * @throws IllegalArgumentException if multiplier is negative
   */
  public Money multiply(int multiplier) {
    if (multiplier < 0) {
      throw new IllegalArgumentException("Multiplier must not be negative");
    }
    return new Money(this.amount.multiply(BigDecimal.valueOf(multiplier)), this.currency);
  }

  /**
   * Checks equality of this Money object with another.
   *
   * @param o the other object
   * @return true if equal, false otherwise
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Money money)) return false;
    return amount.equals(money.amount) && currency.equals(money.currency);
  }

  /**
   * Returns the hash code for this Money object.
   *
   * @return the hash code
   */
  @Override
  public int hashCode() {
    return Objects.hash(amount, currency);
  }

  /**
   * Returns a string representation of this Money object.
   *
   * @return string representation
   */
  @Override
  public String toString() {
    return "Money{" + "amount=" + amount + ", currency=" + currency + '}';
  }
}
