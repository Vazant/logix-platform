package com.vazant.logix.orders.presentation.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Data;

/**
 * Validation error response DTO for validation failures.
 */
@Data
@Builder
public class ValidationErrorResponse {
  private LocalDateTime timestamp;
  private int status;
  private String error;
  private String message;
  private String path;
  private List<FieldError> fieldErrors;

  /**
   * Individual field error information.
   */
  @Data
  @Builder
  public static class FieldError {
    private String field;
    private String message;
    private Object rejectedValue;
  }
} 