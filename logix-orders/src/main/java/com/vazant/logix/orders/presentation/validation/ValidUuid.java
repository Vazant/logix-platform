package com.vazant.logix.orders.presentation.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Custom validation annotation for UUID strings.
 */
@Documented
@Constraint(validatedBy = UuidPathVariableValidator.class)
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidUuid {
  String message() default "Invalid UUID format";
  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};
} 