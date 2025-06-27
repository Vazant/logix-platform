package com.vazant.logix.orders.application.service.common;

import com.vazant.logix.orders.domain.common.BaseEntity;
import com.vazant.logix.orders.domain.common.Updatable;
import java.util.List;

/**
 * Interface defining basic CRUD operations for entities.
 * This interface can be implemented by any service that needs CRUD functionality.
 *
 * @param <T> the entity type
 */
public interface CrudService<T extends BaseEntity & Updatable<T>> {

  /**
   * Find entity by UUID string.
   *
   * @param uuidStr the UUID string
   * @return the found entity
   * @throws IllegalArgumentException if entity not found
   */
  T findByUuid(String uuidStr);

  /**
   * Find all entities.
   *
   * @return list of all entities
   */
  List<T> findAll();

  /**
   * Create a new entity.
   *
   * @param entity the entity to create
   * @return the created entity
   */
  T create(T entity);

  /**
   * Update an existing entity.
   *
   * @param uuidStr the UUID string of the entity to update
   * @param updatedEntity the updated entity data
   * @return the updated entity
   */
  T update(String uuidStr, T updatedEntity);

  /**
   * Delete an entity by UUID string.
   *
   * @param uuidStr the UUID string of the entity to delete
   */
  void delete(String uuidStr);

  /**
   * Check if entity exists by UUID string.
   *
   * @param uuidStr the UUID string
   * @return true if entity exists, false otherwise
   */
  boolean exists(String uuidStr);

  /**
   * Count total number of entities.
   *
   * @return the total count
   */
  long count();
} 