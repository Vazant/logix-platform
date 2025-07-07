package com.vazant.logix.orders.infrastructure.repository.customer;

import com.vazant.logix.orders.domain.customer.Customer;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {
    
    /**
     * Find customer by email.
     *
     * @param email the customer email
     * @return optional customer
     */
    Optional<Customer> findByEmail(String email);
    
    /**
     * Find customers by city.
     *
     * @param city the city name
     * @return list of customers in the city
     */
    List<Customer> findByCity(String city);
}
