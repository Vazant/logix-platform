package com.vazant.logix.orders.infrastructure.config;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
public class GreenMailDevConfig {

  @Bean(initMethod = "start", destroyMethod = "stop")
  public GreenMail greenMail() {
    return new GreenMail(new ServerSetup(3025, "localhost", "smtp"));
  }
}
