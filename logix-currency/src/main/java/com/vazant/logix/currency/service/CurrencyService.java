package com.vazant.logix.currency.service;

import com.vazant.logix.currency.api.RequiredCurrencyProvider;
import com.vazant.logix.currency.config.CurrencyProperties;
import com.vazant.logix.currency.model.CurrencyRate;
import com.vazant.logix.currency.model.CurrencyRatesResponse;
import jakarta.annotation.PostConstruct;
import java.math.BigDecimal;
import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CurrencyService {

  private static final Logger log = LoggerFactory.getLogger(CurrencyService.class);

  private final CurrencyProperties properties;
  private final RestTemplate restTemplate;
  private final RequiredCurrencyProvider currencyProvider;
  private final CurrencyCacheService currencyCacheService;

  public CurrencyService(
      CurrencyProperties properties,
      RestTemplate restTemplate,
      RequiredCurrencyProvider currencyProvider,
      CurrencyCacheService currencyCacheService) {
    this.properties = properties;
    this.restTemplate = restTemplate;
    this.currencyProvider = currencyProvider;
    this.currencyCacheService = currencyCacheService;
  }

  @PostConstruct
  public void init() {
    fetchExchangeRates(); // запрашиваем курсы при запуске
    validateStartupRates(); // убеждаемся, что нужные валюты загружены
    logStartupConfig();
  }

  @Scheduled(cron = "${logix.currency.cron:0 0 3 * * *}")
  @Retryable
  public void fetchExchangeRates() {
    log.info("📥 Fetching currency exchange rates...");

    try {
      String url =
          String.format(
              "%s?apikey=%s&base=%s",
              properties.getProviderUrl(), properties.getApiKey(), properties.getBaseCurrency());

      CurrencyRatesResponse response = restTemplate.getForObject(url, CurrencyRatesResponse.class);

      if (response != null && response.rates() != null) {
        Instant now = Instant.now();
        response
            .rates()
            .forEach(
                (code, rateStr) -> {
                  try {
                    BigDecimal rate = new BigDecimal(rateStr);
                    currencyCacheService.saveRate(
                        new CurrencyRate(code, rate, properties.getBaseCurrency(), now));
                  } catch (NumberFormatException ex) {
                    log.warn("❌ Invalid rate format: {} = {}", code, rateStr);
                  }
                });

        log.info("✅ Exchange rates updated: {}", response.rates().size());
      }

    } catch (Exception e) {
      log.error("🚨 Error fetching currency rates", e);
      throw e;
    }
  }

  public BigDecimal convert(String from, String to, BigDecimal amount) {
    if (from.equalsIgnoreCase(to)) return amount;

    CurrencyRate fromRate = currencyCacheService.getRate(from);
    CurrencyRate toRate = currencyCacheService.getRate(to);

    return amount
        .multiply(toRate.rate())
        .divide(fromRate.rate(), properties.getScale(), properties.getRoundingMode());
  }

  public void validateStartupRates() {
    for (String code : currencyProvider.getRequiredCurrencyCodes()) {
      try {
        currencyCacheService.getRate(code);
      } catch (Exception e) {
        log.warn("⚠️ Missing exchange rate for currency: {}", code);
      }
    }
  }

  private void logStartupConfig() {
    log.info(
        """
        CurrencyService config:
        - baseCurrency: {}
        - providerUrl: {}
        - cron: {}
        - roundingMode: {}
        - scale: {}
        """,
        properties.getBaseCurrency(),
        properties.getProviderUrl(),
        properties.getCron(),
        properties.getRoundingMode(),
        properties.getScale());
  }
}
