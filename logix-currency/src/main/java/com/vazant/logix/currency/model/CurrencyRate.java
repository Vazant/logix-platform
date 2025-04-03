package com.vazant.logix.currency.model;

import java.math.BigDecimal;

public record CurrencyRate(String currency, BigDecimal rate) {}
