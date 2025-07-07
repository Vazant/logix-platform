package com.vazant.logix.currency.application.service;

import com.vazant.logix.currency.domain.model.CurrencyRate;
import com.vazant.logix.currency.infrastructure.cache.CurrencyCacheService;
import com.vazant.logix.currency.infrastructure.config.CurrencyProperties;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.vazant.logix.currency.application.service.provider.CurrencyProviderClient;
import com.vazant.logix.currency.application.service.validation.CurrencyValidationService;

/**
 * Service for currency-related business logic and operations.
 * <p>
 * Handles currency rate updates, conversion, and validation.
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CurrencyApplicationService {

  private final CurrencyProviderClient providerClient;
  private final CurrencyValidationService validationService;
  private final CurrencyCacheService currencyCacheService;
  private final CurrencyProperties properties;

  /**
   * Updates currency rates from the external provider and saves them to the cache.
   * Logs errors for any failed saves.
   */
  public void updateRatesFromProvider() {
    var rates = providerClient.fetchRates();
    int saved = 0;
    for (var entry : rates.entrySet()) {
      try {
        currencyCacheService.saveRate(entry.getValue());
        saved++;
      } catch (Exception e) {
        log.error("Error saving rate {}: {}", entry.getKey(), e.getMessage(), e);
      }
    }
    log.info("Currency rates updated: {} items", saved);
  }

  /**
   * Converts an amount from one currency to another using cached rates.
   *
   * @param from the source currency code
   * @param to the target currency code
   * @param amount the amount to convert
   * @return the converted amount
   * @throws IllegalStateException if a required rate is missing or conversion fails
   */
  public BigDecimal convert(String from, String to, BigDecimal amount) {
    if (from.equalsIgnoreCase(to)) {
      return amount;
    }
    CurrencyRate fromRate = getRateOrThrow(from);
    CurrencyRate toRate = getRateOrThrow(to);
    return calculateConversion(amount, fromRate, toRate);
  }

  /**
   * Validates that required currency rates are present at startup.
   *
   * @param requiredCurrencyCodes the list of required currency codes
   */
  public void validateStartupRates(List<String> requiredCurrencyCodes) {
    validationService.validateStartupRates(requiredCurrencyCodes);
  }

  // Private helper methods are not documented for API consumers
  private CurrencyRate getRateOrThrow(String code) {
    try {
      return currencyCacheService.getRate(code);
    } catch (Exception e) {
      log.error("Failed to get currency rate '{}': {}", code, e.getMessage(), e);
      throw new IllegalStateException("Currency rate not found: " + code, e);
    }
  }

  private BigDecimal calculateConversion(BigDecimal amount, CurrencyRate fromRate, CurrencyRate toRate) {
    try {
      return amount
          .multiply(toRate.getRate())
          .divide(fromRate.getRate(), properties.getScale(), properties.getRoundingMode());
    } catch (ArithmeticException e) {
      log.error("Error converting currency {} -> {}: {}", fromRate.getTargetCurrencyCode(), toRate.getTargetCurrencyCode(), e.getMessage(), e);
      throw new IllegalStateException("Currency conversion error", e);
    }
  }
}
