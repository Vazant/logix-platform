package com.vazant.logix.orders.presentation.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;

/**
 * Standard error response DTO for API errors.
 */
@Data
@Builder
public class ErrorResponse {
  private LocalDateTime timestamp;
  private int status;
  private String error;
  private String message;
  private String path;
} 