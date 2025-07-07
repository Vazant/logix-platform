package com.vazant.logix.shared;

/**
 * Shared application-wide constants for configuration, entity names, topics, and defaults.
 * <p>
 * Use these constants to avoid magic values and ensure consistency across modules.
 */
public class Constants {
    // Kafka topic names
    public static final String CURRENCY_REQUESTS = "currency.requests";
    public static final String CURRENCY_REPLIES = "currency.replies";
    // SMTP and server configuration
    public static final int SMTP_PORT = 3025;
    public static final String LOCALHOST = "localhost";
    public static final String SMTP_PROTOCOL = "smtp";
    // Currency codes
    public static final String CURRENCY_PLN = "PLN";
    public static final String CURRENCY_USD = "USD";
    // Entity table names
    public static final String ENTITY_ORDER = "orders";
    public static final String ENTITY_PRODUCT = "products";
    public static final String ENTITY_CATEGORY = "categories";
    public static final String ENTITY_CUSTOMER = "customers";
    public static final String ENTITY_ORGANIZATION = "organizations";
    public static final String ENTITY_ITEM = "items";
    public static final String ENTITY_PRODUCT_PRICE = "product_prices";
    // Kafka topic and group names
    public static final String KAFKA_TOPIC_CURRENCY_REQUESTS = "currency.requests";
    public static final String KAFKA_TOPIC_CURRENCY_REPLIES_PREFIX = "currency.replies.";
    public static final String KAFKA_GROUP_CURRENCY = "currency-group";
    // Cache names
    public static final String CACHE_CURRENCY_RATES = "currencyRates";
    // Retry and timeout defaults
    public static final int DEFAULT_RETRY_MAX_ATTEMPTS = 3;
    public static final int DEFAULT_RETRY_INITIAL_DELAY_MS = 1000;
    public static final double DEFAULT_RETRY_MULTIPLIER = 2.0;
    public static final int DEFAULT_RETRY_MAX_DELAY_MS = 5000;
    public static final int DEFAULT_REDIS_TTL_HOURS = 24;
    public static final int DEFAULT_REDIS_TTL_CURRENCY_RATES_HOURS = 12;
    public static final int DEFAULT_REST_CONNECT_TIMEOUT_SEC = 5;
    public static final int DEFAULT_REST_READ_TIMEOUT_SEC = 5;
    // Add other shared constants here as needed
} 