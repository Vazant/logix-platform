package com.vazant.logix.currency.infrastructure.cache;

import com.vazant.logix.currency.domain.model.CurrencyRate;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@CacheConfig(cacheNames = "${currency.cache-name}")
public class CurrencyCacheService {

  @Cacheable(key = "#p0")
  public CurrencyRate getRate(String currencyCode) {
    throw new IllegalStateException("❌ Rate not cached yet: " + currencyCode);
  }

  @CachePut(key = "#p0.targetCurrencyCode")
  public CurrencyRate saveRate(CurrencyRate rate) {
    return rate;
  }
}
