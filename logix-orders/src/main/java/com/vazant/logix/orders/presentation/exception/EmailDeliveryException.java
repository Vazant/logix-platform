package com.vazant.logix.orders.presentation.exception;

public class EmailDeliveryException extends RuntimeException {
  public EmailDeliveryException(String to, Throwable cause) {
    super("Failed to deliver email to " + to, cause);
  }
}
