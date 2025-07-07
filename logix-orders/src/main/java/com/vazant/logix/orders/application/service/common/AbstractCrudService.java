package com.vazant.logix.orders.application.service.common;

import com.vazant.logix.orders.domain.common.BaseEntity;
import com.vazant.logix.orders.domain.common.Updatable;
import com.vazant.logix.orders.infrastructure.utils.UuidUtils;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * Abstract CRUD service implementation.
 * <p>
 * Provides common CRUD operations for any entity extending BaseEntity and implementing Updatable.
 * Subclasses must specify the entity type and repository.
 *
 * @param <T> the entity type
 */
@Slf4j
@RequiredArgsConstructor
public abstract class AbstractCrudService<T extends BaseEntity & Updatable<T>> implements CrudService<T> {

  private final JpaRepository<T, UUID> repository;
  private final Class<T> entityClass;

  /**
   * Returns the simple name of the entity class.
   *
   * @return the entity name
   */
  protected String getEntityName() {
    return entityClass.getSimpleName();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public T findByUuid(String uuidStr) {
    Assert.hasText(uuidStr, "UUID string must not be null or empty");
    
    UUID uuid = UuidUtils.parse(uuidStr);
    return repository
        .findById(uuid)
        .orElseThrow(() -> new IllegalArgumentException(
            String.format("%s not found with UUID: %s", getEntityName(), uuidStr)));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<T> findAll() {
    return repository.findAll();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Transactional
  public T create(T entity) {
    Assert.notNull(entity, "Entity to create must not be null");
    
    T saved = repository.save(entity);
    log.info("Created {} with UUID: {}", getEntityName(), saved.getUuid());
    return saved;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Transactional
  public T update(String uuidStr, T updatedEntity) {
    Assert.hasText(uuidStr, "UUID string must not be null or empty");
    Assert.notNull(updatedEntity, "Entity to update must not be null");
    
    T existing = findByUuid(uuidStr);
    existing.updateFrom(updatedEntity);
    existing.setUpdatedAt(LocalDateTime.now());
    T saved = repository.save(existing);
    log.info("Updated {} with UUID: {}", getEntityName(), saved.getUuid());
    return saved;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Transactional
  public void delete(String uuidStr) {
    Assert.hasText(uuidStr, "UUID string must not be null or empty");
    
    UUID uuid = UuidUtils.parse(uuidStr);
    if (!repository.existsById(uuid)) {
      throw new IllegalArgumentException(
          String.format("%s not found with UUID: %s", getEntityName(), uuidStr));
    }
    repository.deleteById(uuid);
    log.info("Deleted {} with UUID: {}", getEntityName(), uuidStr);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean exists(String uuidStr) {
    Assert.hasText(uuidStr, "UUID string must not be null or empty");
    
    UUID uuid = UuidUtils.parse(uuidStr);
    return repository.existsById(uuid);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public long count() {
    return repository.count();
  }
}
