package com.vazant.logix.currency.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.vazant.logix.currency.domain.model.CurrencyRate;
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
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableCaching
@EnableConfigurationProperties(CurrencyProperties.class)
@RequiredArgsConstructor
public class RedisConfig {

  private final CurrencyProperties properties;

  @Bean
  public GenericJackson2JsonRedisSerializer genericRedisSerializer() {
    ObjectMapper mapper =
        new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    return new GenericJackson2JsonRedisSerializer(mapper);
  }

  @Bean
  public Jackson2JsonRedisSerializer<CurrencyRate> currencyRateSerializer() {
    ObjectMapper mapper =
        new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    return new Jackson2JsonRedisSerializer<>(mapper, CurrencyRate.class);
  }

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
