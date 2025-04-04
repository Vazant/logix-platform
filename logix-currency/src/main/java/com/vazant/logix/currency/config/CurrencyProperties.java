package com.vazant.logix.currency.config;

import jakarta.validation.constraints.NotNull;
import java.math.RoundingMode;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.client.RestTemplate;

@Validated
@ConfigurationProperties(prefix = "logix.currency")
public class CurrencyProperties {

  @NotNull private String providerUrl;
  @NotNull private String apiKey;
  @NotNull private String baseCurrency;
  @NotNull private String cron;
  @NotNull private int scale;
  @NotNull private RoundingMode roundingMode;
  @NotNull private Retry retry = new Retry();
  @NotNull private Redis redis = new Redis();
  @NotNull private RestTemplate restTemplate = new RestTemplate();

  public RestTemplate getRestTemplate() {
    return restTemplate;
  }

  public void setRestTemplate(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }

  public Redis getRedis() {
    return redis;
  }

  public void setRedis(Redis redis) {
    this.redis = redis;
  }

  @NotNull
  public Retry getRetry() {
    return retry;
  }

  public void setRetry(Retry retry) {
    this.retry = retry;
  }

  public int getScale() {
    return scale;
  }

  public void setScale(int scale) {
    this.scale = scale;
  }

  public RoundingMode getRoundingMode() {
    return roundingMode;
  }

  public void setRoundingMode(RoundingMode roundingMode) {
    this.roundingMode = roundingMode;
  }

  public String getApiKey() {
    return apiKey;
  }

  public void setApiKey(String apiKey) {
    this.apiKey = apiKey;
  }

  public String getProviderUrl() {
    return providerUrl;
  }

  public void setProviderUrl(String providerUrl) {
    this.providerUrl = providerUrl;
  }

  public String getBaseCurrency() {
    return baseCurrency;
  }

  public void setBaseCurrency(String baseCurrency) {
    this.baseCurrency = baseCurrency;
  }

  public String getCron() {
    return cron;
  }

  public void setCron(String cron) {
    this.cron = cron;
  }

  public static class Redis {

    private int timeToLive;
    private int timeToLiveCurrencyRates;

    public int getTimeToLive() {
      return timeToLive;
    }

    public void setTimeToLive(int timeToLive) {
      this.timeToLive = timeToLive;
    }

    public int getTimeToLiveCurrencyRates() {
      return timeToLiveCurrencyRates;
    }

    public void setTimeToLiveCurrencyRates(int timeToLiveCurrencyRates) {
      this.timeToLiveCurrencyRates = timeToLiveCurrencyRates;
    }
  }

  public static class Retry {
    private int maxAttempts;
    private long initialDelay;
    private double multiplier;
    private long maxDelay;

    public long getInitialDelay() {
      return initialDelay;
    }

    public void setInitialDelay(long initialDelay) {
      this.initialDelay = initialDelay;
    }

    public int getMaxAttempts() {
      return maxAttempts;
    }

    public void setMaxAttempts(int maxAttempts) {
      this.maxAttempts = maxAttempts;
    }

    public double getMultiplier() {
      return multiplier;
    }

    public void setMultiplier(double multiplier) {
      this.multiplier = multiplier;
    }

    public long getMaxDelay() {
      return maxDelay;
    }

    public void setMaxDelay(long maxDelay) {
      this.maxDelay = maxDelay;
    }
  }

  public static class RestTemplate {

    private int connectTimeout;
    private int readTimeout;

    public int getConnectTimeout() {
      return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
      this.connectTimeout = connectTimeout;
    }

    public int getReadTimeout() {
      return readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
      this.readTimeout = readTimeout;
    }
  }
}
