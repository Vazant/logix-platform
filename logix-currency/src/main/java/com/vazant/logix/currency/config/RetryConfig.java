package com.vazant.logix.currency.config;

import java.util.Map;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

@Configuration
@EnableRetry
@EnableConfigurationProperties(RetryProperties.class)
public class RetryConfig {

  @Bean
  public RetryTemplate retryTemplate(RetryProperties retryProperties) {
    RetryTemplate template = new RetryTemplate();

    ExponentialBackOffPolicy backOff = new ExponentialBackOffPolicy();
    backOff.setInitialInterval(retryProperties.getInitialDelay());
    backOff.setMultiplier(retryProperties.getMultiplier());
    backOff.setMaxInterval(retryProperties.getMaxDelay());
    template.setBackOffPolicy(backOff);

    SimpleRetryPolicy retryPolicy =
        new SimpleRetryPolicy(
            retryProperties.getMaxAttempts(), Map.of(RuntimeException.class, true));

    template.setRetryPolicy(retryPolicy);

    return template;
  }
}
