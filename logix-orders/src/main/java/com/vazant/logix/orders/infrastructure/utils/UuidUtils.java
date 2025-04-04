package com.vazant.logix.orders.infrastructure.utils;

import java.util.Optional;
import java.util.UUID;

public class UuidUtils {
  private UuidUtils() {}

  public static UUID safeParse(String uuidString) {
    return Optional.of(UUID.fromString(uuidString))
        .orElseThrow(() -> new IllegalArgumentException("Invalid UUID format: " + uuidString));
  }
}
