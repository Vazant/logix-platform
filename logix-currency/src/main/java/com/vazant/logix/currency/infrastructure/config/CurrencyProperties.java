package com.vazant.logix.currency.infrastructure.config;

import java.math.RoundingMode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for the currency service.
 * <p>
 * Maps properties with the prefix 'currency' from the application configuration.
 * Includes nested classes for retry, Redis TTL, REST, and Kafka topic settings.
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "currency")
public class CurrencyProperties {

  private String scheduleCron;
  private String apiKey;
  private String providerUrl;
  private String baseCurrency;
  private int scale;
  private RoundingMode roundingMode;
  private String cacheName;

  private RetryProperties retry;
  private RedisTtlProperties redis;
  private Rest rest;
  private KafkaTopics kafka;

  /**
   * Retry configuration properties for currency operations.
   */
  @Getter
  @Setter
  public static class RetryProperties {
    private int maxAttempts = 3;
    private long initialDelay = 1000;
    private double multiplier = 2.0;
    private long maxDelay = 5000;
    public int getMaxAttempts() { return maxAttempts; }
    public long getInitialDelay() { return initialDelay; }
    public double getMultiplier() { return multiplier; }
    public long getMaxDelay() { return maxDelay; }
  }

  /**
   * Redis TTL configuration properties for currency caching.
   */
  @Getter
  @Setter
  public static class RedisTtlProperties {
    private int ttlHours = 24;
    private int ttlCurrencyRatesHours = 12;
    public int getTtlHours() { return ttlHours; }
    public int getTtlCurrencyRatesHours() { return ttlCurrencyRatesHours; }
  }

  /**
   * REST client configuration properties for currency service.
   */
  @Getter
  @Setter
  public static class Rest {
    private int connectTimeout = 5;
    private int readTimeout = 5;
    public int getConnectTimeout() { return connectTimeout; }
    public int getReadTimeout() { return readTimeout; }
  }

  /**
   * Kafka topic configuration properties for currency service.
   */
  @Getter
  @Setter
  public static class KafkaTopics {
    private String requestTopic;
    private String replyTopicPrefix;
    private String groupId;
    public String getRequestTopic() { return requestTopic; }
    public String getReplyTopicPrefix() { return replyTopicPrefix; }
    public String getGroupId() { return groupId; }
  }

  public String getScheduleCron() { return scheduleCron; }
  public String getApiKey() { return apiKey; }
  public String getProviderUrl() { return providerUrl; }
  public String getBaseCurrency() { return baseCurrency; }
  public int getScale() { return scale; }
  public RoundingMode getRoundingMode() { return roundingMode; }
  public String getCacheName() { return cacheName; }
  public RetryProperties getRetry() { return retry; }
  public RedisTtlProperties getRedis() { return redis; }
  public Rest getRest() { return rest; }
  public KafkaTopics getKafka() { return kafka; }
}
