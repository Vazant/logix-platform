package com.vazant.logix.orders.presentation.controller.customer;

import com.vazant.logix.orders.application.service.customer.CustomerService;
import com.vazant.logix.orders.domain.customer.Customer;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customers")
public class CustomerController {

  private final CustomerService customerService;

  public CustomerController(CustomerService customerService) {
    this.customerService = customerService;
  }

  @GetMapping
  public ResponseEntity<List<Customer>> getAllCustomers() {
    List<Customer> customers = customerService.findAll();
    return ResponseEntity.ok(customers);
  }

  @GetMapping("/{customerUuid}")
  public ResponseEntity<Customer> getCustomer(@PathVariable String customerUuid) {
    Customer customer = customerService.findByUuid(customerUuid);
    return ResponseEntity.ok(customer);
  }

  @PostMapping
  public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
    Customer created = customerService.create(customer);
    return ResponseEntity.ok(created);
  }

  @PutMapping("/{customerUuid}")
  public ResponseEntity<Customer> updateCustomer(
      @PathVariable String customerUuid, @RequestBody Customer customer) {
    Customer updated = customerService.update(customerUuid, customer);
    return ResponseEntity.ok(updated);
  }

  @DeleteMapping("/{customerUuid}")
  public ResponseEntity<Void> deleteCustomer(@PathVariable String customerUuid) {
    customerService.delete(customerUuid);
    return ResponseEntity.noContent().build();
  }
}
