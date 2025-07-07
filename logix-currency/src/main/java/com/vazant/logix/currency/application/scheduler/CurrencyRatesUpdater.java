package com.vazant.logix.currency.application.scheduler;

import com.vazant.logix.currency.application.service.CurrencyApplicationService;
import org.springframework.stereotype.Component;

/**
 * Service for updating currency rates from an external provider.
 * <p>
 * Used by the scheduler and initializer to keep currency rate data up to date.
 */
@Component
public class CurrencyRatesUpdater {

  private final CurrencyApplicationService service;

  /**
   * Constructs a new CurrencyRatesUpdater.
   *
   * @param service currency business logic service
   */
  public CurrencyRatesUpdater(CurrencyApplicationService service) {
    this.service = service;
  }

  /**
   * Updates currency rates from the external provider.
   */
  public void updateRates() {
    service.updateRatesFromProvider();
  }
}
