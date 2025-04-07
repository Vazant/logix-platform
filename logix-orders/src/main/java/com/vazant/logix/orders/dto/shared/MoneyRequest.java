package com.vazant.logix.orders.dto.shared;

import com.vazant.logix.orders.domain.shared.Currency;
import com.vazant.logix.orders.domain.shared.Money;
import java.math.BigDecimal;

public record MoneyRequest(BigDecimal amount, String currencyCode) {
  public Money toDomain() {
    return new Money(this.amount, Currency.valueOf(currencyCode));
  }
}
