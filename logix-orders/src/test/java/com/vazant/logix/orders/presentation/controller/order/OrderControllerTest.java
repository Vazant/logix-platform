package com.vazant.logix.orders.presentation.controller.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vazant.logix.orders.application.service.common.CrudService;
import com.vazant.logix.orders.application.service.order.OrderBusinessService;
import com.vazant.logix.orders.application.service.order.OrderService;
import com.vazant.logix.orders.domain.customer.Customer;
import com.vazant.logix.orders.domain.customer.CustomerBuilder;
import com.vazant.logix.orders.domain.order.Order;
import com.vazant.logix.orders.domain.order.OrderBuilder;
import com.vazant.logix.orders.domain.order.OrderStatus;
import com.vazant.logix.orders.domain.organization.Organization;
import com.vazant.logix.orders.domain.organization.OrganizationBuilder;
import com.vazant.logix.orders.domain.shared.Currency;
import com.vazant.logix.orders.domain.shared.Money;
import com.vazant.logix.orders.domain.shared.MoneyBuilder;
import com.vazant.logix.orders.dto.order.OrderItemRequest;
import com.vazant.logix.orders.dto.order.OrderRequest;
import com.vazant.logix.orders.dto.shared.MoneyRequest;
import com.vazant.logix.orders.infrastructure.repository.organization.OrganizationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for OrderController.
 * Tests CRUD operations and order-specific business logic.
 */
