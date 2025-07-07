package com.vazant.logix.orders.application.service.common;

import com.vazant.logix.orders.domain.customer.Customer;
import com.vazant.logix.orders.infrastructure.repository.customer.CustomerRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AbstractCrudServiceTest {

  @Mock
  private CustomerRepository customerRepository;

  private AbstractCrudService<Customer> customerService;

  @BeforeEach
  void setUp() {
    customerService = new TestCustomerService(customerRepository);
  }

  @Test
  void shouldFindByUuid() {
    // Given
    UUID uuid = UUID.randomUUID();
    Customer customer = createTestCustomer("John", "Doe", "john@example.com");
    when(customerRepository.findById(uuid)).thenReturn(Optional.of(customer));

    // When
    Customer result = customerService.findByUuid(uuid.toString());

    // Then
    assertThat(result).isEqualTo(customer);
  }

  @Test
  void shouldThrowExceptionWhenEntityNotFound() {
    // Given
    UUID uuid = UUID.randomUUID();
    when(customerRepository.findById(uuid)).thenReturn(Optional.empty());

    // When & Then
    assertThatThrownBy(() -> customerService.findByUuid(uuid.toString()))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Customer not found with UUID");
  }

  @Test
  void shouldValidateUuidInput() {
    // When & Then
    assertThatThrownBy(() -> customerService.findByUuid(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("UUID string must not be null or empty");
        
    assertThatThrownBy(() -> customerService.findByUuid(""))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("UUID string must not be null or empty");
        
    assertThatThrownBy(() -> customerService.findByUuid("   "))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("UUID string must not be null or empty");
  }

  @Test
  void shouldFindAll() {
    // Given
    List<Customer> customers = List.of(
        createTestCustomer("John", "Doe", "john@example.com"),
        createTestCustomer("Jane", "Smith", "jane@example.com")
    );
    when(customerRepository.findAll()).thenReturn(customers);

    // When
    List<Customer> result = customerService.findAll();

    // Then
    assertThat(result).hasSize(2);
    assertThat(result).isEqualTo(customers);
  }

  @Test
  void shouldCreate() {
    // Given
    Customer customer = createTestCustomer("John", "Doe", "john@example.com");
    Customer savedCustomer = createTestCustomer("John", "Doe", "john@example.com");
    when(customerRepository.save(any(Customer.class))).thenReturn(savedCustomer);

    // When
    Customer result = customerService.create(customer);

    // Then
    assertThat(result).isEqualTo(savedCustomer);
    verify(customerRepository).save(customer);
  }

  @Test
  void shouldValidateCreateInput() {
    // When & Then
    assertThatThrownBy(() -> customerService.create(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Entity to create must not be null");
  }

  @Test
  void shouldUpdate() {
    // Given
    UUID uuid = UUID.randomUUID();
    Customer existingCustomer = createTestCustomer("John", "Doe", "john@example.com");
    Customer updatedCustomer = createTestCustomer("John", "Updated", "john@example.com");
    
    when(customerRepository.findById(uuid)).thenReturn(Optional.of(existingCustomer));
    when(customerRepository.save(any(Customer.class))).thenReturn(updatedCustomer);

    // When
    Customer result = customerService.update(uuid.toString(), updatedCustomer);

    // Then
    assertThat(result).isEqualTo(updatedCustomer);
    verify(customerRepository).save(existingCustomer);
  }

  @Test
  void shouldValidateUpdateInput() {
    // When & Then
    assertThatThrownBy(() -> customerService.update(null, createTestCustomer("John", "Doe", "john@example.com")))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("UUID string must not be null or empty");
        
    assertThatThrownBy(() -> customerService.update("", createTestCustomer("John", "Doe", "john@example.com")))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("UUID string must not be null or empty");
        
    assertThatThrownBy(() -> customerService.update(UUID.randomUUID().toString(), null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Entity to update must not be null");
  }

  @Test
  void shouldDelete() {
    // Given
    UUID uuid = UUID.randomUUID();
    when(customerRepository.existsById(uuid)).thenReturn(true);

    // When
    customerService.delete(uuid.toString());

    // Then
    verify(customerRepository).deleteById(uuid);
  }

  @Test
  void shouldThrowExceptionWhenDeleteNonExistentEntity() {
    // Given
    UUID uuid = UUID.randomUUID();
    when(customerRepository.existsById(uuid)).thenReturn(false);

    // When & Then
    assertThatThrownBy(() -> customerService.delete(uuid.toString()))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("Customer not found with UUID");
  }

  @Test
  void shouldValidateDeleteInput() {
    // When & Then
    assertThatThrownBy(() -> customerService.delete(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("UUID string must not be null or empty");
        
    assertThatThrownBy(() -> customerService.delete(""))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("UUID string must not be null or empty");
  }

  @Test
  void shouldCheckExists() {
    // Given
    UUID uuid = UUID.randomUUID();
    when(customerRepository.existsById(uuid)).thenReturn(true);

    // When
    boolean result = customerService.exists(uuid.toString());

    // Then
    assertThat(result).isTrue();
  }

  @Test
  void shouldValidateExistsInput() {
    // When & Then
    assertThatThrownBy(() -> customerService.exists(null))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("UUID string must not be null or empty");
        
    assertThatThrownBy(() -> customerService.exists(""))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("UUID string must not be null or empty");
  }

  @Test
  void shouldCount() {
    // Given
    when(customerRepository.count()).thenReturn(5L);

    // When
    long result = customerService.count();

    // Then
    assertThat(result).isEqualTo(5L);
  }

  private Customer createTestCustomer(String firstName, String lastName, String email) {
    return new Customer(firstName, lastName, email, "", "", "", "", "", "");
  }

  // Test implementation of AbstractCrudService
  private static class TestCustomerService extends AbstractCrudService<Customer> {
    
    public TestCustomerService(CustomerRepository repository) {
      super(repository, Customer.class);
    }
    
    @Override
    protected String getEntityName() {
      return "Customer";
    }
  }
} 