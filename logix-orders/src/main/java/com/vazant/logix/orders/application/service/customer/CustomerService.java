package com.vazant.logix.orders.application.service.customer;

import com.vazant.logix.orders.application.service.common.CrudService;
import com.vazant.logix.orders.domain.customer.Customer;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Customer service with business-specific logic.
 * Uses composition with CrudService instead of inheritance to avoid code duplication.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerService {

  private final CrudService<Customer> crudService;

  /**
   * Find customer by email (business-specific method).
   *
   * @param email the customer email
   * @return optional customer
   */
  public Optional<Customer> findByEmail(String email) {
    return crudService.findAll().stream()
        .filter(customer -> email.equals(customer.getEmail()))
        .findFirst();
  }

  /**
   * Find customers by city (business-specific method).
   *
   * @param city the city name
   * @return list of customers in the city
   */
  public List<Customer> findByCity(String city) {
    return crudService.findAll().stream()
        .filter(customer -> city.equals(customer.getCity()))
        .toList();
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
