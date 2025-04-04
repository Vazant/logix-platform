package com.vazant.logix.orders.application.service.customer;

import com.vazant.logix.orders.application.service.common.AbstractCrudService;
import com.vazant.logix.orders.domain.customer.Customer;
import com.vazant.logix.orders.infrastructure.repository.customer.CustomerRepository;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class CustomerService extends AbstractCrudService<Customer> {

  private final CustomerRepository customerRepository;

  public CustomerService(CustomerRepository customerRepository) {
    super(Customer.class);
    this.customerRepository = customerRepository;
  }

  @Override
  protected JpaRepository<Customer, UUID> getRepository() {
    return customerRepository;
  }
}
