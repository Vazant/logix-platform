package com.vazant.logix.currency.service;

import com.vazant.logix.currency.model.CurrencyRate;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@CacheConfig(cacheNames = "currencyRates")
public class CurrencyCacheService {

  @Cacheable(key = "#currencyCode")
  public CurrencyRate getRate(String currencyCode) {
    throw new IllegalStateException("Currency %s not cached yet".formatted(currencyCode));
  }

  @CachePut(key = "#rate.currency()")
  public CurrencyRate saveRate(CurrencyRate rate) {
    return rate;
  }
}
