package com.vazant.logix.orders.infrastructure.repository.user;

import com.vazant.logix.orders.domain.user.SystemResponsibility;
import com.vazant.logix.orders.domain.user.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, UUID> {
  Optional<User> findByUsername(String username);
  
  List<User> findByOrganizationUuid(UUID organizationUuid);
  
  @Query("SELECT u FROM User u JOIN u.groups g JOIN g.responsibilities r WHERE r.name = :responsibility")
  List<User> findByResponsibility(@Param("responsibility") SystemResponsibility responsibility);
  
  @Query("SELECT COUNT(u) > 0 FROM User u JOIN u.groups g JOIN g.responsibilities r WHERE u.uuid = :userId AND r.name = :responsibility")
  boolean hasResponsibility(@Param("userId") UUID userId, @Param("responsibility") SystemResponsibility responsibility);
}
