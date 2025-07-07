package com.vazant.logix.orders.presentation.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UuidPathVariableValidatorTest {

  private UuidPathVariableValidator validator;

  @BeforeEach
  void setUp() {
    validator = new UuidPathVariableValidator();
  }

  @Test
  void shouldValidateCorrectUuid() {
    // Given
    String validUuid = "550e8400-e29b-41d4-a716-446655440000";

    // When
    boolean result = validator.isValid(validUuid, null);

    // Then
    assertThat(result).isTrue();
  }

  @Test
  void shouldValidateCorrectUuidWithUpperCase() {
    // Given
    String validUuid = "550E8400-E29B-41D4-A716-446655440000";

    // When
    boolean result = validator.isValid(validUuid, null);

    // Then
    assertThat(result).isTrue();
  }

  @Test
  void shouldValidateCorrectUuidWithWhitespace() {
    // Given
    String validUuid = "  550e8400-e29b-41d4-a716-446655440000  ";

    // When
    boolean result = validator.isValid(validUuid, null);

    // Then
    assertThat(result).isTrue();
  }

  @Test
  void shouldNotValidateIncorrectUuidFormats() {
    // Test various invalid UUID formats
    assertThat(validator.isValid("invalid-uuid", null)).isFalse();
    assertThat(validator.isValid("550e8400-e29b-41d4-a716-44665544000", null)).isFalse(); // too short
    assertThat(validator.isValid("550e8400-e29b-41d4-a716-4466554400000", null)).isFalse(); // too long
    assertThat(validator.isValid("550e8400-e29b-41d4-a716-44665544000g", null)).isFalse(); // invalid character
    assertThat(validator.isValid("550e8400e29b41d4a716446655440000", null)).isFalse(); // missing dashes
    assertThat(validator.isValid("", null)).isFalse(); // empty
    assertThat(validator.isValid("   ", null)).isFalse(); // whitespace only
    assertThat(validator.isValid(null, null)).isFalse(); // null
  }
} 