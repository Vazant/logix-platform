package com.vazant.currency.api;

import com.vazant.currency.application.service.CurrencyApplicationService;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/currencies")
@RequiredArgsConstructor
public class CurrencyController {

  private final CurrencyApplicationService currencyService;

  @GetMapping("/convert")
  public BigDecimal convertCurrency(
      @RequestParam String from, @RequestParam String to, @RequestParam BigDecimal amount) {
    return currencyService.convert(from, to, amount);
  }

  @PostMapping("/update")
  public void updateCurrencies() {
    currencyService.updateRatesFromProvider();
  }
}
