package com.vazant.logix.devtools.container;

import java.time.Duration;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.utility.DockerImageName;

@Configuration
@Profile("dev")
public class ElasticsearchDev implements DisposableBean {

  private ElasticsearchContainer container;

  @Bean
  @ServiceConnection
  @SuppressWarnings("resource")
  public ElasticsearchContainer elasticsearchContainer() {
    this.container =
        new ElasticsearchContainer(
            DockerImageName.parse("docker.elastic.co/elasticsearch/elasticsearch:7.17.10"))
            .withEnv("xpack.security.enabled", "false")
            .withEnv("discovery.type", "single-node")
            .withEnv("ES_JAVA_OPTS", "-Xms512m -Xmx512m")
            .withStartupTimeout(Duration.ofMinutes(2))
            .withReuse(true);
    container.start();
    return container;
  }

  @Override
  public void destroy() throws Exception {
    if (container != null) {
      container.stop();
    }
  }
}
