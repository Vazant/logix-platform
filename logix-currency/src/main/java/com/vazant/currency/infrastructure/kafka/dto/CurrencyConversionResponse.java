package com.vazant.currency.infrastructure.kafka.dto;

import java.math.BigDecimal;

public record CurrencyConversionResponse(String requestId, BigDecimal convertedAmount) {}
