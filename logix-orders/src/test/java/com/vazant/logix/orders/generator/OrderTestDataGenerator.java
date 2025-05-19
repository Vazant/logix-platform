package com.vazant.logix.orders.generator;

import com.vazant.logix.datagen.generator.TestDataGenerator;
import com.vazant.logix.orders.infrastructure.repository.order.OrderRepository;
import java.util.List;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class OrderTestDataGenerator implements TestDataGenerator {

  public static final List<String> CUSTOMER_IDS =
      List.of("cust-alice", "cust-bob", "cust-charlie", "cust-diana", "cust-evan");
  public static final List<String> WAREHOUSE_IDS =
      List.of("wh-north", "wh-south", "wh-east", "wh-west");
  public static final List<String> DESCRIPTIONS =
      List.of("Test delivery", "Demo order", "Sample product", "Initial batch", "Test request");
  private final OrderRepository orderRepository;

  public OrderTestDataGenerator(OrderRepository orderRepository) {
    this.orderRepository = orderRepository;
  }

  @Override
  public JpaRepository<?, ?> getRepository() {
    return orderRepository;
  }

  @Override
  public void generate() {
    //    IntStream.range(0, 10)
    //        .forEach(
    //            i -> {
    //              Order order =
    //                  OrderBuilder.order()
    //                      .customer(GeneratorUtils.getRandomElement(CUSTOMER_IDS))
    //                      .warehouseId(GeneratorUtils.getRandomElement(WAREHOUSE_IDS))
    //                      .amount(BigDecimal.valueOf(50.0 + Math.random() * 950.0))
    //                      .description(GeneratorUtils.getRandomElement(DESCRIPTIONS))
    //                      .build();
    //
    //              orderRepository.save(order);
    //            });
  }
}
