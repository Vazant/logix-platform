package com.vazant.logix.orders.infrastructure.init;

import com.vazant.logix.orders.infrastructure.kafka.CurrencyConversionClient;
import com.vazant.logix.shared.Constants;
import java.math.BigDecimal;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * Initializes currency data on application startup.
 * Performs test requests to CurrencyConversionClient after the application is ready.
 */
@Service
public class CurrencyInitializer {
  private final CurrencyConversionClient currencyClient;

  /**
   * Constructs a new CurrencyInitializer.
   *
   * @param currencyClient the client for currency conversion
   */
  public CurrencyInitializer(CurrencyConversionClient currencyClient) {
    this.currencyClient = currencyClient;
  }

  @EventListener(ApplicationReadyEvent.class)
  public void init() {
    System.out.println("Initializing Currency Client");
    System.out.println(currencyClient.convert(Constants.CURRENCY_PLN, Constants.CURRENCY_USD, BigDecimal.valueOf(1000)));
    System.out.println(currencyClient.convert(Constants.CURRENCY_USD, Constants.CURRENCY_PLN, BigDecimal.valueOf(265.0095)));
  }
}
