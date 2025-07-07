package com.vazant.logix.currency.domain.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import lombok.Getter;

/**
 * Value Object representing an exchange rate from a base currency to a target currency.
 * <p>
 * Immutable and used for currency conversion operations.
 */
@Getter
public class CurrencyRate {

  private final String targetCurrencyCode;
  private final BigDecimal rate;
  private final String baseCurrencyCode;
  private final Instant updatedAt;

  /**
   * Constructs a new CurrencyRate.
   *
   * @param targetCurrencyCode the target currency code (e.g., "USD")
   * @param rate the exchange rate (must be positive)
   * @param baseCurrencyCode the base currency code (e.g., "EUR")
   * @param updatedAt the timestamp when the rate was last updated
   * @throws IllegalArgumentException if rate is null or not positive
   */
  @JsonCreator
  public CurrencyRate(
      @JsonProperty("targetCurrencyCode") String targetCurrencyCode,
      @JsonProperty("rate") BigDecimal rate,
      @JsonProperty("baseCurrencyCode") String baseCurrencyCode,
      @JsonProperty("updatedAt") Instant updatedAt) {
    if (rate == null || rate.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("Rate must be positive");
    }
    this.targetCurrencyCode = targetCurrencyCode.toUpperCase();
    this.rate = rate;
    this.baseCurrencyCode = baseCurrencyCode.toUpperCase();
    this.updatedAt = updatedAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof CurrencyRate that)) return false;
    return targetCurrencyCode.equals(that.targetCurrencyCode)
        && rate.equals(that.rate)
        && baseCurrencyCode.equals(that.baseCurrencyCode);
  }

  @Override
  public int hashCode() {
    return Objects.hash(targetCurrencyCode, rate, baseCurrencyCode);
  }

  @Override
  public String toString() {
    return targetCurrencyCode + "=" + rate;
  }

  /**
   * Returns the exchange rate value.
   *
   * @return the exchange rate
   */
  public BigDecimal getRate() { return rate; }

  /**
   * Returns the target currency code.
   *
   * @return the target currency code
   */
  public String getTargetCurrencyCode() { return targetCurrencyCode; }
}