@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderService orderService;

    @MockBean
    private OrderBusinessService orderBusinessService;

    @MockBean
    private OrganizationRepository organizationRepository;

    private Order testOrder;
    private Organization testOrganization;
    private OrderRequest testOrderRequest;
    private String validUuid;
    private Customer testCustomer;

    @BeforeEach
    void setUp() {
        validUuid = UUID.randomUUID().toString();
        testOrganization = OrganizationBuilder.organization()
                .name("Test Organization")
                .email("test@organization.com")
                .address("Test organization address")
                .phoneNumber("+1234567890")
                .build();
        testCustomer = CustomerBuilder.customer()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .phone("+1234567890")
                .address("123 Main St")
                .city("New York")
                .state("NY")
                .zip("10001")
                .country("USA")
                .build();
        testOrder = OrderBuilder.order()
                .customer(testCustomer)
                .organization(testOrganization)
                .warehouseId("warehouse-123")
                .total(MoneyBuilder.money()
                        .amount(new java.math.BigDecimal("100.00"))
                        .currency(Currency.USD)
                        .build())
                .description("Test order")
                .build();
        // uuid, createdAt, updatedAt можно мокать через сервис, если нужно

        testOrderRequest = new OrderRequest(
                "customer-uuid",
                "warehouse-uuid",
                new MoneyRequest(new java.math.BigDecimal("100.00"), "USD"),
                "Test order",
                List.of(new OrderItemRequest(
                        "product-uuid",
                        2,
                        new MoneyRequest(new java.math.BigDecimal("50.00"), "USD")
                ))
        );
    }

    @Test
    void getAll_ShouldReturnAllOrders() throws Exception {
        // Given
        List<Order> orders = List.of(testOrder);
        when(orderService.findAll()).thenReturn(orders);

        // When & Then
        mockMvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].uuid").value(validUuid))
                .andExpect(jsonPath("$[0].status").value("PENDING"));

        verify(orderService).findAll();
    }

    @Test
    void getById_WithValidUuid_ShouldReturnOrder() throws Exception {
        // Given
        when(orderService.findByUuid(validUuid)).thenReturn(testOrder);

        // When & Then
        mockMvc.perform(get("/api/orders/{uuid}", validUuid))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.uuid").value(validUuid))
                .andExpect(jsonPath("$.status").value("PENDING"));

        verify(orderService).findByUuid(validUuid);
    }

    @Test
    void getById_WithInvalidUuid_ShouldReturnBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/orders/{uuid}", "invalid-uuid"))
                .andExpect(status().isBadRequest());

        verify(orderService, never()).findByUuid(anyString());
    }

    @Test
    void createOrder_WithValidRequest_ShouldReturnCreatedOrder() throws Exception {
        // Given
        when(organizationRepository.findAll()).thenReturn(List.of(testOrganization));
        when(orderBusinessService.createOrder(any(OrderRequest.class), any(Organization.class)))
                .thenReturn(testOrder);

        // When & Then
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testOrderRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.uuid").value(validUuid))
                .andExpect(jsonPath("$.status").value("PENDING"));

        verify(orderBusinessService).createOrder(any(OrderRequest.class), any(Organization.class));
    }

    @Test
    void createOrder_WithInvalidRequest_ShouldReturnBadRequest() throws Exception {
        // Given
        OrderRequest invalidRequest = new OrderRequest(
                "",
                "",
                new MoneyRequest(new java.math.BigDecimal("0.00"), "USD"),
                "",
                List.of()
        );

        // When & Then
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(orderBusinessService, never()).createOrder(any(OrderRequest.class), any(Organization.class));
    }

    @Test
    void updateStatus_WithValidData_ShouldReturnUpdatedOrder() throws Exception {
        // Given
        Order updatedOrder = OrderBuilder.order()
                .customer(testCustomer)
                .organization(testOrganization)
                .warehouseId("warehouse-123")
                .total(MoneyBuilder.money()
                        .amount(new java.math.BigDecimal("100.00"))
                        .currency(Currency.USD)
                        .build())
                .description("Test order")
                .build();
        updatedOrder.setStatus(OrderStatus.CONFIRMED);
        when(orderBusinessService.updateStatus(validUuid, OrderStatus.CONFIRMED)).thenReturn(updatedOrder);

        // When & Then
        mockMvc.perform(put("/api/orders/{uuid}/status", validUuid)
                        .param("status", "CONFIRMED"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.uuid").value(validUuid))
                .andExpect(jsonPath("$.status").value("CONFIRMED"));

        verify(orderBusinessService).updateStatus(validUuid, OrderStatus.CONFIRMED);
    }

    @Test
    void updateStatus_WithInvalidUuid_ShouldReturnBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(put("/api/orders/{uuid}/status", "invalid-uuid")
                        .param("status", "CONFIRMED"))
                .andExpect(status().isBadRequest());

        verify(orderBusinessService, never()).updateStatus(anyString(), any(OrderStatus.class));
    }

    @Test
    void findByCustomer_WithValidCustomerId_ShouldReturnOrders() throws Exception {
        // Given
        String customerId = UUID.randomUUID().toString();
        List<Order> orders = List.of(testOrder);
        when(orderBusinessService.findByCustomer(customerId)).thenReturn(orders);

        // When & Then
        mockMvc.perform(get("/api/orders/customer/{customerId}", customerId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].uuid").value(validUuid));

        verify(orderBusinessService).findByCustomer(customerId);
    }

    @Test
    void findByCustomer_WithInvalidCustomerId_ShouldReturnBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/orders/customer/{customerId}", "invalid-uuid"))
                .andExpect(status().isBadRequest());

        verify(orderBusinessService, never()).findByCustomer(anyString());
    }

    @Test
    void findByStatus_WithValidStatus_ShouldReturnOrders() throws Exception {
        // Given
        List<Order> orders = List.of(testOrder);
        when(orderBusinessService.findByStatus(OrderStatus.PENDING)).thenReturn(orders);

        // When & Then
        mockMvc.perform(get("/api/orders/status/{status}", "PENDING"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].uuid").value(validUuid))
                .andExpect(jsonPath("$[0].status").value("PENDING"));

        verify(orderBusinessService).findByStatus(OrderStatus.PENDING);
    }

    @Test
    void findByStatus_WithInvalidStatus_ShouldReturnBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/orders/status/{status}", "INVALID_STATUS"))
                .andExpect(status().isBadRequest());

        verify(orderBusinessService, never()).findByStatus(any(OrderStatus.class));
    }

    @Test
    void findByOrganization_WithValidOrganizationId_ShouldReturnOrders() throws Exception {
        // Given
        String organizationId = UUID.randomUUID().toString();
        List<Order> orders = List.of(testOrder);
        when(orderBusinessService.findByOrganization(organizationId)).thenReturn(orders);

        // When & Then
        mockMvc.perform(get("/api/orders/organization/{organizationId}", organizationId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].uuid").value(validUuid));

        verify(orderBusinessService).findByOrganization(organizationId);
    }

    @Test
    void findByOrganization_WithInvalidOrganizationId_ShouldReturnBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/orders/organization/{organizationId}", "invalid-uuid"))
                .andExpect(status().isBadRequest());

        verify(orderBusinessService, never()).findByOrganization(anyString());
    }

    @Test
    void createOrder_WhenNoOrganizationAvailable_ShouldReturnInternalServerError() throws Exception {
        // Given
        when(organizationRepository.findAll()).thenReturn(List.of());

        // When & Then
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testOrderRequest)))
                .andExpect(status().isInternalServerError());

        verify(orderBusinessService, never()).createOrder(any(OrderRequest.class), any(Organization.class));
    }

    @Test
    void updateOrder_WithValidData_ShouldReturnUpdatedOrder() throws Exception {
        // Given
        when(orderService.update(eq(validUuid), any(Order.class))).thenReturn(testOrder);

        // When & Then
        mockMvc.perform(put("/api/orders/{uuid}", validUuid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testOrder)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.uuid").value(validUuid));

        verify(orderService).update(eq(validUuid), any(Order.class));
    }

    @Test
    void deleteOrder_WithValidUuid_ShouldReturnNoContent() throws Exception {
        // Given
        doNothing().when(orderService).delete(validUuid);

        // When & Then
        mockMvc.perform(delete("/api/orders/{uuid}", validUuid))
                .andExpect(status().isNoContent());

        verify(orderService).delete(validUuid);
    }

    @Test
    void exists_WithValidUuid_ShouldReturnTrue() throws Exception {
        // Given
        when(orderService.exists(validUuid)).thenReturn(true);

        // When & Then
        mockMvc.perform(get("/api/orders/{uuid}/exists", validUuid))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(orderService).exists(validUuid);
    }

    @Test
    void count_ShouldReturnOrderCount() throws Exception {
        // Given
        when(orderService.count()).thenReturn(10L);

        // When & Then
        mockMvc.perform(get("/api/orders/count"))
                .andExpect(status().isOk())
                .andExpect(content().string("10"));

        verify(orderService).count();
    }
} 