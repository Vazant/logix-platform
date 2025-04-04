package com.vazant.logix.orders.application.service.common;

import static com.vazant.logix.orders.infrastructure.utils.UuidUtils.safeParse;

import com.vazant.logix.orders.domain.common.BaseEntity;
import com.vazant.logix.orders.domain.common.Updatable;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;

public abstract class AbstractCrudService<T extends BaseEntity & Updatable<T>> {

  protected final Logger log;
  private final Class<T> entityClass;

  protected AbstractCrudService(Class<T> entityClass) {
    this.entityClass = entityClass;
    this.log = LoggerFactory.getLogger(getClass());
  }

  protected abstract JpaRepository<T, UUID> getRepository();

  protected String getEntityName() {
    return entityClass.getSimpleName();
  }

  public T findByUuid(String uuidStr) {
    UUID uuid = safeParse(uuidStr);
    return getRepository()
        .findById(uuid)
        .orElseThrow(
            () -> new IllegalArgumentException(getEntityName() + " not found: " + uuidStr));
  }

  public List<T> findAll() {
    return getRepository().findAll();
  }

  @Transactional
  public T create(T entity) {
    T saved = getRepository().save(entity);
    log.info("✅ Created {}: {}", getEntityName(), saved.getUuid());
    return saved;
  }

  @Transactional
  public T update(String uuidStr, T updatedEntity) {
    if (updatedEntity == null) {
      throw new IllegalArgumentException(getEntityName() + " to update must not be null");
    }
    T existing = findByUuid(uuidStr);
    existing.updateFrom(updatedEntity);
    existing.setUpdatedAt(LocalDateTime.now());
    T saved = getRepository().save(existing);
    log.info("♻️ Updated {}: {}", getEntityName(), saved.getUuid());
    return saved;
  }

  @Transactional
  public void delete(String uuidStr) {
    UUID uuid = safeParse(uuidStr);
    if (!getRepository().existsById(uuid)) {
      throw new IllegalArgumentException(getEntityName() + " not found: " + uuidStr);
    }
    getRepository().deleteById(uuid);
    log.info("🗑️ Deleted {}: {}", getEntityName(), uuidStr);
  }
}
