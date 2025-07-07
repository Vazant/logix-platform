package com.vazant.logix.currency.application.service.provider;

import com.vazant.logix.currency.domain.model.CurrencyRate;
import com.vazant.logix.currency.infrastructure.client.dto.CurrencyRatesResponse;
import com.vazant.logix.currency.infrastructure.config.CurrencyProperties;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CurrencyProviderClient {
  private static final Logger log = LoggerFactory.getLogger(CurrencyProviderClient.class);
  private final RestTemplate restTemplate;
  private final CurrencyProperties properties;

  public CurrencyProviderClient(RestTemplate restTemplate, CurrencyProperties properties) {
    this.restTemplate = restTemplate;
    this.properties = properties;
  }

  public Map<String, CurrencyRate> fetchRates() {
    String url = String.format("%s?apikey=%s&base=%s",
        properties.getProviderUrl(), properties.getApiKey(), properties.getBaseCurrency());
    CurrencyRatesResponse response = restTemplate.getForObject(url, CurrencyRatesResponse.class);
    if (response == null || response.getRates() == null) {
      throw new IllegalStateException("\u274c Пустой ответ от API валют");
    }
    Map<String, CurrencyRate> result = new HashMap<>();
    for (Map.Entry<String, String> entry : response.getRates().entrySet()) {
      try {
        BigDecimal rate = new BigDecimal(entry.getValue());
        CurrencyRate currencyRate = new CurrencyRate(entry.getKey(), rate, properties.getBaseCurrency(), Instant.now());
        result.put(entry.getKey(), currencyRate);
      } catch (NumberFormatException e) {
        log.warn("\u274c Невалидный курс: {} = {}", entry.getKey(), entry.getValue(), e);
      }
    }
    return result;
  }
} 