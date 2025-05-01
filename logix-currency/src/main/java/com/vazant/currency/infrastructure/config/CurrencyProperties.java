package com.vazant.currency.infrastructure.config;

import java.math.RoundingMode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

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

  private RetryProperties retry;
  private RedisTtlProperties redis;
  private Rest rest;
  private KafkaTopics kafka;

  @Getter
  @Setter
  public static class RetryProperties {
    private int maxAttempts;
    private long initialDelay;
    private double multiplier;
    private long maxDelay;
  }

  @Getter
  @Setter
  public static class RedisTtlProperties {
    private int ttlHours;
    private int ttlCurrencyRatesHours;
  }

  @Getter
  @Setter
  public static class Rest {
    private int connectTimeout;
    private int readTimeout;
  }

  @Getter
  @Setter
  public static class KafkaTopics {
    private String requestTopic;
    private String replyTopicPrefix;
  }
}
