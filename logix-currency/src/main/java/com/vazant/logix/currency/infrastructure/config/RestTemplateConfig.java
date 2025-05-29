package com.vazant.logix.currency.infrastructure.config;

import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@EnableConfigurationProperties(CurrencyProperties.class)
@RequiredArgsConstructor
public class RestTemplateConfig {

  private final CurrencyProperties properties;

  @Bean
  public RestTemplate restTemplate(RestTemplateBuilder builder) {
    return builder
        .setConnectTimeout(Duration.ofSeconds(properties.getRest().getConnectTimeout()))
        .setReadTimeout(Duration.ofSeconds(properties.getRest().getReadTimeout()))
        .build();
  }
}
