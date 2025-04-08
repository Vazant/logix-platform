package com.vazant.logix.orders.infrastructure.repository.organization;

import com.vazant.logix.orders.domain.organization.Organization;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationRepository extends JpaRepository<Organization, UUID> {

  Optional<Organization> findByName(String name);

  Optional<Organization> findByEmail(String email);
}
