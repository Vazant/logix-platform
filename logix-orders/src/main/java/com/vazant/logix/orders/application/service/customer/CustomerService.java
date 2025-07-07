package com.vazant.logix.orders.application.service.customer;

import com.vazant.logix.orders.application.service.common.CrudService;
import com.vazant.logix.orders.domain.customer.Customer;
import com.vazant.logix.orders.infrastructure.repository.customer.CustomerRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * Service for working with customers (Customer).
 * Contains business logic and delegates basic CRUD operations to CrudService.
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CustomerService {

  private final CrudService<Customer> crudService;
  private final CustomerRepository customerRepository;

  /**
   * Find customer by email (business-specific method).
   *
   * @param email the customer email
   * @return optional customer
   */
  public Optional<Customer> findByEmail(String email) {
    Assert.hasText(email, "Email must not be null or empty");
    return customerRepository.findByEmail(email);
  }

  /**
   * Find customers by city (business-specific method).
   *
   * @param city the city name
   * @return list of customers in the city
   */
  public List<Customer> findByCity(String city) {
    Assert.hasText(city, "City must not be null or empty");
    return customerRepository.findByCity(city);
  }

  /**
   * Get customer full name (business-specific method).
   *
   * @param customerUuid the customer UUID
   * @return the full name
   */
  public String getCustomerFullName(String customerUuid) {
    Customer customer = crudService.findByUuid(customerUuid);
    return customer.getFullName();
  }

  /**
   * Check if customer has valid email (business-specific validation).
   *
   * @param customerUuid the customer UUID
   * @return true if email is valid
   */
  public boolean hasValidEmail(String customerUuid) {
    Customer customer = crudService.findByUuid(customerUuid);
    return customer.getEmail() != null && customer.getEmail().contains("@");
  }

  // Delegate basic CRUD operations to the composed service
  public Customer findByUuid(String uuid) {
    return crudService.findByUuid(uuid);
  }

  public List<Customer> findAll() {
    return crudService.findAll();
  }

  public Customer create(Customer customer) {
    return crudService.create(customer);
  }

  public Customer update(String uuid, Customer customer) {
    return crudService.update(uuid, customer);
  }

  public void delete(String uuid) {
    crudService.delete(uuid);
  }

  public boolean exists(String uuid) {
    return crudService.exists(uuid);
  }

  public long count() {
    return crudService.count();
  }
}
