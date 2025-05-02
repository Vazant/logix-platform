package com.vazant.logix.currency.application.service;

import com.vazant.logix.currency.domain.model.CurrencyRate;
import com.vazant.logix.currency.infrastructure.cache.CurrencyCacheService;
import com.vazant.logix.currency.infrastructure.client.dto.CurrencyRatesResponse;
import com.vazant.logix.currency.infrastructure.config.CurrencyProperties;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class CurrencyApplicationService {

  private final RestTemplate restTemplate;
  private final CurrencyProperties properties;
  private final CurrencyCacheService currencyCacheService;

  public void updateRatesFromProvider() {
    try {
      String url =
          String.format(
              "%s?apikey=%s&base=%s",
              properties.getProviderUrl(), properties.getApiKey(), properties.getBaseCurrency());

      CurrencyRatesResponse response = restTemplate.getForObject(url, CurrencyRatesResponse.class);

      if (response == null || response.getRates() == null) {
        throw new IllegalStateException("❌ Пустой ответ от API валют");
      }

      int saved = 0;
      for (Map.Entry<String, String> entry : response.getRates().entrySet()) {
        try {
          BigDecimal rate = new BigDecimal(entry.getValue());
          CurrencyRate currencyRate =
              new CurrencyRate(entry.getKey(), rate, properties.getBaseCurrency(), Instant.now());
          currencyCacheService.saveRate(currencyRate);
          saved++;
        } catch (NumberFormatException e) {
          log.warn("❌ Невалидный курс: {} = {}", entry.getKey(), entry.getValue());
        }
      }

      log.info("✅ Курсы валют обновлены: {} штук", saved);
    } catch (Exception ex) {
      log.error("🚨 Ошибка при получении курсов валют — используем кэш", ex);
    }
  }

  public BigDecimal convert(String from, String to, BigDecimal amount) {
    if (from.equalsIgnoreCase(to)) return amount;

    CurrencyRate fromRate = currencyCacheService.getRate(from);
    CurrencyRate toRate = currencyCacheService.getRate(to);

    return amount
        .multiply(toRate.getRate())
        .divide(fromRate.getRate(), properties.getScale(), properties.getRoundingMode());
  }

  public void validateStartupRates(List<String> requiredCurrencyCodes) {
    for (String code : requiredCurrencyCodes) {
      try {
        currencyCacheService.getRate(code);
      } catch (Exception e) {
        log.warn("⚠️ Missing exchange rate for currency: {}", code);
      }
    }
  }
}
