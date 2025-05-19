package com.vazant.logix.orders.infrastructure.repository.user;

import com.vazant.logix.orders.domain.user.Person;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonRepository extends JpaRepository<Person, UUID> {}
