package com.vazant.logix.orders.presentation.exception;

import com.vazant.logix.orders.infrastructure.utils.MessageUtils;
import com.vazant.logix.orders.presentation.dto.ErrorResponse;
import com.vazant.logix.orders.presentation.dto.ValidationErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global exception handler for validation errors.
 * Provides consistent error responses for various validation failures.
 */
@Slf4j
@RestControllerAdvice
public class ValidationExceptionHandler {

  private final MessageUtils messageUtils;

  public ValidationExceptionHandler(MessageUtils messageUtils) {
    this.messageUtils = messageUtils;
  }

  /**
   * Handles validation errors from @Valid annotations.
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ValidationErrorResponse> handleValidationErrors(
      MethodArgumentNotValidException ex, HttpServletRequest request) {

    log.warn("Validation error: {}", ex.getMessage());

    List<ValidationErrorResponse.FieldError> fieldErrors = ex.getBindingResult().getFieldErrors()
        .stream()
        .map(this::createFieldError)
        .collect(Collectors.toList());

    ValidationErrorResponse response = ValidationErrorResponse.builder()
        .timestamp(LocalDateTime.now())
        .status(HttpStatus.BAD_REQUEST.value())
        .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
        .message(messageUtils.getErrorMessage("validation.failed"))
        .path(request.getRequestURI())
        .fieldErrors(fieldErrors)
        .build();

    return ResponseEntity.badRequest().body(response);
  }

  /**
   * Handles constraint violation exceptions.
   */
  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ValidationErrorResponse> handleConstraintViolation(
      ConstraintViolationException ex, HttpServletRequest request) {

    log.warn("Constraint violation: {}", ex.getMessage());

    List<ValidationErrorResponse.FieldError> fieldErrors = ex.getConstraintViolations()
        .stream()
        .map(this::createFieldError)
        .collect(Collectors.toList());

    ValidationErrorResponse response = ValidationErrorResponse.builder()
        .timestamp(LocalDateTime.now())
        .status(HttpStatus.BAD_REQUEST.value())
        .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
        .message(messageUtils.getErrorMessage("validation.constraint.violation"))
        .path(request.getRequestURI())
        .fieldErrors(fieldErrors)
        .build();

    return ResponseEntity.badRequest().body(response);
  }

  /**
   * Handles UUID validation errors in path variables.
   */
  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorResponse> handleIllegalArgument(
      IllegalArgumentException ex, HttpServletRequest request) {

    log.warn("Illegal argument: {}", ex.getMessage());

    ErrorResponse response = ErrorResponse.builder()
        .timestamp(LocalDateTime.now())
        .status(HttpStatus.BAD_REQUEST.value())
        .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
        .message(ex.getMessage())
        .path(request.getRequestURI())
        .build();

    return ResponseEntity.badRequest().body(response);
  }

  /**
   * Handles email delivery exceptions.
   */
  @ExceptionHandler(EmailDeliveryException.class)
  public ResponseEntity<ErrorResponse> handleEmailDeliveryError(
      EmailDeliveryException ex, HttpServletRequest request) {

    log.error("Email delivery error: {}", ex.getMessage(), ex);

    ErrorResponse response = ErrorResponse.builder()
        .timestamp(LocalDateTime.now())
        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
        .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
        .message(messageUtils.getErrorMessage("email.delivery.failed"))
        .path(request.getRequestURI())
        .build();

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
  }

  private ValidationErrorResponse.FieldError createFieldError(FieldError fieldError) {
    return ValidationErrorResponse.FieldError.builder()
        .field(fieldError.getField())
        .message(fieldError.getDefaultMessage())
        .rejectedValue(fieldError.getRejectedValue())
        .build();
  }

  private ValidationErrorResponse.FieldError createFieldError(ConstraintViolation<?> violation) {
    return ValidationErrorResponse.FieldError.builder()
        .field(violation.getPropertyPath().toString())
        .message(violation.getMessage())
        .rejectedValue(violation.getInvalidValue())
        .build();
  }
} 