package com.vazant.logix.currency.config;

import java.time.Duration;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.*;

@Configuration
@EnableCaching
@EnableConfigurationProperties({CurrencyProperties.class})
public class RedisConfig {

  @Bean
  public RedisCacheConfiguration defaultCacheConfig(CurrencyProperties currencyProperties) {
    return RedisCacheConfiguration.defaultCacheConfig()
        .serializeValuesWith(
            RedisSerializationContext.SerializationPair.fromSerializer(
                new GenericJackson2JsonRedisSerializer()))
        .entryTtl(Duration.ofHours(currencyProperties.getRedis().getTimeToLive()))
        .disableCachingNullValues();
  }

  @Bean
  public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer(
      CurrencyProperties currencyProperties) {
    return builder ->
        builder.withCacheConfiguration(
            "currencyRates",
            RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(
                    Duration.ofHours(currencyProperties.getRedis().getTimeToLiveCurrencyRates()))
                .serializeValuesWith(
                    RedisSerializationContext.SerializationPair.fromSerializer(
                        new GenericJackson2JsonRedisSerializer())));
  }
}
