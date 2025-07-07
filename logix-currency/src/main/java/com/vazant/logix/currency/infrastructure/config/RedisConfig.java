package com.vazant.logix.currency.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.vazant.logix.currency.domain.model.CurrencyRate;
import java.time.Duration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis configuration for currency service caching.
 * <p>
 * Configures cache manager, serializers, and Redis template for storing currency rates.
 */
@Configuration
@EnableCaching
@EnableConfigurationProperties(CurrencyProperties.class)
public class RedisConfig {

  private final CurrencyProperties properties;

  /**
   * Constructs a new RedisConfig with the given currency properties.
   *
   * @param properties the currency configuration properties
   */
  public RedisConfig(CurrencyProperties properties) {
    this.properties = properties;
  }

  /**
   * Generic JSON serializer for Redis values.
   *
   * @return the generic Jackson2 JSON Redis serializer
   */
  @Bean
  public GenericJackson2JsonRedisSerializer genericRedisSerializer() {
    ObjectMapper mapper =
        new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    return new GenericJackson2JsonRedisSerializer(mapper);
  }

  /**
   * Jackson2 JSON serializer for CurrencyRate objects.
   *
   * @return the Jackson2 JSON Redis serializer for CurrencyRate
   */
  @Bean
  public Jackson2JsonRedisSerializer<CurrencyRate> currencyRateSerializer() {
    ObjectMapper mapper =
        new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    return new Jackson2JsonRedisSerializer<>(mapper, CurrencyRate.class);
  }

  /**
   * Configures the Redis cache manager with custom cache settings.
   *
   * @param factory the Redis connection factory
   * @param genericSerializer the generic JSON serializer
   * @param rateSerializer the serializer for CurrencyRate objects
   * @return the Redis cache manager
   */
  @Bean
  public RedisCacheManager cacheManager(
      RedisConnectionFactory factory,
      GenericJackson2JsonRedisSerializer genericSerializer,
      Jackson2JsonRedisSerializer<CurrencyRate> rateSerializer) {

    RedisCacheConfiguration defaultConfig =
        RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofHours(properties.getRedis().getTtlHours()))
            .disableCachingNullValues()
            .serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(genericSerializer));

    RedisCacheConfiguration ratesConfig =
        RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofHours(properties.getRedis().getTtlCurrencyRatesHours()))
            .disableCachingNullValues()
            .serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(rateSerializer));

    return RedisCacheManager.builder(factory)
        .cacheDefaults(defaultConfig)
        .withCacheConfiguration(properties.getCacheName(), ratesConfig)
        .build();
  }

  /**
   * Configures the Redis template for general-purpose Redis operations.
   *
   * @param factory the Redis connection factory
   * @param genericSerializer the generic JSON serializer
   * @return the Redis template
   */
  @Bean
  public RedisTemplate<String, Object> redisTemplate(
      RedisConnectionFactory factory, GenericJackson2JsonRedisSerializer genericSerializer) {

    RedisTemplate<String, Object> template = new RedisTemplate<>();
    template.setConnectionFactory(factory);
    template.setKeySerializer(new StringRedisSerializer());
    template.setValueSerializer(genericSerializer);
    template.setHashKeySerializer(new StringRedisSerializer());
    template.setHashValueSerializer(genericSerializer);
    return template;
  }
}
