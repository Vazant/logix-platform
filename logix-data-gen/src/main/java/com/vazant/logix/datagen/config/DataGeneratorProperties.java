package com.vazant.logix.datagen.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.data-generator")
public record DataGeneratorProperties(boolean enabled) {}
