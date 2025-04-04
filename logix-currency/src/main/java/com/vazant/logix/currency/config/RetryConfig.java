package com.vazant.logix.currency.config;

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
  public RetryTemplate retryTemplate(CurrencyProperties currencyProperties) {
    RetryTemplate template = new RetryTemplate();

    var backOffPolicy = new ExponentialBackOffPolicy();
    backOffPolicy.setInitialInterval(currencyProperties.getRetry().getInitialDelay());
    backOffPolicy.setMultiplier(currencyProperties.getRetry().getMultiplier());
    backOffPolicy.setMaxInterval(currencyProperties.getRetry().getMaxDelay());
    template.setBackOffPolicy(backOffPolicy);

    var retryPolicy = new SimpleRetryPolicy(currencyProperties.getRetry().getMaxAttempts());
    template.setRetryPolicy(retryPolicy);

    return template;
  }
}
