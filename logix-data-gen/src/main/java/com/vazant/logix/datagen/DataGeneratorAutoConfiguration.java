package com.vazant.logix.datagen;

import com.vazant.logix.datagen.config.DataGeneratorProperties;
import com.vazant.logix.datagen.generator.TestDataGenerator;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * Auto-configuration for test data generation on application startup.
 * <p>
 * Registers an ApplicationRunner that runs all enabled TestDataGenerator beans.
 */
@AutoConfiguration
@EnableConfigurationProperties(DataGeneratorProperties.class)
public class DataGeneratorAutoConfiguration {

  private static final Logger log = LoggerFactory.getLogger(DataGeneratorAutoConfiguration.class);

  /**
   * ApplicationRunner bean that triggers test data generation if enabled.
   *
   * @param generators the list of TestDataGenerator beans
   * @return the ApplicationRunner
   */
  @Bean
  @ConditionalOnProperty(prefix = "app.data-generator", name = "enabled", havingValue = "true")
  public ApplicationRunner testDataGenerator(List<TestDataGenerator> generators) {
    return args -> {
      log.info("📦 Starting data generation...");
      Instant startAll = Instant.now();

      for (TestDataGenerator generator : generators) {
        if (generator.shouldGenerate()) {
          Instant start = Instant.now();
          generator.generate();
          log.info(
              "✅ Generated: {} ({} ms)",
              generator.name(),
              Duration.between(start, Instant.now()).toMillis());
        } else {
          log.info("⏭️ Skipped (already present): {}", generator.name());
        }
      }

      log.info(
          "✅ All test data generated in {} ms",
          Duration.between(startAll, Instant.now()).toMillis());
    };
  }
}
