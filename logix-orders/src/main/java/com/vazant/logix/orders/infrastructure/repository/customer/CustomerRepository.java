package com.vazant.logix.orders.infrastructure.repository.customer;

import com.vazant.logix.orders.domain.customer.Customer;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {}
