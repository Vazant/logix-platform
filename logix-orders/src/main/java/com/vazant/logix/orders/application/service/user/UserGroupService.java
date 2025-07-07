package com.vazant.logix.orders.application.service.user;

import com.vazant.logix.orders.application.service.common.AbstractCrudService;
import com.vazant.logix.orders.domain.user.User;
import com.vazant.logix.orders.domain.user.UserGroup;
import com.vazant.logix.orders.domain.user.UserResponsibility;
import com.vazant.logix.orders.infrastructure.repository.user.UserGroupRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * Service for managing user groups.
 */
@Slf4j
@Service
@Transactional
public class UserGroupService extends AbstractCrudService<UserGroup> {

  private final UserGroupRepository userGroupRepository;

  public UserGroupService(UserGroupRepository userGroupRepository) {
    super(userGroupRepository, UserGroup.class);
    this.userGroupRepository = userGroupRepository;
  }

  @Override
  protected String getEntityName() {
    return "UserGroup";
  }

  @Transactional
  public UserGroup update(String groupUuid, UserGroup updatedGroup) {
    Assert.hasText(groupUuid, "Group UUID must not be null or empty");
    Assert.notNull(updatedGroup, "Updated group must not be null");
    
    UserGroup existingGroup = findByUuid(groupUuid);
    existingGroup.doUpdate(updatedGroup);
    return userGroupRepository.save(existingGroup);
  }

  @Transactional
  public UserGroup addUser(String groupUuid, User user) {
    Assert.hasText(groupUuid, "Group UUID must not be null or empty");
    Assert.notNull(user, "User must not be null");
    
    UserGroup group = findByUuid(groupUuid);
    group.addUser(user);
    return userGroupRepository.save(group);
  }

  @Transactional
  public UserGroup removeUser(String groupUuid, User user) {
    Assert.hasText(groupUuid, "Group UUID must not be null or empty");
    Assert.notNull(user, "User must not be null");
    
    UserGroup group = findByUuid(groupUuid);
    group.removeUser(user);
    return userGroupRepository.save(group);
  }

  @Transactional
  public UserGroup addResponsibility(String groupUuid, UserResponsibility responsibility) {
    Assert.hasText(groupUuid, "Group UUID must not be null or empty");
    Assert.notNull(responsibility, "Responsibility must not be null");
    
    UserGroup group = findByUuid(groupUuid);
    group.addResponsibility(responsibility);
    return userGroupRepository.save(group);
  }

  @Transactional
  public UserGroup removeResponsibility(String groupUuid, UserResponsibility responsibility) {
    UserGroup group = findByUuid(groupUuid);
    group.removeResponsibility(responsibility);
    return userGroupRepository.save(group);
  }
}
