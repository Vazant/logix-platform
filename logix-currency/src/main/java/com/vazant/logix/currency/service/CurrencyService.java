package com.vazant.logix.currency.service;

import com.vazant.logix.currency.config.CurrencyProperties;
import com.vazant.logix.currency.model.CurrencyRate;
import com.vazant.logix.currency.model.CurrencyRatesResponse;
import jakarta.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.RestTemplate;

@Service
@Validated
@CacheConfig(cacheNames = "currencyRates")
public class CurrencyService {

  private static final Logger log = LoggerFactory.getLogger(CurrencyService.class);

  private final CurrencyProperties properties;
  private final RestTemplate restTemplate;
  private final Map<String, CurrencyRate> localCache = new ConcurrentHashMap<>();

  public CurrencyService(CurrencyProperties properties, RestTemplate restTemplate) {
    this.properties = properties;
    this.restTemplate = restTemplate;
  }

  @PostConstruct
  public void init() {
    fetchExchangeRates(); // вручную запускаем при старте
    logStartupConfig();
  }

  @Scheduled(cron = "${logix.currency.cron:0 0 0 * * *}")
  public void fetchExchangeRates() {
    doFetchRates();
  }

  @Retryable
  private void doFetchRates() {
    log.info("Fetching currency exchange rates...");

    try {
      String url =
          String.format(
              "%s?apikey=%s&base=%s",
              properties.getProviderUrl(), properties.getApiKey(), properties.getBaseCurrency());

      CurrencyRatesResponse response = restTemplate.getForObject(url, CurrencyRatesResponse.class);

      if (response != null && response.rates() != null) {
        response
            .rates()
            .forEach(
                (code, rateStr) -> {
                  try {
                    BigDecimal rate = new BigDecimal(rateStr);
                    localCache.put(code, new CurrencyRate(code, rate));
                  } catch (NumberFormatException ex) {
                    log.warn("Invalid rate format: {} = {}", code, rateStr);
                  }
                });

        log.info("Exchange rates updated: {} currencies", localCache.size());
      }

    } catch (Exception e) {
      log.error("Error fetching currency rates", e);
      throw e; // для Retry
    }
  }

  public Optional<CurrencyRate> getRate(String currencyCode) {
    return Optional.ofNullable(localCache.get(currencyCode));
  }

  public BigDecimal convert(String fromCurrency, String toCurrency, BigDecimal amount) {
    if (fromCurrency.equalsIgnoreCase(toCurrency)) return amount;

    CurrencyRate from = localCache.get(fromCurrency);
    CurrencyRate to = localCache.get(toCurrency);

    if (from == null || to == null) {
      throw new IllegalStateException(
          "Currency rate not found for: %s → %s".formatted(fromCurrency, toCurrency));
    }

    return amount
        .multiply(to.rate())
        .divide(from.rate(), properties.getScale(), properties.getRoundingMode());
  }

  private void logStartupConfig() {
    log.info(
        "CurrencyService initialized with baseCurrency={}, providerUrl={}, cron={}, scale={}, rounding={}",
        properties.getBaseCurrency(),
        properties.getProviderUrl(),
        properties.getCron(),
        properties.getScale(),
        properties.getRoundingMode());
  }
}
