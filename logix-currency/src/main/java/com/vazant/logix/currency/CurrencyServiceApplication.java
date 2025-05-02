package com.vazant.logix.currency;

import com.vazant.logix.currency.infrastructure.config.CurrencyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(CurrencyProperties.class)
@SpringBootApplication
public class CurrencyServiceApplication {
  public static void main(String[] args) {
    SpringApplication.run(CurrencyServiceApplication.class, args);
  }
}
