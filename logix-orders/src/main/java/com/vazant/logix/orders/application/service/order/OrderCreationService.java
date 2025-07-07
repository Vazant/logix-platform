package com.vazant.logix.orders.application.service.order;

import com.vazant.logix.orders.domain.customer.Customer;
import com.vazant.logix.orders.domain.order.Item;
import com.vazant.logix.orders.domain.order.ItemBuilder;
import com.vazant.logix.orders.domain.order.Order;
import com.vazant.logix.orders.domain.order.OrderBuilder;
import com.vazant.logix.orders.domain.organization.Organization;
import com.vazant.logix.orders.domain.product.Product;
import com.vazant.logix.orders.dto.order.OrderRequest;
import com.vazant.logix.orders.infrastructure.repository.customer.CustomerRepository;
import com.vazant.logix.orders.infrastructure.repository.product.ProductRepository;
import com.vazant.logix.orders.infrastructure.utils.UuidUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * Service for creating orders (Order) with business logic for building orders and their items.
 * Uses customer and product repositories for validation and entity lookup.
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OrderCreationService {
    
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    /**
     * Creates a new order from request and organization.
     *
     * @param request the order request
     * @param organization the organization
     * @return the created order
     */
    public Order createOrder(OrderRequest request, Organization organization) {
        Assert.notNull(request, "Order request must not be null");
        Assert.notNull(organization, "Organization must not be null");
        Assert.hasText(request.customerUuid(), "Customer UUID must not be null or empty");
        
        log.info("Creating order for customer: {} in organization: {}", 
                request.customerUuid(), organization.getUuid());
        
        Customer customer = getCustomer(request.customerUuid());
        Order order = buildOrder(request, organization, customer);
        addItems(order, request);
        
        log.info("Successfully created order: {}", order.getUuid());
        return order;
    }

    private Customer getCustomer(String customerUuid) {
        Assert.hasText(customerUuid, "Customer UUID must not be null or empty");
        
        return customerRepository.findById(UuidUtils.parse(customerUuid))
                .orElseThrow(() -> new IllegalArgumentException("Customer not found with UUID: " + customerUuid));
    }

    private Order buildOrder(OrderRequest request, Organization organization, Customer customer) {
        return OrderBuilder.order()
                .customer(customer)
                .organization(organization)
                .warehouseId(request.warehouseUuid())
                .total(request.total().toDomain())
                .description(request.description())
                .build();
    }

    private void addItems(Order order, OrderRequest request) {
        for (var itemRequest : request.items()) {
            Product product = getProduct(itemRequest.productUuid());
            Item item = ItemBuilder.item()
                    .order(order)
                    .product(product)
                    .quantity(itemRequest.quantity())
                    .unitPrice(itemRequest.price().toDomain())
                    .build();
            order.addItem(item);
        }
    }

    private Product getProduct(String productUuid) {
        Assert.hasText(productUuid, "Product UUID must not be null or empty");
        
        return productRepository.findById(UuidUtils.parse(productUuid))
                .orElseThrow(() -> new IllegalArgumentException("Product not found with UUID: " + productUuid));
    }
} 