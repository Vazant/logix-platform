package com.vazant.logix.currency.domain.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import lombok.Getter;

/** Value Object, representing exchange rate from base → target currency. */
@Getter
public class CurrencyRate {

  private final String targetCurrencyCode;
  private final BigDecimal rate;
  private final String baseCurrencyCode;
  private final Instant updatedAt;

  public CurrencyRate(
      String targetCurrencyCode, BigDecimal rate, String baseCurrencyCode, Instant updatedAt) {
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
}
