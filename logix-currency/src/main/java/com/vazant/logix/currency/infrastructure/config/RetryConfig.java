package com.vazant.logix.currency.infrastructure.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

@Configuration
@EnableRetry
@EnableConfigurationProperties(CurrencyProperties.class)
public class RetryConfig {
  @Bean
  public RetryTemplate retryTemplate(CurrencyProperties properties) {
    RetryTemplate template = new RetryTemplate();

    ExponentialBackOffPolicy backOff = new ExponentialBackOffPolicy();
    backOff.setInitialInterval(properties.getRetry().getInitialDelay());
    backOff.setMultiplier(properties.getRetry().getMultiplier());
    backOff.setMaxInterval(properties.getRetry().getMaxDelay());

    template.setBackOffPolicy(backOff);
    template.setRetryPolicy(new SimpleRetryPolicy(properties.getRetry().getMaxAttempts()));

    return template;
  }
}
