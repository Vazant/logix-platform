package com.vazant.logix.shared.kafka.dto;

import java.math.BigDecimal;

/**
 * Response DTO for a currency conversion operation.
 * <p>
 * Contains the result of the currency conversion.
 *
 * @param result the converted amount
 */
public record CurrencyConversionResponse(BigDecimal result) {}
