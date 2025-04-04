package com.vazant.logix.currency.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

public record CurrencyRate(String currency, BigDecimal rate, String baseCurrency, Instant fetchedAt)
    implements Serializable {}
