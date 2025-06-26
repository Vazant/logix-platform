package com.vazant.logix.orders.infrastructure.init;

import com.vazant.logix.orders.infrastructure.kafka.CurrencyConversionClient;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrencyInitializer {
  private final CurrencyConversionClient currencyClient;

  @EventListener(ApplicationReadyEvent.class)
  public void init() {
    System.out.println("Initializing Currency Client");
    System.out.println(currencyClient.convert("PLN", "USD", BigDecimal.valueOf(1000)));
    System.out.println(currencyClient.convert("USD", "PLN", BigDecimal.valueOf(265.0095)));
  }
}
