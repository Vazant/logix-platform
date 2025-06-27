package com.vazant.logix.orders.application.service.common;

import static com.vazant.logix.orders.infrastructure.utils.UuidUtils.safeParse;

import com.vazant.logix.orders.domain.common.BaseEntity;
import com.vazant.logix.orders.domain.common.Updatable;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

/**
 * Generic CRUD service that can be used for any entity without code duplication.
 * This service provides basic CRUD operations and can be extended for specific business logic.
 */
@Slf4j
@Service
public class GenericCrudService<T extends BaseEntity & Updatable<T>> implements CrudService<T> {

  private final JpaRepository<T, UUID> repository;
  private final Class<T> entityClass;

  public GenericCrudService(JpaRepository<T, UUID> repository, Class<T> entityClass) {
    this.repository = repository;
    this.entityClass = entityClass;
  }

  protected String getEntityName() {
    return entityClass.getSimpleName();
  }

  @Override
  public T findByUuid(String uuidStr) {
    UUID uuid = safeParse(uuidStr);
    return repository
        .findById(uuid)
        .orElseThrow(
            () -> new IllegalArgumentException(getEntityName() + " not found: " + uuidStr));
  }

  @Override
  public List<T> findAll() {
    return repository.findAll();
  }

  @Override
  @Transactional
  public T create(T entity) {
    T saved = repository.save(entity);
    log.info("✅ Created {}: {}", getEntityName(), saved.getUuid());
    return saved;
  }

  @Override
  @Transactional
  public T update(String uuidStr, T updatedEntity) {
    if (updatedEntity == null) {
      throw new IllegalArgumentException(getEntityName() + " to update must not be null");
    }
    T existing = findByUuid(uuidStr);
    existing.updateFrom(updatedEntity);
    existing.setUpdatedAt(LocalDateTime.now());
    T saved = repository.save(existing);
    log.info("♻️ Updated {}: {}", getEntityName(), saved.getUuid());
    return saved;
  }

  @Override
  @Transactional
  public void delete(String uuidStr) {
    UUID uuid = safeParse(uuidStr);
    if (!repository.existsById(uuid)) {
      throw new IllegalArgumentException(getEntityName() + " not found: " + uuidStr);
    }
    repository.deleteById(uuid);
    log.info("🗑️ Deleted {}: {}", getEntityName(), uuidStr);
  }

  @Override
  public boolean exists(String uuidStr) {
    UUID uuid = safeParse(uuidStr);
    return repository.existsById(uuid);
  }

  @Override
  public long count() {
    return repository.count();
  }
} 