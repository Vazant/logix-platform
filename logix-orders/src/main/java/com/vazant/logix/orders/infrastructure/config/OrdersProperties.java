package com.vazant.logix.orders.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "orders")
public class OrdersProperties {
    private DataGenerator dataGenerator = new DataGenerator();
    private Retry retry = new Retry();
    private Cache cache = new Cache();
    private Rest rest = new Rest();
    private String activationBaseUrl = "http://localhost:8080";

    @Getter
    @Setter
    public static class DataGenerator {
        private boolean enabled;
    }

    @Getter
    @Setter
    public static class Retry {
        private int maxAttempts = 3;
        private long initialDelay = 1000;
        private double multiplier = 2.0;
        private long maxDelay = 5000;
    }

    @Getter
    @Setter
    public static class Cache {
        private int ttlHours = 24;
        private int ttlCurrencyRatesHours = 12;
    }

    @Getter
    @Setter
    public static class Rest {
        private int connectTimeout = 5;
        private int readTimeout = 5;
    }

    public String getActivationBaseUrl() {
        return activationBaseUrl;
    }

    public void setActivationBaseUrl(String activationBaseUrl) {
        this.activationBaseUrl = activationBaseUrl;
    }
} 