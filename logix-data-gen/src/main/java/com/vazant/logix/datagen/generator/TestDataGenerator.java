package com.vazant.logix.datagen.generator;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TestDataGenerator {
  default String name() {
    return TestDataGenerator.class.getSimpleName();
  }

  default boolean shouldGenerate() {
    return getRepository().count() == 0;
  }

  void generate();

  JpaRepository<?, ?> getRepository();
}
