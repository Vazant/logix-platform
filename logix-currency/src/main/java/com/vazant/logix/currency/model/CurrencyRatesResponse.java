package com.vazant.logix.currency.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

public record CurrencyRatesResponse(
    @JsonProperty("date") String date,
    @JsonProperty("base") String base,
    @JsonProperty("rates") Map<String, String> rates) {}
