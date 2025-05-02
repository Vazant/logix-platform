package com.vazant.logix.currency.infrastructure.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CurrencyRatesResponse {

  @JsonProperty("date")
  private String date;

  @JsonProperty("base")
  private String base;

  @JsonProperty("rates")
  private Map<String, String> rates;
}
