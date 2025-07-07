package com.vazant.logix.orders.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import com.vazant.logix.orders.infrastructure.config.DatabaseProperties;

@Configuration
@EnableJpaAuditing
@EnableConfigurationProperties(DatabaseProperties.class)
public class JpaConfig {

  private final DatabaseProperties databaseProperties;

  public JpaConfig(DatabaseProperties databaseProperties) {
    this.databaseProperties = databaseProperties;
  }

  // Пример использования databaseProperties.getUrl(), getUsername(), getPassword() при необходимости
}
