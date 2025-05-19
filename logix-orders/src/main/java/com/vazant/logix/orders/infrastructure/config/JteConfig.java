package com.vazant.logix.orders.infrastructure.config;

import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.resolve.ResourceCodeResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JteConfig {

  @Bean
  public TemplateEngine templateEngine() {
    return TemplateEngine.create(new ResourceCodeResolver("email/templates"), ContentType.Html);
  }
}
