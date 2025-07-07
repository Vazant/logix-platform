package com.vazant.logix.orders.presentation.controller.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vazant.logix.orders.application.service.common.CrudService;
import com.vazant.logix.orders.application.service.product.CategoryService;
import com.vazant.logix.orders.domain.product.Category;
import com.vazant.logix.orders.domain.product.CategoryBuilder;
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
 * Test class for CategoryController.
 * Tests CRUD operations and category-specific endpoints.
 */
@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CrudService<Category> categoryService;

    private Category testCategory;
    private String validUuid;

    @BeforeEach
    void setUp() {
        validUuid = UUID.randomUUID().toString();
        testCategory = CategoryBuilder.category()
                .name("Test Category")
                .description("Test category description")
                .build();
        // uuid, createdAt, updatedAt можно мокать через сервис, если нужно
    }

    @Test
    void getAll_ShouldReturnAllCategories() throws Exception {
        // Given
        List<Category> categories = List.of(testCategory);
        when(categoryService.findAll()).thenReturn(categories);

        // When & Then
        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].uuid").value(validUuid))
                .andExpect(jsonPath("$[0].name").value("Test Category"))
                .andExpect(jsonPath("$[0].description").value("Test category description"));

        verify(categoryService).findAll();
    }

    @Test
    void getById_WithValidUuid_ShouldReturnCategory() throws Exception {
        // Given
        when(categoryService.findByUuid(validUuid)).thenReturn(testCategory);

        // When & Then
        mockMvc.perform(get("/api/categories/{uuid}", validUuid))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.uuid").value(validUuid))
                .andExpect(jsonPath("$.name").value("Test Category"))
                .andExpect(jsonPath("$.description").value("Test category description"));

        verify(categoryService).findByUuid(validUuid);
    }

    @Test
    void getById_WithInvalidUuid_ShouldReturnBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/categories/{uuid}", "invalid-uuid"))
                .andExpect(status().isBadRequest());

        verify(categoryService, never()).findByUuid(anyString());
    }

    @Test
    void create_WithValidCategory_ShouldReturnCreatedCategory() throws Exception {
        // Given
        when(categoryService.create(any(Category.class))).thenReturn(testCategory);

        // When & Then
        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testCategory)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.uuid").value(validUuid))
                .andExpect(jsonPath("$.name").value("Test Category"))
                .andExpect(jsonPath("$.description").value("Test category description"));

        verify(categoryService).create(any(Category.class));
    }

    @Test
    void create_WithInvalidCategory_ShouldReturnBadRequest() throws Exception {
        // Given
        Category invalidCategory = new Category("", "Test category description"); // Invalid: empty name

        // When & Then
        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidCategory)))
                .andExpect(status().isBadRequest());

        verify(categoryService, never()).create(any(Category.class));
    }

    @Test
    void update_WithValidData_ShouldReturnUpdatedCategory() throws Exception {
        // Given
        when(categoryService.update(eq(validUuid), any(Category.class))).thenReturn(testCategory);

        // When & Then
        mockMvc.perform(put("/api/categories/{uuid}", validUuid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testCategory)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.uuid").value(validUuid))
                .andExpect(jsonPath("$.name").value("Test Category"));

        verify(categoryService).update(eq(validUuid), any(Category.class));
    }

    @Test
    void update_WithInvalidUuid_ShouldReturnBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(put("/api/categories/{uuid}", "invalid-uuid")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testCategory)))
                .andExpect(status().isBadRequest());

        verify(categoryService, never()).update(anyString(), any(Category.class));
    }

    @Test
    void delete_WithValidUuid_ShouldReturnNoContent() throws Exception {
        // Given
        doNothing().when(categoryService).delete(validUuid);

        // When & Then
        mockMvc.perform(delete("/api/categories/{uuid}", validUuid))
                .andExpect(status().isNoContent());

        verify(categoryService).delete(validUuid);
    }

    @Test
    void delete_WithInvalidUuid_ShouldReturnBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(delete("/api/categories/{uuid}", "invalid-uuid"))
                .andExpect(status().isBadRequest());

        verify(categoryService, never()).delete(anyString());
    }

    @Test
    void exists_WithValidUuid_ShouldReturnTrue() throws Exception {
        // Given
        when(categoryService.exists(validUuid)).thenReturn(true);

        // When & Then
        mockMvc.perform(get("/api/categories/{uuid}/exists", validUuid))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(categoryService).exists(validUuid);
    }

    @Test
    void exists_WithInvalidUuid_ShouldReturnBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/categories/{uuid}/exists", "invalid-uuid"))
                .andExpect(status().isBadRequest());

        verify(categoryService, never()).exists(anyString());
    }

    @Test
    void count_ShouldReturnCategoryCount() throws Exception {
        // Given
        when(categoryService.count()).thenReturn(8L);

        // When & Then
        mockMvc.perform(get("/api/categories/count"))
                .andExpect(status().isOk())
                .andExpect(content().string("8"));

        verify(categoryService).count();
    }

    @Test
    void searchByName_WithValidName_ShouldReturnEmptyList() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/categories/search")
                        .param("name", "electronics"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        // Note: This endpoint is not implemented yet, so it returns empty list
    }

    @Test
    void searchByName_WithEmptyName_ShouldReturnBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/categories/search")
                        .param("name", ""))
                .andExpect(status().isBadRequest());
    }

    @Test
    void searchByName_WithMissingName_ShouldReturnBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/categories/search"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_WithMissingRequiredFields_ShouldReturnBadRequest() throws Exception {
        // Given
        Category categoryWithMissingFields = new Category("", "Test description"); // Missing name

        // When & Then
        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryWithMissingFields)))
                .andExpect(status().isBadRequest());

        verify(categoryService, never()).create(any(Category.class));
    }

    @Test
    void update_WithEmptyName_ShouldReturnBadRequest() throws Exception {
        // Given
        Category categoryWithEmptyName = new Category("", "Test description"); // Invalid: empty name

        // When & Then
        mockMvc.perform(put("/api/categories/{uuid}", validUuid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryWithEmptyName)))
                .andExpect(status().isBadRequest());

        verify(categoryService, never()).update(anyString(), any(Category.class));
    }

    @Test
    void create_WithLongName_ShouldReturnBadRequest() throws Exception {
        // Given
        Category categoryWithLongName = new Category("A".repeat(256), "Test description"); // Invalid: too long name

        // When & Then
        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryWithLongName)))
                .andExpect(status().isBadRequest());

        verify(categoryService, never()).create(any(Category.class));
    }

    @Test
    void create_WithSpecialCharactersInName_ShouldWork() throws Exception {
        // Given
        Category categoryWithSpecialChars = new Category("Electronics & Gadgets", "Test description");
        when(categoryService.create(any(Category.class))).thenReturn(categoryWithSpecialChars);

        // When & Then
        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryWithSpecialChars)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Electronics & Gadgets"));

        verify(categoryService).create(any(Category.class));
    }
} 