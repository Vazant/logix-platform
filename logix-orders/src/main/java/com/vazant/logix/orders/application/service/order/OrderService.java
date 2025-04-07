package com.vazant.logix.orders.application.service.order;

import com.vazant.logix.currency.service.CurrencyService;
import com.vazant.logix.orders.application.service.common.AbstractCrudService;
import com.vazant.logix.orders.application.service.customer.CustomerService;
import com.vazant.logix.orders.application.service.product.ProductService;
import com.vazant.logix.orders.domain.order.Order;
import com.vazant.logix.orders.domain.order.OrderBuilder;
import com.vazant.logix.orders.domain.product.Product;
import com.vazant.logix.orders.domain.shared.Money;
import com.vazant.logix.orders.dto.order.OrderRequest;
import com.vazant.logix.orders.infrastructure.repository.order.OrderRepository;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class OrderService extends AbstractCrudService<Order> {
  private final OrderRepository repository;
  private final CustomerService customerService;
  private final ProductService productService;
  private final CurrencyService currencyService;

  public OrderService(
      OrderRepository repository,
      CustomerService customerService,
      ProductService productService,
      CurrencyService currencyService) {
    super(Order.class);
    this.repository = repository;
    this.customerService = customerService;
    this.productService = productService;
    this.currencyService = currencyService;
  }

  @Override
  protected JpaRepository<Order, UUID> getRepository() {
    return repository;
  }

  @Transactional
  public Order create(OrderRequest request) {
    validateRequest(request);

    var customer = customerService.findByUuid(request.customerUuid());
    var order =
        OrderBuilder.order()
            .customer(customer)
            .warehouseId(request.warehouseUuid())
            .total(request.total().toDomain())
            .description(request.description())
            .build();

    request
        .items()
        .forEach(
            itemRequest -> {
              Product product = productService.findByUuid(itemRequest.productUuid());
              order.addItem(product, itemRequest.quantity(), itemRequest.price().toDomain());
            });

    return super.create(order);
  }

  public BigDecimal calculateTotalIn(String orderUuid, String targetCurrency) {
    var order = findByUuid(orderUuid);
    Money total = order.getTotal();

    return currencyService.convert(total.getCurrency().name(), targetCurrency, total.getAmount());
  }

  private void validateRequest(OrderRequest request) {
    if (!StringUtils.hasText(request.customerUuid())) {
      throw new IllegalArgumentException("Customer ID must not be empty");
    }
    if (!StringUtils.hasText(request.warehouseUuid())) {
      throw new IllegalArgumentException("Warehouse ID must not be empty");
    }
    if (request.total() == null || request.total().getAmount().doubleValue() <= 0) {
      throw new IllegalArgumentException("Total amount must be positive");
    }
  }
}
