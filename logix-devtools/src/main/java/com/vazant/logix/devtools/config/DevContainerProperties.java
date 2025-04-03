package com.vazant.logix.devtools.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "test-container")
public record DevContainerProperties(
    String host, int dbPort, String dbname, String username, String password) {}
