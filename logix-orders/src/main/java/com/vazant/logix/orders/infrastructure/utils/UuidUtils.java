package com.vazant.logix.orders.infrastructure.utils;

import java.util.UUID;

public class UuidUtils {
  private UuidUtils() {}

  /**
   * Safely parses a UUID string, throwing a descriptive exception if invalid.
   *
   * @param uuidString the UUID string to parse
   * @return the parsed UUID
   * @throws IllegalArgumentException if the string is not a valid UUID
   */
  public static UUID safeParse(String uuidString) {
    try {
      return UUID.fromString(uuidString);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException("Invalid UUID format: " + uuidString, e);
    }
  }

  /**
   * Checks if a string is a valid UUID format.
   *
   * @param uuidString the string to check
   * @return true if the string is a valid UUID format
   */
  public static boolean isValidUuid(String uuidString) {
    try {
      UUID.fromString(uuidString);
      return true;
    } catch (IllegalArgumentException e) {
      return false;
    }
  }
}
