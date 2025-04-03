package com.vazant.logix.orders;

import com.vazant.logix.datagen.DataGeneratorAutoConfiguration;
import com.vazant.logix.devtools.config.DevContainerProperties;
import com.vazant.logix.devtools.container.ElasticsearchDev;
import com.vazant.logix.devtools.container.KafkaDev;
import com.vazant.logix.devtools.container.PostgresDev;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties({DevContainerProperties.class})
public class DevelopmentApplication {

  public static void main(String[] args) {
    System.setProperty("spring.profiles.active", "dev");

    SpringApplication.from(OrderServiceApplication::main)
        .with(
            KafkaDev.class,
            PostgresDev.class,
            ElasticsearchDev.class,
            DataGeneratorAutoConfiguration.class)
        .run(args);
  }
}
