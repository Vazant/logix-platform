package com.vazant.logix.orders.presentation.validation;

import com.vazant.logix.orders.infrastructure.utils.UuidUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator for UUID path variables.
 */
public class UuidPathVariableValidator implements ConstraintValidator<ValidUuid, String> {

  @Override
  public void initialize(ValidUuid constraintAnnotation) {
    // No initialization needed
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (value == null || value.trim().isEmpty()) {
      return false;
    }
    
    return UuidUtils.isValidUuid(value.trim());
  }
} 