package com.vazant.logix.orders.generator;

import com.vazant.logix.datagen.generator.TestDataGenerator;
import com.vazant.logix.orders.domain.customer.Customer;
import com.vazant.logix.orders.domain.customer.CustomerBuilder;
import com.vazant.logix.orders.domain.order.Item;
import com.vazant.logix.orders.domain.order.ItemBuilder;
import com.vazant.logix.orders.domain.order.Order;
import com.vazant.logix.orders.domain.order.OrderBuilder;
import com.vazant.logix.orders.domain.organization.Organization;
import com.vazant.logix.orders.domain.organization.OrganizationBuilder;
import com.vazant.logix.orders.domain.product.Product;
import com.vazant.logix.orders.domain.product.ProductBuilder;
import com.vazant.logix.orders.domain.shared.Currency;
import com.vazant.logix.orders.domain.shared.Money;
import com.vazant.logix.orders.infrastructure.repository.customer.CustomerRepository;
import com.vazant.logix.orders.infrastructure.repository.order.OrderRepository;
import com.vazant.logix.orders.infrastructure.repository.organization.OrganizationRepository;
import com.vazant.logix.orders.infrastructure.repository.product.ProductRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
@Slf4j
@RequiredArgsConstructor
public class OrderTestDataGenerator implements TestDataGenerator {

  private final OrderRepository orderRepository;
  private final CustomerRepository customerRepository;
  private final ProductRepository productRepository;
  private final OrganizationRepository organizationRepository;

  @Override
  public JpaRepository<?, ?> getRepository() {
    return orderRepository;
  }

  @Override
  public void generate() {
    if (!shouldGenerate()) {
      log.info("Orders already exist, skipping generation");
      return;
    }

    log.info("Generating test orders...");

    // Создаем тестовые данные
    Organization org = createTestOrganization();
    Customer customer = createTestCustomer();
    Product product = createTestProduct(org);

    // Генерируем заказы
    IntStream.range(0, 10)
        .forEach(i -> {
          Order order = OrderBuilder.order()
              .customer(customer)
              .organization(org)
              .warehouseId("wh-test-" + i)
              .total(new Money(BigDecimal.valueOf(50.0 + Math.random() * 950.0), Currency.USD))
              .description("Test order " + i)
              .build();

          // Добавляем товары
          Item item = ItemBuilder.item()
              .order(order)
              .product(product)
              .quantity(i + 1)
              .unitPrice(new Money(BigDecimal.valueOf(10.0 + Math.random() * 90.0), Currency.USD))
              .build();
          
          order.addItem(item);
          orderRepository.save(order);
        });

    log.info("Generated {} test orders", orderRepository.count());
  }

  private Organization createTestOrganization() {
    Organization org = OrganizationBuilder.organization()
        .name("Test Organization")
        .email("test@example.com")
        .address("Test Address")
        .phoneNumber("+1234567890")
        .build();
    return organizationRepository.save(org);
  }

  private Customer createTestCustomer() {
    Customer customer = CustomerBuilder.customer()
        .firstName("John")
        .lastName("Doe")
        .email("john@example.com")
        .address("123 Main St")
        .city("New York")
        .state("NY")
        .zipCode("10001")
        .country("USA")
        .phoneNumber("+1234567890")
        .build();
    return customerRepository.save(customer);
  }

  private Product createTestProduct(Organization org) {
    Product product = ProductBuilder.product()
        .name("Test Product")
        .prices(null)
        .description("Test product description")
        .skuCode("SKU-TEST-001")
        .dimensions(null)
        .stockQuantity(100)
        .categoryId(null)
        .imageId(null)
        .organization(org)
        .build();
    return productRepository.save(product);
  }
}
