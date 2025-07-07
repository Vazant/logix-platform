package com.vazant.logix.orders.infrastructure.repository;

import com.vazant.logix.orders.domain.customer.Customer;
import com.vazant.logix.orders.domain.order.Order;
import com.vazant.logix.orders.domain.order.OrderStatus;
import com.vazant.logix.orders.domain.organization.Organization;
import com.vazant.logix.orders.domain.shared.Currency;
import com.vazant.logix.orders.domain.shared.Money;
import com.vazant.logix.orders.infrastructure.repository.customer.CustomerRepository;
import com.vazant.logix.orders.infrastructure.repository.order.OrderRepository;
import com.vazant.logix.orders.infrastructure.repository.organization.OrganizationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class OrderRepositoryIntegrationTest {

  @Autowired
  private OrderRepository orderRepository;

  @Autowired
  private CustomerRepository customerRepository;

  @Autowired
  private OrganizationRepository organizationRepository;

  @Autowired
  private TestEntityManager entityManager;

  private Customer customer1;
  private Customer customer2;
  private Organization organization1;
  private Organization organization2;
  private Order order1;
  private Order order2;
  private Order order3;

  @BeforeEach
  void setUp() {
    orderRepository.deleteAll();
    customerRepository.deleteAll();
    organizationRepository.deleteAll();

    // Create test data
    customer1 = new Customer("John", "Doe", "john@example.com", 
        "123 Main St", "New York", "NY", "10001", "USA", "+1234567890");
    customer2 = new Customer("Jane", "Smith", "jane@example.com", 
        "456 Oak St", "Los Angeles", "CA", "90210", "USA", "+1234567891");

    organization1 = new Organization("Test Org 1", "org1@example.com", "Address 1", "+1234567890");
    organization2 = new Organization("Test Org 2", "org2@example.com", "Address 2", "+1234567891");

    customerRepository.saveAll(List.of(customer1, customer2));
    organizationRepository.saveAll(List.of(organization1, organization2));

    // Create orders
    order1 = new Order(customer1, organization1, "warehouse-1", 
        new Money(BigDecimal.valueOf(100.00), Currency.USD), "Test order 1");
    order2 = new Order(customer1, organization1, "warehouse-2", 
        new Money(BigDecimal.valueOf(200.00), Currency.USD), "Test order 2");
    order3 = new Order(customer2, organization2, "warehouse-3", 
        new Money(BigDecimal.valueOf(300.00), Currency.USD), "Test order 3");

    orderRepository.saveAll(List.of(order1, order2, order3));
    entityManager.flush();
    entityManager.clear();
  }

  @Test
  void shouldFindByCustomerUuid() {
    // When
    List<Order> found = orderRepository.findByCustomerUuid(customer1.getUuid());

    // Then
    assertThat(found).hasSize(2);
    assertThat(found).allMatch(o -> o.getCustomer().getUuid().equals(customer1.getUuid()));
    assertThat(found).extracting("description").containsExactlyInAnyOrder("Test order 1", "Test order 2");
  }

  @Test
  void shouldReturnEmptyListWhenCustomerNotFound() {
    // When
    List<Order> found = orderRepository.findByCustomerUuid(UUID.randomUUID());

    // Then
    assertThat(found).isEmpty();
  }

  @Test
  void shouldFindByStatus() {
    // Given - change status of one order
    order1.setStatus(OrderStatus.PROCESSING);
    orderRepository.save(order1);

    // When
    List<Order> found = orderRepository.findByStatus(OrderStatus.PROCESSING);

    // Then
    assertThat(found).hasSize(1);
    assertThat(found.get(0).getStatus()).isEqualTo(OrderStatus.PROCESSING);
    assertThat(found.get(0).getDescription()).isEqualTo("Test order 1");
  }

  @Test
  void shouldFindByOrganizationUuid() {
    // When
    List<Order> found = orderRepository.findByOrganizationUuid(organization1.getUuid());

    // Then
    assertThat(found).hasSize(2);
    assertThat(found).allMatch(o -> o.getOrganization().getUuid().equals(organization1.getUuid()));
    assertThat(found).extracting("description").containsExactlyInAnyOrder("Test order 1", "Test order 2");
  }

  @Test
  void shouldReturnEmptyListWhenOrganizationNotFound() {
    // When
    List<Order> found = orderRepository.findByOrganizationUuid(UUID.randomUUID());

    // Then
    assertThat(found).isEmpty();
  }

  @Test
  void shouldFindAllOrders() {
    // When
    List<Order> allOrders = orderRepository.findAll();

    // Then
    assertThat(allOrders).hasSize(3);
    assertThat(allOrders).extracting("description")
        .containsExactlyInAnyOrder("Test order 1", "Test order 2", "Test order 3");
  }

  @Test
  void shouldSaveOrder() {
    // Given
    Order newOrder = new Order(customer1, organization1, "warehouse-4", 
        new Money(BigDecimal.valueOf(400.00), Currency.USD), "New test order");

    // When
    Order saved = orderRepository.save(newOrder);

    // Then
    assertThat(saved).isNotNull();
    assertThat(saved.getUuid()).isNotNull();
    assertThat(saved.getDescription()).isEqualTo("New test order");
    assertThat(saved.getCustomer().getUuid()).isEqualTo(customer1.getUuid());
    assertThat(saved.getOrganization().getUuid()).isEqualTo(organization1.getUuid());
    assertThat(saved.getStatus()).isEqualTo(OrderStatus.CREATED);
  }

  @Test
  void shouldUpdateOrder() {
    // Given
    Order order = orderRepository.findAll().get(0);
    order.setDescription("Updated description");
    order.setStatus(OrderStatus.SHIPPED);

    // When
    Order updated = orderRepository.save(order);

    // Then
    assertThat(updated.getDescription()).isEqualTo("Updated description");
    assertThat(updated.getStatus()).isEqualTo(OrderStatus.SHIPPED);
    
    // Verify in database
    Order found = orderRepository.findById(order.getUuid()).orElseThrow();
    assertThat(found.getDescription()).isEqualTo("Updated description");
    assertThat(found.getStatus()).isEqualTo(OrderStatus.SHIPPED);
  }

  @Test
  void shouldDeleteOrder() {
    // Given
    Order order = orderRepository.findAll().get(0);
    UUID orderUuid = order.getUuid();

    // When
    orderRepository.deleteById(orderUuid);

    // Then
    assertThat(orderRepository.findById(orderUuid)).isEmpty();
    assertThat(orderRepository.count()).isEqualTo(2);
  }

  @Test
  void shouldCountOrders() {
    // When
    long count = orderRepository.count();

    // Then
    assertThat(count).isEqualTo(3);
  }

  @Test
  void shouldCheckIfOrderExists() {
    // Given
    Order order = orderRepository.findAll().get(0);

    // When
    boolean exists = orderRepository.existsById(order.getUuid());

    // Then
    assertThat(exists).isTrue();
  }

  @Test
  void shouldCheckIfOrderNotExists() {
    // When
    boolean exists = orderRepository.existsById(UUID.randomUUID());

    // Then
    assertThat(exists).isFalse();
  }

  @Test
  void shouldFindById() {
    // Given
    Order order = orderRepository.findAll().get(0);

    // When
    Order found = orderRepository.findById(order.getUuid()).orElseThrow();

    // Then
    assertThat(found.getUuid()).isEqualTo(order.getUuid());
    assertThat(found.getDescription()).isEqualTo(order.getDescription());
  }

  @Test
  void shouldReturnEmptyWhenIdNotFound() {
    // When
    var found = orderRepository.findById(UUID.randomUUID());

    // Then
    assertThat(found).isEmpty();
  }

  @Test
  void shouldFindOrdersByMultipleStatuses() {
    // Given - set different statuses
    order1.setStatus(OrderStatus.PROCESSING);
    order2.setStatus(OrderStatus.SHIPPED);
    order3.setStatus(OrderStatus.DELIVERED);
    orderRepository.saveAll(List.of(order1, order2, order3));

    // When
    List<Order> processingOrders = orderRepository.findByStatus(OrderStatus.PROCESSING);
    List<Order> shippedOrders = orderRepository.findByStatus(OrderStatus.SHIPPED);
    List<Order> deliveredOrders = orderRepository.findByStatus(OrderStatus.DELIVERED);

    // Then
    assertThat(processingOrders).hasSize(1);
    assertThat(shippedOrders).hasSize(1);
    assertThat(deliveredOrders).hasSize(1);
  }

  @Test
  void shouldHandleOrderWithDifferentCurrencies() {
    // Given
    Order eurOrder = new Order(customer1, organization1, "warehouse-eur", 
        new Money(BigDecimal.valueOf(150.00), Currency.EUR), "EUR order");

    // When
    Order saved = orderRepository.save(eurOrder);

    // Then
    assertThat(saved.getTotal().getCurrency()).isEqualTo(Currency.EUR);
    assertThat(saved.getTotal().getAmount()).isEqualTo(BigDecimal.valueOf(150.00));
  }
} 