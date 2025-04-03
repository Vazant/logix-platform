package com.vazant.logix.currency.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "logix.currency.retry")
public class RetryProperties {

  private int maxAttempts;
  private long initialDelay;
  private double multiplier;
  private long maxDelay;

  public int getMaxAttempts() {
    return maxAttempts;
  }

  public void setMaxAttempts(int maxAttempts) {
    this.maxAttempts = maxAttempts;
  }

  public long getInitialDelay() {
    return initialDelay;
  }

  public void setInitialDelay(long initialDelay) {
    this.initialDelay = initialDelay;
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
