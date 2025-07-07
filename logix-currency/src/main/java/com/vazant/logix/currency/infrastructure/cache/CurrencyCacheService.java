package com.vazant.logix.currency.infrastructure.cache;

import com.vazant.logix.currency.domain.model.CurrencyRate;
import com.vazant.logix.currency.infrastructure.config.CurrencyProperties;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

/**
 * Service for caching and retrieving currency rates.
 * <p>
 * Uses a Spring CacheManager to store and fetch CurrencyRate objects by currency code.
 */
@Service
public class CurrencyCacheService {

  private final CacheManager cacheManager;
  private final String cacheName;

  /**
   * Constructs a new CurrencyCacheService.
   *
   * @param properties the currency properties containing the cache name
   * @param cacheManager the Spring CacheManager
   */
  public CurrencyCacheService(CurrencyProperties properties, CacheManager cacheManager) {
    this.cacheManager = cacheManager;
    this.cacheName = properties.getCacheName();
  }

  /**
   * Retrieves a currency rate from the cache by currency code.
   *
   * @param currencyCode the currency code
   * @return the cached CurrencyRate
   * @throws IllegalStateException if the cache or rate is not found
   */
  public CurrencyRate getRate(String currencyCode) {
    var cache = cacheManager.getCache(cacheName);
    if (cache == null) throw new IllegalStateException("❌ Cache not found: " + cacheName);
    CurrencyRate rate = cache.get(currencyCode, CurrencyRate.class);
    if (rate == null) throw new IllegalStateException("❌ Rate not cached yet: " + currencyCode);
    return rate;
  }

  /**
   * Saves a currency rate to the cache.
   *
   * @param rate the CurrencyRate to save
   * @return the saved CurrencyRate
   * @throws IllegalStateException if the cache is not found
   */
  public CurrencyRate saveRate(CurrencyRate rate) {
    var cache = cacheManager.getCache(cacheName);
    if (cache == null) throw new IllegalStateException("❌ Cache not found: " + cacheName);
    cache.put(rate.getTargetCurrencyCode(), rate);
    return rate;
  }
}
