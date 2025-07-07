package com.vazant.logix.datagen.generator;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interface for generating test data for the application.
 * <p>
 * Implementations should provide logic for generating and checking the presence of test data.
 */
public interface TestDataGenerator {
  /**
   * Returns the name of the data generator.
   *
   * @return the generator name
   */
  default String name() {
    return TestDataGenerator.class.getSimpleName();
  }

  /**
   * Determines whether data should be generated (e.g., if not already present).
   *
   * @return true if data should be generated, false otherwise
   */
  default boolean shouldGenerate() {
    return getRepository().count() == 0;
  }

  /**
   * Generates the test data.
   */
  void generate();

  /**
   * Returns the JPA repository associated with the data generator.
   *
   * @return the JPA repository
   */
  JpaRepository<?, ?> getRepository();
}
