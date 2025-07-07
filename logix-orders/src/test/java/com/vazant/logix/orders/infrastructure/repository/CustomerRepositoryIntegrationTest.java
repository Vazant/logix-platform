package com.vazant.logix.orders.infrastructure.repository;

import com.vazant.logix.orders.domain.customer.Customer;
import com.vazant.logix.orders.infrastructure.repository.customer.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class CustomerRepositoryIntegrationTest {

  @Autowired
  private CustomerRepository customerRepository;

  @Autowired
  private TestEntityManager entityManager;

  private Customer customer1;
  private Customer customer2;
  private Customer customer3;

  @BeforeEach
  void setUp() {
    customerRepository.deleteAll();

    customer1 = new Customer("John", "Doe", "john@example.com", 
        "123 Main St", "New York", "NY", "10001", "USA", "+1234567890");
    customer2 = new Customer("Jane", "Smith", "jane@example.com", 
        "456 Oak St", "New York", "NY", "10002", "USA", "+1234567891");
    customer3 = new Customer("Bob", "Johnson", "bob@example.com", 
        "789 Pine St", "Los Angeles", "CA", "90210", "USA", "+1234567892");

    customerRepository.saveAll(List.of(customer1, customer2, customer3));
    entityManager.flush();
    entityManager.clear();
  }

  @Test
  void shouldFindByEmail() {
    // When
    Optional<Customer> found = customerRepository.findByEmail("john@example.com");

    // Then
    assertThat(found).isPresent();
    assertThat(found.get().getEmail()).isEqualTo("john@example.com");
    assertThat(found.get().getFirstName()).isEqualTo("John");
    assertThat(found.get().getLastName()).isEqualTo("Doe");
  }

  @Test
  void shouldReturnEmptyWhenEmailNotFound() {
    // When
    Optional<Customer> found = customerRepository.findByEmail("nonexistent@example.com");

    // Then
    assertThat(found).isEmpty();
  }

  @Test
  void shouldFindByCity() {
    // When
    List<Customer> found = customerRepository.findByCity("New York");

    // Then
    assertThat(found).hasSize(1);
    assertThat(found).allMatch(c -> "New York".equals(c.getCity()));
  }

  @Test
  void shouldReturnEmptyListWhenCityNotFound() {
    // When
    List<Customer> found = customerRepository.findByCity("Nonexistent City");

    // Then
    assertThat(found).isEmpty();
  }

  @Test
  void shouldFindAllCustomers() {
    // When
    List<Customer> allCustomers = customerRepository.findAll();

    // Then
    assertThat(allCustomers).hasSize(3);
    assertThat(allCustomers).extracting("email")
        .containsExactlyInAnyOrder("john@example.com", "jane@example.com", "bob@example.com");
  }

  @Test
  void shouldSaveCustomer() {
    // Given
    Customer newCustomer = new Customer("Alice", "Brown", "alice@example.com", 
        "321 Elm St", "Chicago", "IL", "60601", "USA", "+1234567893");

    // When
    Customer saved = customerRepository.save(newCustomer);

    // Then
    assertThat(saved).isNotNull();
    assertThat(saved.getUuid()).isNotNull();
    assertThat(saved.getEmail()).isEqualTo("alice@example.com");
    assertThat(saved.getFirstName()).isEqualTo("Alice");
    assertThat(saved.getLastName()).isEqualTo("Brown");
  }

  @Test
  void shouldUpdateCustomer() {
    // Given
    Customer customer = customerRepository.findByEmail("john@example.com").orElseThrow();
    customer.setFirstName("Jonathan");
    customer.setPhone("+9876543210");

    // When
    Customer updated = customerRepository.save(customer);

    // Then
    assertThat(updated.getFirstName()).isEqualTo("Jonathan");
    assertThat(updated.getPhone()).isEqualTo("+9876543210");
    
    // Verify in database
    Customer found = customerRepository.findByEmail("john@example.com").orElseThrow();
    assertThat(found.getFirstName()).isEqualTo("Jonathan");
    assertThat(found.getPhone()).isEqualTo("+9876543210");
  }

  @Test
  void shouldDeleteCustomer() {
    // Given
    Customer customer = customerRepository.findByEmail("john@example.com").orElseThrow();
    UUID customerUuid = customer.getUuid();

    // When
    customerRepository.deleteById(customerUuid);

    // Then
    assertThat(customerRepository.findById(customerUuid)).isEmpty();
    assertThat(customerRepository.findByEmail("john@example.com")).isEmpty();
  }

  @Test
  void shouldCountCustomers() {
    // When
    long count = customerRepository.count();

    // Then
    assertThat(count).isEqualTo(3);
  }

  @Test
  void shouldCheckIfCustomerExists() {
    // Given
    Customer customer = customerRepository.findByEmail("john@example.com").orElseThrow();

    // When
    boolean exists = customerRepository.existsById(customer.getUuid());

    // Then
    assertThat(exists).isTrue();
  }

  @Test
  void shouldCheckIfCustomerNotExists() {
    // When
    boolean exists = customerRepository.existsById(UUID.randomUUID());

    // Then
    assertThat(exists).isFalse();
  }

  @Test
  void shouldFindById() {
    // Given
    Customer customer = customerRepository.findByEmail("john@example.com").orElseThrow();

    // When
    Optional<Customer> found = customerRepository.findById(customer.getUuid());

    // Then
    assertThat(found).isPresent();
    assertThat(found.get().getEmail()).isEqualTo("john@example.com");
  }

  @Test
  void shouldReturnEmptyWhenIdNotFound() {
    // When
    Optional<Customer> found = customerRepository.findById(UUID.randomUUID());

    // Then
    assertThat(found).isEmpty();
  }

  @Test
  void shouldHandleCaseInsensitiveEmailSearch() {
    // When
    Optional<Customer> found = customerRepository.findByEmail("JOHN@EXAMPLE.COM");

    // Then
    assertThat(found).isPresent();
    assertThat(found.get().getEmail()).isEqualTo("john@example.com");
  }

  @Test
  void shouldHandleCaseInsensitiveCitySearch() {
    // When
    List<Customer> found = customerRepository.findByCity("new york");

    // Then
    assertThat(found).hasSize(2);
    assertThat(found).allMatch(c -> "New York".equals(c.getCity()));
  }
} 