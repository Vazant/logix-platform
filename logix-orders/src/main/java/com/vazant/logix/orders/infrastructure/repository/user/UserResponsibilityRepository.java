package com.vazant.logix.orders.infrastructure.repository.user;

import com.vazant.logix.orders.domain.user.SystemResponsibility;
import com.vazant.logix.orders.domain.user.UserResponsibility;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserResponsibilityRepository extends JpaRepository<UserResponsibility, UUID> {
  Optional<UserResponsibility> findByResponsibility(SystemResponsibility responsibility);
}
