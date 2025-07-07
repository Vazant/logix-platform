package com.vazant.logix.currency.application.service.validation;

import com.vazant.logix.currency.infrastructure.cache.CurrencyCacheService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service for validating the presence of required currency rates at application startup.
 * <p>
 * Logs warnings for any missing required rates.
 */
@Service
public class CurrencyValidationService {
  private static final Logger log = LoggerFactory.getLogger(CurrencyValidationService.class);
  private final CurrencyCacheService currencyCacheService;

  /**
   * Constructs a new CurrencyValidationService.
   *
   * @param currencyCacheService the cache service for currency rates
   */
  public CurrencyValidationService(CurrencyCacheService currencyCacheService) {
    this.currencyCacheService = currencyCacheService;
  }

  /**
   * Validates that all required currency rates are present in the cache.
   * Logs a warning for each missing rate.
   *
   * @param requiredCurrencyCodes the list of required currency codes
   */
  public void validateStartupRates(List<String> requiredCurrencyCodes) {
    for (String code : requiredCurrencyCodes) {
      try {
        currencyCacheService.getRate(code);
      } catch (Exception e) {
        log.warn("\u26a0\uFE0F Не найден курс для обязательной валюты '{}': {}", code, e.getMessage());
      }
    }
  }
} 