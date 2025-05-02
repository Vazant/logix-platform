package com.vazant.logix.currency.application.scheduler;

import com.vazant.logix.currency.application.service.CurrencyApplicationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CurrencyRatesUpdater {

  private final CurrencyApplicationService service;

  public void updateRates() {
    log.info("⏰ Обновление курсов валют по расписанию...");
    service.updateRatesFromProvider();
  }
}
