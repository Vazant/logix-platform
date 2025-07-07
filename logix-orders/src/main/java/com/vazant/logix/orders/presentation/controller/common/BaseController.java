package com.vazant.logix.orders.presentation.controller.common;

import com.vazant.logix.orders.application.service.common.CrudService;
import com.vazant.logix.orders.domain.common.BaseEntity;
import com.vazant.logix.orders.domain.common.Updatable;
import com.vazant.logix.orders.presentation.validation.ValidUuid;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

/**
 * Base controller with common CRUD operations and validation.
 * Provides standard endpoints for any entity that extends BaseEntity and implements Updatable.
 *
 * @param <T> the entity type
 */
@RequiredArgsConstructor
public abstract class BaseController<T extends BaseEntity & Updatable<T>> {

  protected final CrudService<T> crudService;

  /**
   * Get all entities.
   *
   * @return list of all entities
   */
  @GetMapping
  public ResponseEntity<List<T>> getAll() {
    List<T> entities = crudService.findAll();
    return ResponseEntity.ok(entities);
  }

  /**
   * Get entity by UUID.
   *
   * @param uuid the entity UUID
   * @return the found entity
   */
  @GetMapping("/{uuid}")
  public ResponseEntity<T> getById(@PathVariable @ValidUuid String uuid) {
    Assert.hasText(uuid, "UUID must not be null or empty");
    T entity = crudService.findByUuid(uuid);
    return ResponseEntity.ok(entity);
  }

  /**
   * Create new entity.
   *
   * @param entity the entity to create
   * @return the created entity
   */
  @PostMapping
  public ResponseEntity<T> create(@Valid @RequestBody T entity) {
    Assert.notNull(entity, "Entity must not be null");
    T created = crudService.create(entity);
    return ResponseEntity.ok(created);
  }

  /**
   * Update existing entity.
   *
   * @param uuid the entity UUID
   * @param entity the updated entity data
   * @return the updated entity
   */
  @PutMapping("/{uuid}")
  public ResponseEntity<T> update(@PathVariable @ValidUuid String uuid, @Valid @RequestBody T entity) {
    Assert.hasText(uuid, "UUID must not be null or empty");
    Assert.notNull(entity, "Entity must not be null");
    T updated = crudService.update(uuid, entity);
    return ResponseEntity.ok(updated);
  }

  /**
   * Delete entity.
   *
   * @param uuid the entity UUID
   * @return no content response
   */
  @DeleteMapping("/{uuid}")
  public ResponseEntity<Void> delete(@PathVariable @ValidUuid String uuid) {
    Assert.hasText(uuid, "UUID must not be null or empty");
    crudService.delete(uuid);
    return ResponseEntity.noContent().build();
  }

  /**
   * Check if entity exists.
   *
   * @param uuid the entity UUID
   * @return true if entity exists, false otherwise
   */
  @GetMapping("/{uuid}/exists")
  public ResponseEntity<Boolean> exists(@PathVariable @ValidUuid String uuid) {
    Assert.hasText(uuid, "UUID must not be null or empty");
    boolean exists = crudService.exists(uuid);
    return ResponseEntity.ok(exists);
  }

  /**
   * Get entity count.
   *
   * @return the total count of entities
   */
  @GetMapping("/count")
  public ResponseEntity<Long> count() {
    long count = crudService.count();
    return ResponseEntity.ok(count);
  }
} 