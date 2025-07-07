package com.vazant.logix.orders.infrastructure.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UuidUtilsTest {

  @BeforeEach
  void setUp() {
    // Clear cache before each test
    UuidUtils.clearCache();
  }

  @Test
  void shouldParseValidUuid() {
    // Given
    String uuidString = "550e8400-e29b-41d4-a716-446655440000";

    // When
    UUID result = UuidUtils.parse(uuidString);

    // Then
    assertThat(result).isNotNull();
    assertThat(result.toString()).isEqualTo(uuidString.toLowerCase());
  }

  @Test
  void shouldParseValidUuidWithUpperCase() {
    // Given
    String uuidString = "550E8400-E29B-41D4-A716-446655440000";

    // When
    UUID result = UuidUtils.parse(uuidString);

    // Then
    assertThat(result).isNotNull();
    assertThat(result.toString()).isEqualTo(uuidString.toLowerCase());
  }

  @Test
  void shouldParseValidUuidWithMixedCase() {
    // Given
    String uuidString = "550e8400-E29b-41d4-A716-446655440000";

    // When
    UUID result = UuidUtils.parse(uuidString);

    // Then
    assertThat(result).isNotNull();
    assertThat(result.toString()).isEqualTo(uuidString.toLowerCase());
  }

  @Test
  void shouldParseValidUuidWithWhitespace() {
    // Given
    String uuidString = "  550e8400-e29b-41d4-a716-446655440000  ";

    // When
    UUID result = UuidUtils.parse(uuidString);

    // Then
    assertThat(result).isNotNull();
    assertThat(result.toString()).isEqualTo("550e8400-e29b-41d4-a716-446655440000");
  }

  @Test
  void shouldUseCacheForRepeatedParsing() {
    // Given
    String uuidString = "550e8400-e29b-41d4-a716-446655440000";

    // When
    UUID first = UuidUtils.parse(uuidString);
    UUID second = UuidUtils.parse(uuidString);

    // Then
    assertThat(first).isEqualTo(second);
    assertThat(UuidUtils.getCacheSize()).isEqualTo(1);
  }

  @Test
  void shouldThrowExceptionForNullUuid() {
    // When & Then
    assertThatThrownBy(() -> UuidUtils.parse(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("UUID string cannot be null or empty");
  }

  @Test
  void shouldThrowExceptionForEmptyUuid() {
    // When & Then
    assertThatThrownBy(() -> UuidUtils.parse(""))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("UUID string cannot be null or empty");
  }

  @Test
  void shouldThrowExceptionForWhitespaceOnlyUuid() {
    // When & Then
    assertThatThrownBy(() -> UuidUtils.parse("   "))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("UUID string cannot be null or empty");
  }

  @Test
  void shouldThrowExceptionForInvalidUuidFormats() {
    // Test various invalid UUID formats
    assertThatThrownBy(() -> UuidUtils.parse("invalid-uuid"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Invalid UUID format");
    
    assertThatThrownBy(() -> UuidUtils.parse("550e8400-e29b-41d4-a716-44665544000"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Invalid UUID format");
    
    assertThatThrownBy(() -> UuidUtils.parse("550e8400-e29b-41d4-a716-4466554400000"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Invalid UUID format");
    
    assertThatThrownBy(() -> UuidUtils.parse("550e8400-e29b-41d4-a716-44665544000g"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Invalid UUID format");
    
    assertThatThrownBy(() -> UuidUtils.parse("550e8400e29b41d4a716446655440000"))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Invalid UUID format");
  }

  @Test
  void shouldReturnNullForInvalidUuidWithParseOrNull() {
    // Given
    String invalidUuid = "invalid-uuid";

    // When
    UUID result = UuidUtils.parseOrNull(invalidUuid);

    // Then
    assertThat(result).isNull();
  }

  @Test
  void shouldReturnNullForNullUuidWithParseOrNull() {
    // When
    UUID result = UuidUtils.parseOrNull(null);

    // Then
    assertThat(result).isNull();
  }

  @Test
  void shouldReturnUuidForValidStringWithParseOrNull() {
    // Given
    String uuidString = "550e8400-e29b-41d4-a716-446655440000";

    // When
    UUID result = UuidUtils.parseOrNull(uuidString);

    // Then
    assertThat(result).isNotNull();
    assertThat(result.toString()).isEqualTo(uuidString.toLowerCase());
  }

  @Test
  void shouldValidateCorrectUuid() {
    // Given
    String validUuid = "550e8400-e29b-41d4-a716-446655440000";

    // When
    boolean result = UuidUtils.isValidUuid(validUuid);

    // Then
    assertThat(result).isTrue();
  }

  @Test
  void shouldValidateCorrectUuidWithUpperCase() {
    // Given
    String validUuid = "550E8400-E29B-41D4-A716-446655440000";

    // When
    boolean result = UuidUtils.isValidUuid(validUuid);

    // Then
    assertThat(result).isTrue();
  }

  @Test
  void shouldValidateCorrectUuidWithWhitespace() {
    // Given
    String validUuid = "  550e8400-e29b-41d4-a716-446655440000  ";

    // When
    boolean result = UuidUtils.isValidUuid(validUuid);

    // Then
    assertThat(result).isTrue();
  }

  @Test
  void shouldNotValidateIncorrectUuidFormats() {
    // Test various invalid UUID formats
    assertThat(UuidUtils.isValidUuid("invalid-uuid")).isFalse();
    assertThat(UuidUtils.isValidUuid("550e8400-e29b-41d4-a716-44665544000")).isFalse(); // too short
    assertThat(UuidUtils.isValidUuid("550e8400-e29b-41d4-a716-4466554400000")).isFalse(); // too long
    assertThat(UuidUtils.isValidUuid("550e8400-e29b-41d4-a716-44665544000g")).isFalse(); // invalid character
    assertThat(UuidUtils.isValidUuid("550e8400e29b41d4a716446655440000")).isFalse(); // missing dashes
    assertThat(UuidUtils.isValidUuid("")).isFalse(); // empty
    assertThat(UuidUtils.isValidUuid("   ")).isFalse(); // whitespace only
    assertThat(UuidUtils.isValidUuid(null)).isFalse(); // null
  }

  @Test
  void shouldGenerateRandomUuid() {
    // When
    UUID result = UuidUtils.generate();

    // Then
    assertThat(result).isNotNull();
    assertThat(UuidUtils.isValidUuid(result.toString())).isTrue();
  }

  @Test
  void shouldGenerateDeterministicUuidFromName() {
    // Given
    String name = "test-name";

    // When
    UUID result1 = UuidUtils.generateFromName(name);
    UUID result2 = UuidUtils.generateFromName(name);

    // Then
    assertThat(result1).isNotNull();
    assertThat(result1).isEqualTo(result2);
    assertThat(UuidUtils.isValidUuid(result1.toString())).isTrue();
  }

  @Test
  void shouldGenerateDifferentUuidsForDifferentNames() {
    // Given
    String name1 = "test-name-1";
    String name2 = "test-name-2";

    // When
    UUID result1 = UuidUtils.generateFromName(name1);
    UUID result2 = UuidUtils.generateFromName(name2);

    // Then
    assertThat(result1).isNotEqualTo(result2);
  }

  @Test
  void shouldThrowExceptionForNullName() {
    // When & Then
    assertThatThrownBy(() -> UuidUtils.generateFromName(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Name cannot be null or empty");
  }

  @Test
  void shouldThrowExceptionForEmptyName() {
    // When & Then
    assertThatThrownBy(() -> UuidUtils.generateFromName(""))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Name cannot be null or empty");
  }

  @Test
  void shouldConvertUuidToCompactString() {
    // Given
    UUID uuid = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");

    // When
    String result = UuidUtils.toCompactString(uuid);

    // Then
    assertThat(result).isEqualTo("550e8400e29b41d4a716446655440000");
    assertThat(result).hasSize(32);
  }

  @Test
  void shouldConvertCompactStringBackToUuid() {
    // Given
    String compactUuid = "550e8400e29b41d4a716446655440000";

    // When
    UUID result = UuidUtils.parseCompact(compactUuid);

    // Then
    assertThat(result).isNotNull();
    assertThat(result.toString()).isEqualTo("550e8400-e29b-41d4-a716-446655440000");
  }

  @Test
  void shouldThrowExceptionForInvalidCompactString() {
    // Given
    String invalidCompactUuid = "invalid";

    // When & Then
    assertThatThrownBy(() -> UuidUtils.parseCompact(invalidCompactUuid))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Compact UUID must be exactly 32 characters");
  }

  @Test
  void shouldThrowExceptionForNullCompactString() {
    // When & Then
    assertThatThrownBy(() -> UuidUtils.parseCompact(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Compact UUID must be exactly 32 characters");
  }

  @Test
  void shouldClearCache() {
    // Given
    String uuidString = "550e8400-e29b-41d4-a716-446655440000";
    UuidUtils.parse(uuidString);

    // When
    UuidUtils.clearCache();

    // Then
    assertThat(UuidUtils.getCacheSize()).isEqualTo(0);
  }

  @Test
  void shouldGetCacheSize() {
    // Given
    String uuid1 = "550e8400-e29b-41d4-a716-446655440000";
    String uuid2 = "550e8400-e29b-41d4-a716-446655440001";

    // When
    UuidUtils.parse(uuid1);
    UuidUtils.parse(uuid2);

    // Then
    assertThat(UuidUtils.getCacheSize()).isEqualTo(2);
  }

  @Test
  void shouldHandleCacheLimit() {
    // Given - generate more UUIDs than cache limit
    int cacheLimit = 1000;
    
    // When - parse many different UUIDs
    for (int i = 0; i < cacheLimit + 10; i++) {
      String uuidString = "550e8400-e29b-41d4-a716-" + String.format("%012d", i);
      UuidUtils.parse(uuidString);
    }

    // Then - cache should not exceed limit
    assertThat(UuidUtils.getCacheSize()).isLessThanOrEqualTo(cacheLimit);
  }
} 