package com.vazant.logix.orders;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

/**
 * Entry point for the Logix Orders microservice.
 * <p>
 * This class bootstraps the Spring Boot application for order management,
 * including configuration properties scanning and component scanning.
 */
@SpringBootApplication(scanBasePackages = "com.vazant.logix")
@ConfigurationPropertiesScan(basePackages = "com.vazant.logix")
public class OrderServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(OrderServiceApplication.class, args);
  }
}
