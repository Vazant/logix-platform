package com.vazant.logix.orders.application.integration;

import com.vazant.logix.currency.api.RequiredCurrencyProvider;
import com.vazant.logix.orders.domain.shared.Currency;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class OrderCurrencyProvider implements RequiredCurrencyProvider {

  @Override
  public List<String> getRequiredCurrencyCodes() {
    return Arrays.stream(Currency.values()).map(Enum::name).toList();
  }
}
