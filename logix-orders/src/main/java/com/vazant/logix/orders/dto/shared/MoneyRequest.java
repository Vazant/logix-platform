package com.vazant.logix.orders.dto.shared;

import com.vazant.logix.orders.domain.shared.Currency;
import com.vazant.logix.orders.domain.shared.Money;
import java.math.BigDecimal;

/**
 * Request DTO representing a monetary value for API operations.
 * <p>
 * Contains the amount and currency code, and provides conversion to the domain Money object.
 *
 * @param amount the monetary amount
 * @param currencyCode the ISO currency code (e.g., "USD")
 */
public record MoneyRequest(BigDecimal amount, String currencyCode) {
  /**
   * Converts this request DTO to the domain Money object.
   *
   * @return the domain Money object
   * @throws IllegalArgumentException if the currency code is invalid
   */
  public Money toDomain() {
    return new Money(this.amount, Currency.valueOf(currencyCode));
  }
}
