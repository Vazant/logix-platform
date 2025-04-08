package com.vazant.logix.orders.infrastructure.config;

import gg.jte.TemplateEngine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JteConfig {

  @Bean
  public TemplateEngine jteTemplateEngine() {
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    return TemplateEngine.createPrecompiled(classLoader, "email.templates");
  }
}
