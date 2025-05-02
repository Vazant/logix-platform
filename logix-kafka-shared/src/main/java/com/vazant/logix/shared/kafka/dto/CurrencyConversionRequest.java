package com.vazant.logix.shared.kafka.dto;

import java.math.BigDecimal;

public record CurrencyConversionRequest(String from, String to, BigDecimal amount) {}
