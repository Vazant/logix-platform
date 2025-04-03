package com.vazant.logix.devtools.container;

import java.time.Duration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

@Configuration
@Profile("dev")
public class KafkaDev {

  @Bean
  @ServiceConnection
  public KafkaContainer kafkaContainer() {
    var kafkaContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:6.2.1"));
    var container = kafkaContainer.withStartupTimeout(Duration.ofMinutes(2)).withReuse(true);
    container.start();
    return container;
  }
}
