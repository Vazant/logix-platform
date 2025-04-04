package com.vazant.logix.currency.config;

import java.time.Duration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableConfigurationProperties(CurrencyProperties.class)
public class RestTemplateConfig {
  @Bean
  public RestTemplate restTemplate(
      RestTemplateBuilder builder, CurrencyProperties currencyProperties) {
    return builder
        .setConnectTimeout(
            Duration.ofSeconds(currencyProperties.getRestTemplate().getConnectTimeout()))
        .setReadTimeout(Duration.ofSeconds(currencyProperties.getRestTemplate().getReadTimeout()))
        .build();
  }
}
