package com.vazant.logix.currency.infrastructure.cache;

import com.vazant.logix.currency.domain.model.CurrencyRate;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@CacheConfig(cacheNames = "currencyRates")
public class CurrencyCacheService {

  @Cacheable(key = "#currencyCode")
  public CurrencyRate getRate(String currencyCode) {
    throw new IllegalStateException("❌ Rate not cached yet: " + currencyCode);
  }

  @CachePut(key = "#rate.targetCurrencyCode()")
  public CurrencyRate saveRate(CurrencyRate rate) {
    return rate;
  }
}
