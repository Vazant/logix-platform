package com.vazant.logix.orders.application.service.common;

import com.vazant.logix.orders.domain.common.BaseEntity;
import com.vazant.logix.orders.domain.common.Updatable;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

/**
 * Factory for creating CRUD services without code duplication.
 * This factory creates GenericCrudService instances for any entity type.
 */
@Component
public class CrudServiceFactory {

  /**
   * Creates a generic CRUD service for the given repository and entity class.
   *
   * @param repository the JPA repository
   * @param entityClass the entity class
   * @param <T> the entity type
   * @return a GenericCrudService instance
   */
  public <T extends BaseEntity & Updatable<T>> GenericCrudService<T> createService(
      JpaRepository<T, UUID> repository, Class<T> entityClass) {
    return new GenericCrudService<>(repository, entityClass);
  }
} 