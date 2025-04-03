package com.vazant.logix.orders.common.exception;

import jakarta.validation.ConstraintViolationException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Object> handleValidation(MethodArgumentNotValidException ex) {
    var errors = new HashMap<String, String>();
    ex.getBindingResult()
        .getFieldErrors()
        .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
    return buildResponse(HttpStatus.BAD_REQUEST, "Validation failed", errors);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<Object> handleConstraint(ConstraintViolationException ex) {
    var errors = new HashMap<String, String>();
    ex.getConstraintViolations()
        .forEach(v -> errors.put(v.getPropertyPath().toString(), v.getMessage()));
    return buildResponse(HttpStatus.BAD_REQUEST, "Constraint violation", errors);
  }

  @ExceptionHandler(BindException.class)
  public ResponseEntity<Object> handleBind(BindException ex) {
    var errors = new HashMap<String, String>();
    ex.getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
    return buildResponse(HttpStatus.BAD_REQUEST, "Binding error", errors);
  }

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<Object> handleRuntime(RuntimeException ex) {
    return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), null);
  }

  private ResponseEntity<Object> buildResponse(HttpStatus status, String message, Object errors) {
    var body =
        Map.of(
            "timestamp", Instant.now(),
            "status", status.value(),
            "error", status.getReasonPhrase(),
            "message", message,
            "details", errors);
    return new ResponseEntity<>(body, status);
  }
}
