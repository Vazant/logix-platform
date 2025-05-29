package com.vazant.logix.currency.infrastructure.init;

import com.vazant.logix.currency.application.scheduler.CurrencyRatesUpdater;
import com.vazant.logix.currency.infrastructure.config.CurrencyProperties;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrencyRatesInitializer {

  private final CurrencyRatesUpdater updater;
  private final StringRedisTemplate redisTemplate;
  private final CurrencyProperties properties;

  @EventListener(ApplicationReadyEvent.class)
  public void init() {
    var cacheName = properties.getCacheName();
    var keyPrefix = cacheName + "::";

    var keys = redisTemplate.keys(keyPrefix + "*");
    boolean shouldUpdate = true;

    if (!keys.isEmpty()) {
      shouldUpdate = false;
      for (String key : keys) {
        var ttl = redisTemplate.getExpire(key, TimeUnit.SECONDS);
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
