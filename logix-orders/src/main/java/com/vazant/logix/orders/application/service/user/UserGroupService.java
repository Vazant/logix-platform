package com.vazant.logix.orders.application.service.user;

import com.vazant.logix.orders.application.service.common.AbstractCrudService;
import com.vazant.logix.orders.domain.user.User;
import com.vazant.logix.orders.domain.user.UserGroup;
import com.vazant.logix.orders.domain.user.UserResponsibility;
import com.vazant.logix.orders.infrastructure.repository.user.UserGroupRepository;
import jakarta.transaction.Transactional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class UserGroupService extends AbstractCrudService<UserGroup> {

  private final UserGroupRepository userGroupRepository;

  public UserGroupService(UserGroupRepository userGroupRepository) {
    super(UserGroup.class);
    this.userGroupRepository = userGroupRepository;
  }

  @Override
  protected JpaRepository<UserGroup, UUID> getRepository() {
    return userGroupRepository;
  }

  @Transactional
  public UserGroup update(String groupUuid, UserGroup updatedGroup) {
    UserGroup existingGroup = findByUuid(groupUuid);
    existingGroup.doUpdate(updatedGroup);
    // Коллекции (users, responsibilities) не обновляются здесь — используйте бизнес-методы ниже.
    return userGroupRepository.save(existingGroup);
  }

  // Бизнес-метод для добавления пользователя в группу
  @Transactional
  public UserGroup addUser(String groupUuid, User user) {
    UserGroup group = findByUuid(groupUuid);
    group.addUser(user);
    return userGroupRepository.save(group);
  }

  // Бизнес-метод для удаления пользователя из группы
  @Transactional
  public UserGroup removeUser(String groupUuid, User user) {
    UserGroup group = findByUuid(groupUuid);
    group.removeUser(user);
    return userGroupRepository.save(group);
  }

  // Бизнес-метод для добавления обязанности в группу
  @Transactional
  public UserGroup addResponsibility(String groupUuid, UserResponsibility responsibility) {
    UserGroup group = findByUuid(groupUuid);
    group.addResponsibility(responsibility);
    return userGroupRepository.save(group);
  }

  // Бизнес-метод для удаления обязанности из группы
  @Transactional
  public UserGroup removeResponsibility(String groupUuid, UserResponsibility responsibility) {
    UserGroup group = findByUuid(groupUuid);
    group.removeResponsibility(responsibility);
    return userGroupRepository.save(group);
  }
}
