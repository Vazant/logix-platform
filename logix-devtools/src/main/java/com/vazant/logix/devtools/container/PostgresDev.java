package com.vazant.logix.devtools.container;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import com.vazant.logix.devtools.config.DevContainerProperties;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@Configuration
@Profile("dev")
public class PostgresDev implements DisposableBean {

  private PostgreSQLContainer<?> container;

  @Bean
  @ServiceConnection
  @SuppressWarnings("resource")
  public PostgreSQLContainer<?> postgresContainer(DevContainerProperties props) {
    var postgresContainer = new PostgreSQLContainer<>(DockerImageName.parse("postgres:16.2-alpine"));
    this.container =
        postgresContainer
            .withDatabaseName(props.dbname())
            .withUsername(props.username())
            .withPassword(props.password())
            .withReuse(true)
            .withCreateContainerCmdModifier(
                cmd -> {
                  var hostConfig = cmd.getHostConfig();
                  if (hostConfig == null) {
                    hostConfig = new com.github.dockerjava.api.model.HostConfig();
                    cmd.withHostConfig(hostConfig);
                  }

                  hostConfig.withPortBindings(
                      new PortBinding(
                          Ports.Binding.bindPort(props.dbPort()), new ExposedPort(5432)));
                });
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
