package com.vazant.logix.currency.infrastructure.cache;

import com.vazant.logix.currency.domain.model.CurrencyRate;
import com.vazant.logix.currency.infrastructure.config.CurrencyProperties;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

@Service
public class CurrencyCacheService {

  private final CacheManager cacheManager;
  private final String cacheName;

  public CurrencyCacheService(CurrencyProperties properties, CacheManager cacheManager) {
    this.cacheManager = cacheManager;
    this.cacheName = properties.getCacheName();
  }

  public CurrencyRate getRate(String currencyCode) {
    var cache = cacheManager.getCache(cacheName);
    if (cache == null) throw new IllegalStateException("❌ Cache not found: " + cacheName);
    CurrencyRate rate = cache.get(currencyCode, CurrencyRate.class);
    if (rate == null) throw new IllegalStateException("❌ Rate not cached yet: " + currencyCode);
    return rate;
  }

  public CurrencyRate saveRate(CurrencyRate rate) {
    var cache = cacheManager.getCache(cacheName);
    if (cache == null) throw new IllegalStateException("❌ Cache not found: " + cacheName);
    cache.put(rate.getTargetCurrencyCode(), rate);
    return rate;
  }
}
