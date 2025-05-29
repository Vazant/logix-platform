package com.vazant.logix.currency.domain.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
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
}
