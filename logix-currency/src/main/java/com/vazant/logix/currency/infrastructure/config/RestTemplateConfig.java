package com.vazant.logix.currency.infrastructure.config;

import java.time.Duration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Configuration for RestTemplate to work with external currency APIs.
 * <p>
 * Sets timeouts and parameters using CurrencyProperties.
 */
@Configuration
@EnableConfigurationProperties(CurrencyProperties.class)
public class RestTemplateConfig {

  private final CurrencyProperties properties;

  /**
   * Constructs a new RestTemplateConfig.
   *
   * @param properties currency service properties
   */
  public RestTemplateConfig(CurrencyProperties properties) {
    this.properties = properties;
  }

  /**
   * Configures a RestTemplate bean with custom timeouts for external API calls.
   *
   * @param builder the RestTemplateBuilder
   * @return the configured RestTemplate
   */
  @Bean
  public RestTemplate restTemplate(RestTemplateBuilder builder) {
    return builder
        .setConnectTimeout(Duration.ofSeconds(properties.getRest().getConnectTimeout()))
        .setReadTimeout(Duration.ofSeconds(properties.getRest().getReadTimeout()))
        .build();
  }
}
