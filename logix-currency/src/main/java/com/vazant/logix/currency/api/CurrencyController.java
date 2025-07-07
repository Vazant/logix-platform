package com.vazant.logix.currency.api;

import com.vazant.logix.currency.application.service.CurrencyApplicationService;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

/**
 * REST controller for currency conversion and rate update operations.
 * <p>
 * Provides endpoints to convert currency and trigger rate updates.
 */
@RestController
@RequestMapping("/api/currency")
@RequiredArgsConstructor
public class CurrencyController {

  private final CurrencyApplicationService currencyService;

  /**
   * Converts an amount from one currency to another.
   *
   * @param from the source currency code
   * @param to the target currency code
   * @param amount the amount to convert
   * @return the converted amount
   */
  @GetMapping("/convert")
  public ResponseEntity<BigDecimal> convertCurrency(
      @RequestParam @NotBlank String from, 
      @RequestParam @NotBlank String to, 
      @RequestParam @NotNull @DecimalMin("0.01") BigDecimal amount) {
    
    BigDecimal result = currencyService.convert(from, to, amount);
    return ResponseEntity.ok(result);
  }

  /**
   * Triggers an update of currency rates from the provider.
   *
   * @return success message
   */
  @PostMapping("/update")
  public ResponseEntity<Map<String, String>> updateCurrencies() {
    currencyService.updateRatesFromProvider();
    return ResponseEntity.ok(Map.of("message", "Currency rates updated successfully"));
  }

  /**
   * Gets the current exchange rate between two currencies.
   *
   * @param from the source currency code
   * @param to the target currency code
   * @return the current exchange rate
   */
  @GetMapping("/rate")
  public ResponseEntity<BigDecimal> getExchangeRate(
      @RequestParam @NotBlank String from, 
      @RequestParam @NotBlank String to) {
    
    // TODO: Implement getting exchange rate
    // This would require adding a method to CurrencyApplicationService
    return ResponseEntity.ok(BigDecimal.ONE);
  }

  /**
   * Gets all available currencies.
   *
   * @return list of available currency codes
   */
  @GetMapping("/available")
  public ResponseEntity<Map<String, String>> getAvailableCurrencies() {
    // TODO: Implement getting available currencies
    return ResponseEntity.ok(Map.of("currencies", "USD,EUR,RUB"));
  }
}
