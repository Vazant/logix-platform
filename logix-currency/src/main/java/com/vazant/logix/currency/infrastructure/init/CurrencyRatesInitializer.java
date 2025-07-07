package com.vazant.logix.currency.infrastructure.init;

import com.vazant.logix.currency.application.scheduler.CurrencyRatesUpdater;
import com.vazant.logix.currency.infrastructure.config.CurrencyProperties;
import java.util.concurrent.TimeUnit;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * Initializes currency rates on application startup.
 * <p>
 * Checks the presence and freshness of rates in Redis and triggers update if needed.
 */
@Service
public class CurrencyRatesInitializer {

  private final CurrencyRatesUpdater updater;
  private final StringRedisTemplate redisTemplate;
  private final CurrencyProperties properties;

  /**
   * Constructs a new CurrencyRatesInitializer.
   *
   * @param updater service for updating currency rates
   * @param redisTemplate template for working with Redis
   * @param properties currency service properties
   */
  public CurrencyRatesInitializer(CurrencyRatesUpdater updater, StringRedisTemplate redisTemplate, CurrencyProperties properties) {
    this.updater = updater;
    this.redisTemplate = redisTemplate;
    this.properties = properties;
  }

  /**
   * Checks the cache for currency rates and updates them if missing or expired.
   * Invoked on application startup.
   */
  @EventListener(ApplicationReadyEvent.class)
  public void init() {
    var cacheName = properties.getCacheName();
    var keyPrefix = cacheName + "::";

    var keys = redisTemplate.keys(keyPrefix + "*");
    boolean shouldUpdate = true;

    if (!keys.isEmpty()) {
      shouldUpdate = false;
      for (String key : keys) {
        var ttl = redisTemplate.getExpire(key, java.util.concurrent.TimeUnit.SECONDS);
        if (ttl <= 0) {
          shouldUpdate = true;
          break;
        }
      }
    }

    if (shouldUpdate) {
      updater.updateRates();
    }
  }
}
