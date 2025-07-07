package com.vazant.logix.orders.presentation.controller.customer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vazant.logix.orders.application.service.common.CrudService;
import com.vazant.logix.orders.application.service.customer.CustomerService;
import com.vazant.logix.orders.domain.customer.Customer;
import com.vazant.logix.orders.domain.customer.CustomerBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for CustomerController.
 * Tests CRUD operations inherited from BaseController.
 */
@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CrudService<Customer> customerService;

    private Customer testCustomer;
    private String validUuid;

    @BeforeEach
    void setUp() {
        validUuid = UUID.randomUUID().toString();
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
        // uuid, createdAt, updatedAt можно мокать через сервис, если нужно
    }

    @Test
    void getAll_ShouldReturnAllCustomers() throws Exception {
        // Given
        List<Customer> customers = List.of(testCustomer);
        when(customerService.findAll()).thenReturn(customers);

        // When & Then
        mockMvc.perform(get("/customers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].uuid").value(validUuid))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Doe"))
                .andExpect(jsonPath("$[0].email").value("john.doe@example.com"));

        verify(customerService).findAll();
    }

    @Test
    void getById_WithValidUuid_ShouldReturnCustomer() throws Exception {
        // Given
        when(customerService.findByUuid(validUuid)).thenReturn(testCustomer);

        // When & Then
        mockMvc.perform(get("/customers/{uuid}", validUuid))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.uuid").value(validUuid))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));

        verify(customerService).findByUuid(validUuid);
    }

    @Test
    void getById_WithInvalidUuid_ShouldReturnBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(get("/customers/{uuid}", "invalid-uuid"))
                .andExpect(status().isBadRequest());

        verify(customerService, never()).findByUuid(anyString());
    }

    @Test
    void create_WithValidCustomer_ShouldReturnCreatedCustomer() throws Exception {
        // Given
        when(customerService.create(any(Customer.class))).thenReturn(testCustomer);

        // When & Then
        mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testCustomer)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.uuid").value(validUuid))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));

        verify(customerService).create(any(Customer.class));
    }

    @Test
    void create_WithInvalidCustomer_ShouldReturnBadRequest() throws Exception {
        // Given
        Customer invalidCustomer = CustomerBuilder.customer()
                .firstName("") // Invalid: empty first name
                .lastName("Doe")
                .email("invalid-email") // Invalid: wrong email format
                .phone("+1234567890")
                .address("123 Main St")
                .city("New York")
                .state("NY")
                .zip("10001")
                .country("USA")
                .build();

        // When & Then
        mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidCustomer)))
                .andExpect(status().isBadRequest());

        verify(customerService, never()).create(any(Customer.class));
    }

    @Test
    void update_WithValidData_ShouldReturnUpdatedCustomer() throws Exception {
        // Given
        when(customerService.update(eq(validUuid), any(Customer.class))).thenReturn(testCustomer);

        // When & Then
        mockMvc.perform(put("/customers/{uuid}", validUuid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testCustomer)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.uuid").value(validUuid))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));

        verify(customerService).update(eq(validUuid), any(Customer.class));
    }

    @Test
    void update_WithInvalidUuid_ShouldReturnBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(put("/customers/{uuid}", "invalid-uuid")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testCustomer)))
                .andExpect(status().isBadRequest());

        verify(customerService, never()).update(anyString(), any(Customer.class));
    }

    @Test
    void delete_WithValidUuid_ShouldReturnNoContent() throws Exception {
        // Given
        doNothing().when(customerService).delete(validUuid);

        // When & Then
        mockMvc.perform(delete("/customers/{uuid}", validUuid))
                .andExpect(status().isNoContent());

        verify(customerService).delete(validUuid);
    }

    @Test
    void delete_WithInvalidUuid_ShouldReturnBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(delete("/customers/{uuid}", "invalid-uuid"))
                .andExpect(status().isBadRequest());

        verify(customerService, never()).delete(anyString());
    }

    @Test
    void exists_WithValidUuid_ShouldReturnTrue() throws Exception {
        // Given
        when(customerService.exists(validUuid)).thenReturn(true);

        // When & Then
        mockMvc.perform(get("/customers/{uuid}/exists", validUuid))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(customerService).exists(validUuid);
    }

    @Test
    void exists_WithInvalidUuid_ShouldReturnBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(get("/customers/{uuid}/exists", "invalid-uuid"))
                .andExpect(status().isBadRequest());

        verify(customerService, never()).exists(anyString());
    }

    @Test
    void count_ShouldReturnCustomerCount() throws Exception {
        // Given
        when(customerService.count()).thenReturn(25L);

        // When & Then
        mockMvc.perform(get("/customers/count"))
                .andExpect(status().isOk())
                .andExpect(content().string("25"));

        verify(customerService).count();
    }

    @Test
    void create_WithMissingRequiredFields_ShouldReturnBadRequest() throws Exception {
        // Given
        Customer customerWithMissingFields = CustomerBuilder.customer()
                .firstName("John")
                .lastName("") // Missing lastName
                .email("") // Missing email
                .phone("+1234567890")
                .address("123 Main St")
                .city("New York")
                .state("NY")
                .zip("10001")
                .country("USA")
                .build();

        // When & Then
        mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerWithMissingFields)))
                .andExpect(status().isBadRequest());

        verify(customerService, never()).create(any(Customer.class));
    }

    @Test
    void update_WithInvalidEmail_ShouldReturnBadRequest() throws Exception {
        // Given
        Customer customerWithInvalidEmail = CustomerBuilder.customer()
                .firstName("John")
                .lastName("Doe")
                .email("invalid-email-format") // Invalid email format
                .phone("+1234567890")
                .address("123 Main St")
                .city("New York")
                .state("NY")
                .zip("10001")
                .country("USA")
                .build();

        // When & Then
        mockMvc.perform(put("/customers/{uuid}", validUuid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerWithInvalidEmail)))
                .andExpect(status().isBadRequest());

        verify(customerService, never()).update(anyString(), any(Customer.class));
    }
} 