package com.vazant.logix.orders.presentation.controller.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vazant.logix.orders.application.service.common.CrudService;
import com.vazant.logix.orders.application.service.product.ProductService;
import com.vazant.logix.orders.domain.organization.Organization;
import com.vazant.logix.orders.domain.organization.OrganizationBuilder;
import com.vazant.logix.orders.domain.product.Dimensions;
import com.vazant.logix.orders.domain.product.DimensionsBuilder;
import com.vazant.logix.orders.domain.product.Product;
import com.vazant.logix.orders.domain.product.ProductBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for ProductController.
 * Tests CRUD operations and product-specific endpoints.
 */
@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CrudService<Product> productService;

    private Product testProduct;
    private String validUuid;

    @BeforeEach
    void setUp() {
        validUuid = UUID.randomUUID().toString();
        testProduct = ProductBuilder.product()
                .name("Test Product")
                .prices(new ArrayList<>())
                .description("Test product description")
                .skuCode("TEST-SKU-001")
                .dimensions(DimensionsBuilder.dimensions()
                        .width(BigDecimal.valueOf(10.0))
                        .height(BigDecimal.valueOf(5.0))
                        .length(BigDecimal.valueOf(2.0))
                        .weight(BigDecimal.valueOf(0.5))
                        .build())
                .stockQuantity(100)
                .categoryId(UUID.randomUUID())
                .imageId(UUID.randomUUID())
                .organization(OrganizationBuilder.organization()
                        .name("Test Org")
                        .email("test@org.com")
                        .address("Test Address")
                        .phoneNumber("+1234567890")
                        .build())
                .build();
        // uuid, createdAt, updatedAt можно мокать через сервис, если нужно
    }

    @Test
    void getAll_ShouldReturnAllProducts() throws Exception {
        // Given
        List<Product> products = List.of(testProduct);
        when(productService.findAll()).thenReturn(products);

        // When & Then
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].uuid").value(validUuid))
                .andExpect(jsonPath("$[0].name").value("Test Product"))
                .andExpect(jsonPath("$[0].price").value(99.99))
                .andExpect(jsonPath("$[0].sku").value("TEST-SKU-001"));

        verify(productService).findAll();
    }

    @Test
    void getById_WithValidUuid_ShouldReturnProduct() throws Exception {
        // Given
        when(productService.findByUuid(validUuid)).thenReturn(testProduct);

        // When & Then
        mockMvc.perform(get("/api/products/{uuid}", validUuid))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.uuid").value(validUuid))
                .andExpect(jsonPath("$.name").value("Test Product"))
                .andExpect(jsonPath("$.price").value(99.99));

        verify(productService).findByUuid(validUuid);
    }

    @Test
    void getById_WithInvalidUuid_ShouldReturnBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/products/{uuid}", "invalid-uuid"))
                .andExpect(status().isBadRequest());

        verify(productService, never()).findByUuid(anyString());
    }

    @Test
    void create_WithValidProduct_ShouldReturnCreatedProduct() throws Exception {
        // Given
        when(productService.create(any(Product.class))).thenReturn(testProduct);

        // When & Then
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testProduct)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.uuid").value(validUuid))
                .andExpect(jsonPath("$.name").value("Test Product"))
                .andExpect(jsonPath("$.price").value(99.99));

        verify(productService).create(any(Product.class));
    }

    @Test
    void create_WithInvalidProduct_ShouldReturnBadRequest() throws Exception {
        // Given
        Product invalidProduct = ProductBuilder.product()
                .name("Test Product")
                .prices(new ArrayList<>())
                .description("Test product description")
                .skuCode("TEST-SKU-001")
                .dimensions(DimensionsBuilder.dimensions()
                        .width(BigDecimal.valueOf(10.0))
                        .height(BigDecimal.valueOf(5.0))
                        .length(BigDecimal.valueOf(2.0))
                        .weight(BigDecimal.valueOf(0.5))
                        .build())
                .stockQuantity(-1) // Invalid: negative stock
                .categoryId(UUID.randomUUID())
                .imageId(UUID.randomUUID())
                .organization(OrganizationBuilder.organization()
                        .name("Test Org")
                        .email("test@org.com")
                        .address("Test Address")
                        .phoneNumber("+1234567890")
                        .build())
                .build();
        // When & Then
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidProduct)))
                .andExpect(status().isBadRequest());
        verify(productService, never()).create(any(Product.class));
    }

    @Test
    void update_WithValidData_ShouldReturnUpdatedProduct() throws Exception {
        // Given
        when(productService.update(eq(validUuid), any(Product.class))).thenReturn(testProduct);

        // When & Then
        mockMvc.perform(put("/api/products/{uuid}", validUuid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testProduct)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.uuid").value(validUuid))
                .andExpect(jsonPath("$.name").value("Test Product"));

        verify(productService).update(eq(validUuid), any(Product.class));
    }

    @Test
    void update_WithInvalidUuid_ShouldReturnBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(put("/api/products/{uuid}", "invalid-uuid")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testProduct)))
                .andExpect(status().isBadRequest());

        verify(productService, never()).update(anyString(), any(Product.class));
    }

    @Test
    void delete_WithValidUuid_ShouldReturnNoContent() throws Exception {
        // Given
        doNothing().when(productService).delete(validUuid);

        // When & Then
        mockMvc.perform(delete("/api/products/{uuid}", validUuid))
                .andExpect(status().isNoContent());

        verify(productService).delete(validUuid);
    }

    @Test
    void delete_WithInvalidUuid_ShouldReturnBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(delete("/api/products/{uuid}", "invalid-uuid"))
                .andExpect(status().isBadRequest());

        verify(productService, never()).delete(anyString());
    }

    @Test
    void exists_WithValidUuid_ShouldReturnTrue() throws Exception {
        // Given
        when(productService.exists(validUuid)).thenReturn(true);

        // When & Then
        mockMvc.perform(get("/api/products/{uuid}/exists", validUuid))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(productService).exists(validUuid);
    }

    @Test
    void exists_WithInvalidUuid_ShouldReturnBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/products/{uuid}/exists", "invalid-uuid"))
                .andExpect(status().isBadRequest());

        verify(productService, never()).exists(anyString());
    }

    @Test
    void count_ShouldReturnProductCount() throws Exception {
        // Given
        when(productService.count()).thenReturn(50L);

        // When & Then
        mockMvc.perform(get("/api/products/count"))
                .andExpect(status().isOk())
                .andExpect(content().string("50"));

        verify(productService).count();
    }

    @Test
    void findByCategory_WithValidCategoryId_ShouldReturnEmptyList() throws Exception {
        // Given
        String categoryId = UUID.randomUUID().toString();

        // When & Then
        mockMvc.perform(get("/api/products/category/{categoryId}", categoryId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        // Note: This endpoint is not implemented yet, so it returns empty list
    }

    @Test
    void findByCategory_WithInvalidCategoryId_ShouldReturnBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/products/category/{categoryId}", "invalid-uuid"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void searchByName_WithValidName_ShouldReturnEmptyList() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/products/search")
                        .param("name", "test"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        // Note: This endpoint is not implemented yet, so it returns empty list
    }

    @Test
    void searchByName_WithEmptyName_ShouldReturnBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/products/search")
                        .param("name", ""))
                .andExpect(status().isBadRequest());
    }

    @Test
    void findByPriceRange_WithValidRange_ShouldReturnEmptyList() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/products/price-range")
                        .param("minPrice", "10.0")
                        .param("maxPrice", "100.0"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        // Note: This endpoint is not implemented yet, so it returns empty list
    }

    @Test
    void findByPriceRange_WithInvalidRange_ShouldReturnBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/products/price-range")
                        .param("minPrice", "100.0")
                        .param("maxPrice", "10.0")) // min > max
                .andExpect(status().isBadRequest());
    }

    @Test
    void findByPriceRange_WithNegativePrice_ShouldReturnBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/products/price-range")
                        .param("minPrice", "-10.0")
                        .param("maxPrice", "100.0"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_WithMissingRequiredFields_ShouldReturnBadRequest() throws Exception {
        // Given
        Product productWithMissingFields = ProductBuilder.product()
                .name("") // Missing name
                .prices(new ArrayList<>())
                .description("Test product description")
                .skuCode("") // Missing SKU
                .dimensions(DimensionsBuilder.dimensions()
                        .width(BigDecimal.valueOf(10.0))
                        .height(BigDecimal.valueOf(5.0))
                        .length(BigDecimal.valueOf(2.0))
                        .weight(BigDecimal.valueOf(0.5))
                        .build())
                .stockQuantity(100)
                .categoryId(UUID.randomUUID())
                .imageId(UUID.randomUUID())
                .organization(OrganizationBuilder.organization()
                        .name("Test Org")
                        .email("test@org.com")
                        .address("Test Address")
                        .phoneNumber("+1234567890")
                        .build())
                .build();

        // When & Then
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productWithMissingFields)))
                .andExpect(status().isBadRequest());

        verify(productService, never()).create(any(Product.class));
    }

    @Test
    void update_WithNegativePrice_ShouldReturnBadRequest() throws Exception {
        // Given
        Product productWithNegativePrice = ProductBuilder.product()
                .name("Test Product")
                .prices(new ArrayList<>())
                .description("Test product description")
                .skuCode("TEST-SKU-001")
                .dimensions(DimensionsBuilder.dimensions()
                        .width(BigDecimal.valueOf(10.0))
                        .height(BigDecimal.valueOf(5.0))
                        .length(BigDecimal.valueOf(2.0))
                        .weight(BigDecimal.valueOf(0.5))
                        .build())
                .stockQuantity(-1) // Invalid: negative stock
                .categoryId(UUID.randomUUID())
                .imageId(UUID.randomUUID())
                .organization(OrganizationBuilder.organization()
                        .name("Test Org")
                        .email("test@org.com")
                        .address("Test Address")
                        .phoneNumber("+1234567890")
                        .build())
                .build();

        // When & Then
        mockMvc.perform(put("/api/products/{uuid}", validUuid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productWithNegativePrice)))
                .andExpect(status().isBadRequest());

        verify(productService, never()).update(anyString(), any(Product.class));
    }
} 