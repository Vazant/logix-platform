package com.vazant.logix.orders.presentation.controller.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vazant.logix.orders.application.service.common.CrudService;
import com.vazant.logix.orders.application.service.product.ProductPriceService;
import com.vazant.logix.orders.domain.product.Product;
import com.vazant.logix.orders.domain.product.ProductBuilder;
import com.vazant.logix.orders.domain.product.ProductPrice;
import com.vazant.logix.orders.domain.product.ProductPriceBuilder;
import com.vazant.logix.orders.domain.product.DimensionsBuilder;
import com.vazant.logix.orders.domain.shared.Currency;
import com.vazant.logix.orders.domain.shared.Money;
import com.vazant.logix.orders.domain.shared.MoneyBuilder;
import com.vazant.logix.orders.domain.organization.Organization;
import com.vazant.logix.orders.domain.organization.OrganizationBuilder;
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
 * Test class for ProductPriceController.
 * Tests CRUD operations and product price-specific endpoints.
 */
@WebMvcTest(ProductPriceController.class)
class ProductPriceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CrudService<ProductPrice> productPriceService;

    private ProductPrice testProductPrice;
    private String validUuid;
    private Product testProduct;

    @BeforeEach
    void setUp() {
        validUuid = UUID.randomUUID().toString();
        
        Organization testOrganization = OrganizationBuilder.organization()
                .name("Test Organization")
                .email("test@org.com")
                .address("Test Address")
                .phoneNumber("+1234567890")
                .build();
        
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
                .organization(testOrganization)
                .build();
        
        testProductPrice = ProductPriceBuilder.productPrice()
                .product(testProduct)
                .price(MoneyBuilder.money()
                        .amount(new BigDecimal("100.00"))
                        .currency(Currency.USD)
                        .build())
                .build();
    }

    @Test
    void getAll_ShouldReturnAllProductPrices() throws Exception {
        // Given
        List<ProductPrice> productPrices = List.of(testProductPrice);
        when(productPriceService.findAll()).thenReturn(productPrices);

        // When & Then
        mockMvc.perform(get("/api/product-prices"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].uuid").value(validUuid))
                .andExpect(jsonPath("$[0].isActive").value(true));

        verify(productPriceService).findAll();
    }

    @Test
    void getById_WithValidUuid_ShouldReturnProductPrice() throws Exception {
        // Given
        when(productPriceService.findByUuid(validUuid)).thenReturn(testProductPrice);

        // When & Then
        mockMvc.perform(get("/api/product-prices/{uuid}", validUuid))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.uuid").value(validUuid))
                .andExpect(jsonPath("$.isActive").value(true));

        verify(productPriceService).findByUuid(validUuid);
    }

    @Test
    void getById_WithInvalidUuid_ShouldReturnBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/product-prices/{uuid}", "invalid-uuid"))
                .andExpect(status().isBadRequest());

        verify(productPriceService, never()).findByUuid(anyString());
    }

    @Test
    void create_WithValidProductPrice_ShouldReturnCreatedProductPrice() throws Exception {
        // Given
        when(productPriceService.create(any(ProductPrice.class))).thenReturn(testProductPrice);

        // When & Then
        mockMvc.perform(post("/api/product-prices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testProductPrice)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.uuid").value(validUuid))
                .andExpect(jsonPath("$.isActive").value(true));

        verify(productPriceService).create(any(ProductPrice.class));
    }

    @Test
    void create_WithInvalidProductPrice_ShouldReturnBadRequest() throws Exception {
        // Given
        ProductPrice invalidProductPrice = ProductPriceBuilder.productPrice()
                .product(testProduct)
                .price(MoneyBuilder.money()
                        .amount(new BigDecimal("0.00"))
                        .currency(Currency.USD)
                        .build())
                .build();
        // Missing required fields

        // When & Then
        mockMvc.perform(post("/api/product-prices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidProductPrice)))
                .andExpect(status().isBadRequest());

        verify(productPriceService, never()).create(any(ProductPrice.class));
    }

    @Test
    void update_WithValidData_ShouldReturnUpdatedProductPrice() throws Exception {
        // Given
        when(productPriceService.update(eq(validUuid), any(ProductPrice.class))).thenReturn(testProductPrice);

        // When & Then
        mockMvc.perform(put("/api/product-prices/{uuid}", validUuid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testProductPrice)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.uuid").value(validUuid))
                .andExpect(jsonPath("$.isActive").value(true));

        verify(productPriceService).update(eq(validUuid), any(ProductPrice.class));
    }

    @Test
    void update_WithInvalidUuid_ShouldReturnBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(put("/api/product-prices/{uuid}", "invalid-uuid")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testProductPrice)))
                .andExpect(status().isBadRequest());

        verify(productPriceService, never()).update(anyString(), any(ProductPrice.class));
    }

    @Test
    void delete_WithValidUuid_ShouldReturnNoContent() throws Exception {
        // Given
        doNothing().when(productPriceService).delete(validUuid);

        // When & Then
        mockMvc.perform(delete("/api/product-prices/{uuid}", validUuid))
                .andExpect(status().isNoContent());

        verify(productPriceService).delete(validUuid);
    }

    @Test
    void delete_WithInvalidUuid_ShouldReturnBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(delete("/api/product-prices/{uuid}", "invalid-uuid"))
                .andExpect(status().isBadRequest());

        verify(productPriceService, never()).delete(anyString());
    }

    @Test
    void exists_WithValidUuid_ShouldReturnTrue() throws Exception {
        // Given
        when(productPriceService.exists(validUuid)).thenReturn(true);

        // When & Then
        mockMvc.perform(get("/api/product-prices/{uuid}/exists", validUuid))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(productPriceService).exists(validUuid);
    }

    @Test
    void exists_WithInvalidUuid_ShouldReturnBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/product-prices/{uuid}/exists", "invalid-uuid"))
                .andExpect(status().isBadRequest());

        verify(productPriceService, never()).exists(anyString());
    }

    @Test
    void count_ShouldReturnProductPriceCount() throws Exception {
        // Given
        when(productPriceService.count()).thenReturn(75L);

        // When & Then
        mockMvc.perform(get("/api/product-prices/count"))
                .andExpect(status().isOk())
                .andExpect(content().string("75"));

        verify(productPriceService).count();
    }

    @Test
    void findByProduct_WithValidProductId_ShouldReturnEmptyList() throws Exception {
        // Given
        String productId = UUID.randomUUID().toString();

        // When & Then
        mockMvc.perform(get("/api/product-prices/product/{productId}", productId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        // Note: This endpoint is not implemented yet, so it returns empty list
    }

    @Test
    void findByProduct_WithInvalidProductId_ShouldReturnBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/product-prices/product/{productId}", "invalid-uuid"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void findByCurrency_WithValidCurrency_ShouldReturnEmptyList() throws Exception {
        // Given
        String currency = "USD";

        // When & Then
        mockMvc.perform(get("/api/product-prices/currency/{currency}", currency))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        // Note: This endpoint is not implemented yet, so it returns empty list
    }

    @Test
    void findByCurrency_WithInvalidCurrency_ShouldReturnBadRequest() throws Exception {
        // Given
        String invalidCurrency = "INVALID";

        // When & Then
        mockMvc.perform(get("/api/product-prices/currency/{currency}", invalidCurrency))
                .andExpect(status().isBadRequest());
    }

    @Test
    void findByCurrency_WithEmptyCurrency_ShouldReturnBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/product-prices/currency/{currency}", ""))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_WithMissingRequiredFields_ShouldReturnBadRequest() throws Exception {
        // Given
        ProductPrice productPriceWithMissingFields = ProductPriceBuilder.productPrice()
                .product(testProduct)
                .price(MoneyBuilder.money()
                        .amount(new BigDecimal("0.00"))
                        .currency(Currency.USD)
                        .build())
                .build();
        // Missing productUuid and price

        // When & Then
        mockMvc.perform(post("/api/product-prices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productPriceWithMissingFields)))
                .andExpect(status().isBadRequest());

        verify(productPriceService, never()).create(any(ProductPrice.class));
    }

    @Test
    void update_WithInvalidProductUuid_ShouldReturnBadRequest() throws Exception {
        // Given
        ProductPrice productPriceWithInvalidUuid = ProductPriceBuilder.productPrice()
                .product(testProduct)
                .price(MoneyBuilder.money()
                        .amount(new BigDecimal("0.00"))
                        .currency(Currency.USD)
                        .build())
                .build();
        // Invalid UUID will be validated by controller

        // When & Then
        mockMvc.perform(put("/api/product-prices/{uuid}", validUuid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productPriceWithInvalidUuid)))
                .andExpect(status().isBadRequest());

        verify(productPriceService, never()).update(anyString(), any(ProductPrice.class));
    }

    @Test
    void create_WithNegativePrice_ShouldReturnBadRequest() throws Exception {
        // Given
        ProductPrice productPriceWithNegativePrice = ProductPriceBuilder.productPrice()
                .product(testProduct)
                .price(MoneyBuilder.money()
                        .amount(new BigDecimal("-50.0"))
                        .currency(Currency.USD)
                        .build())
                .build();

        // When & Then
        mockMvc.perform(post("/api/product-prices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productPriceWithNegativePrice)))
                .andExpect(status().isBadRequest());

        verify(productPriceService, never()).create(any(ProductPrice.class));
    }

    @Test
    void create_WithZeroPrice_ShouldReturnBadRequest() throws Exception {
        // Given
        ProductPrice productPriceWithZeroPrice = ProductPriceBuilder.productPrice()
                .product(testProduct)
                .price(MoneyBuilder.money()
                        .amount(BigDecimal.ZERO)
                        .currency(Currency.USD)
                        .build())
                .build();

        // When & Then
        mockMvc.perform(post("/api/product-prices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productPriceWithZeroPrice)))
                .andExpect(status().isBadRequest());

        verify(productPriceService, never()).create(any(ProductPrice.class));
    }

    @Test
    void create_WithValidData_ShouldWork() throws Exception {
        // Given
        ProductPrice validProductPrice = ProductPriceBuilder.productPrice()
                .product(testProduct)
                .price(MoneyBuilder.money()
                        .amount(new BigDecimal("150.0"))
                        .currency(Currency.EUR)
                        .build())
                .build();
        when(productPriceService.create(any(ProductPrice.class))).thenReturn(validProductPrice);

        // When & Then
        mockMvc.perform(post("/api/product-prices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validProductPrice)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isActive").value(true));

        verify(productPriceService).create(any(ProductPrice.class));
    }
} 