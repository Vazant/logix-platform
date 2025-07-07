package com.vazant.logix.datagen.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for enabling or disabling the test data generator.
 * <p>
 * Maps the 'app.data-generator.enabled' property from the application configuration.
 *
 * @param enabled whether the data generator is enabled
 */
@ConfigurationProperties(prefix = "app.data-generator")
public record DataGeneratorProperties(boolean enabled) {}
