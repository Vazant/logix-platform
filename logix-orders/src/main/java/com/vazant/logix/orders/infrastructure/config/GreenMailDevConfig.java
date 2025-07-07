package com.vazant.logix.orders.infrastructure.config;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import com.vazant.logix.shared.Constants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import com.vazant.logix.orders.infrastructure.config.MailProperties;

@Configuration
@Profile("dev")
@EnableConfigurationProperties(MailProperties.class)
public class GreenMailDevConfig {

  private final MailProperties mailProperties;

  public GreenMailDevConfig(MailProperties mailProperties) {
    this.mailProperties = mailProperties;
  }

  @Bean(initMethod = "start", destroyMethod = "stop")
  public GreenMail greenMail() {
    return new GreenMail(new ServerSetup(Constants.SMTP_PORT, Constants.LOCALHOST, Constants.SMTP_PROTOCOL));
  }
}
