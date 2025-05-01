package com.vazant.currency.infrastructure.config;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableCaching
@EnableConfigurationProperties(CurrencyProperties.class)
@RequiredArgsConstructor
public class RedisConfig {

  private final CurrencyProperties properties;

  @Bean
  public RedisCacheConfiguration defaultCacheConfiguration() {
    return RedisCacheConfiguration.defaultCacheConfig()
        .entryTtl(Duration.ofHours(properties.getRedis().getTtlHours()))
        .disableCachingNullValues()
        .serializeValuesWith(
            RedisSerializationContext.SerializationPair.fromSerializer(
                new GenericJackson2JsonRedisSerializer()));
  }

  @Bean
  public RedisCacheManager cacheManager(RedisConnectionFactory factory) {
    return RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(factory)
        .withCacheConfiguration("currencyRates", defaultCurrencyRatesCacheConfiguration())
        .cacheDefaults(defaultCacheConfiguration())
        .build();
  }

  @Bean
  public RedisCacheConfiguration defaultCurrencyRatesCacheConfiguration() {
    return RedisCacheConfiguration.defaultCacheConfig()
        .entryTtl(Duration.ofHours(properties.getRedis().getTtlCurrencyRatesHours()))
        .disableCachingNullValues()
        .serializeValuesWith(
            RedisSerializationContext.SerializationPair.fromSerializer(
                new GenericJackson2JsonRedisSerializer()));
  }

  @Bean
  public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
    RedisTemplate<String, Object> template = new RedisTemplate<>();
    template.setConnectionFactory(factory);
    template.setKeySerializer(new StringRedisSerializer());
    template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
    return template;
  }
}
