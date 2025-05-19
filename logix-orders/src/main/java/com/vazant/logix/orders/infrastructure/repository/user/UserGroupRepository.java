package com.vazant.logix.orders.infrastructure.repository.user;

import com.vazant.logix.orders.domain.user.UserGroup;
import com.vazant.logix.orders.domain.user.UserGroupName;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserGroupRepository extends JpaRepository<UserGroup, UUID> {

  Optional<UserGroup> findByGroupName(UserGroupName groupName);

  Optional<UserGroup> findByGroupNameAndOrganization(UserGroupName groupName, String organization);
}
