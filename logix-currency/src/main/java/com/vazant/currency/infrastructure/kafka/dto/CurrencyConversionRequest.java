package com.vazant.currency.infrastructure.kafka.dto;

import java.math.BigDecimal;

public record CurrencyConversionRequest(
    String requestId, String from, String to, BigDecimal amount) {}
