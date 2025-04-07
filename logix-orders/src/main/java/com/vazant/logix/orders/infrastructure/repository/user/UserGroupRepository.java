package com.vazant.logix.orders.infrastructure.repository.user;

import com.vazant.logix.orders.domain.user.UserGroup;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserGroupRepository extends JpaRepository<UserGroup, UUID> {}
