package com.vazant.logix.currency.application.scheduler;

import com.vazant.logix.currency.application.service.CurrencyApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CurrencyRatesUpdater {

  private final CurrencyApplicationService service;

  public void updateRates() {
    service.updateRatesFromProvider();
  }
}
