package com.vazant.logix.orders.presentation.controller.customer;

import com.vazant.logix.orders.application.service.common.CrudService;
import com.vazant.logix.orders.domain.customer.Customer;
import com.vazant.logix.orders.presentation.controller.common.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for customer management with validation.
 * Extends BaseController to inherit common CRUD operations and validation.
 */
@RestController
@RequestMapping("/customers")
public class CustomerController extends BaseController<Customer> {

  public CustomerController(CrudService<Customer> customerService) {
    super(customerService);
  }
  
  // Additional customer-specific endpoints can be added here
  // All common CRUD operations are inherited from BaseController
}
