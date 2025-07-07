package com.vazant.logix.orders.presentation.controller.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vazant.logix.orders.application.service.common.CrudService;
import com.vazant.logix.orders.domain.common.BaseEntity;
import com.vazant.logix.orders.domain.common.Updatable;
import com.vazant.logix.orders.presentation.exception.GlobalExceptionHandler;
import com.vazant.logix.orders.presentation.exception.ValidationExceptionHandler;
import com.vazant.logix.orders.presentation.validation.ValidUuid;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Base test class for controllers that extend BaseController.
 * Tests common CRUD operations and validation.
 */
@WebMvcTest(BaseControllerTest.TestBaseController.class)
class BaseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CrudService<TestEntity> crudService;

    private TestEntity testEntity;
    private String validUuid;

    @BeforeEach
    void setUp() {
        validUuid = UUID.randomUUID().toString();
        testEntity = new TestEntity();
        testEntity.setName("Test Entity");
        testEntity.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void getAll_ShouldReturnAllEntities() throws Exception {
        // Given
        List<TestEntity> entities = List.of(testEntity);
        when(crudService.findAll()).thenReturn(entities);

        // When & Then
        mockMvc.perform(get("/test"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].uuid").value(validUuid))
                .andExpect(jsonPath("$[0].name").value("Test Entity"));

        verify(crudService).findAll();
    }

    @Test
    void getById_WithValidUuid_ShouldReturnEntity() throws Exception {
        // Given
        when(crudService.findByUuid(validUuid)).thenReturn(testEntity);

        // When & Then
        mockMvc.perform(get("/test/{uuid}", validUuid))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.uuid").value(validUuid))
                .andExpect(jsonPath("$.name").value("Test Entity"));

        verify(crudService).findByUuid(validUuid);
    }

    @Test
    void getById_WithInvalidUuid_ShouldReturnBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(get("/test/{uuid}", "invalid-uuid"))
                .andExpect(status().isBadRequest());

        verify(crudService, never()).findByUuid(anyString());
    }

    @Test
    void create_WithValidEntity_ShouldReturnCreatedEntity() throws Exception {
        // Given
        when(crudService.create(any(TestEntity.class))).thenReturn(testEntity);

        // When & Then
        mockMvc.perform(post("/test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testEntity)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.uuid").value(validUuid))
                .andExpect(jsonPath("$.name").value("Test Entity"));

        verify(crudService).create(any(TestEntity.class));
    }

    @Test
    void create_WithInvalidEntity_ShouldReturnBadRequest() throws Exception {
        // Given
        TestEntity invalidEntity = new TestEntity();
        invalidEntity.setName(""); // Invalid: empty name

        // When & Then
        mockMvc.perform(post("/test")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidEntity)))
                .andExpect(status().isBadRequest());

        verify(crudService, never()).create(any(TestEntity.class));
    }

    @Test
    void update_WithValidData_ShouldReturnUpdatedEntity() throws Exception {
        // Given
        when(crudService.update(eq(validUuid), any(TestEntity.class))).thenReturn(testEntity);

        // When & Then
        mockMvc.perform(put("/test/{uuid}", validUuid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testEntity)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.uuid").value(validUuid))
                .andExpect(jsonPath("$.name").value("Test Entity"));

        verify(crudService).update(eq(validUuid), any(TestEntity.class));
    }

    @Test
    void update_WithInvalidUuid_ShouldReturnBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(put("/test/{uuid}", "invalid-uuid")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testEntity)))
                .andExpect(status().isBadRequest());

        verify(crudService, never()).update(anyString(), any(TestEntity.class));
    }

    @Test
    void delete_WithValidUuid_ShouldReturnNoContent() throws Exception {
        // Given
        doNothing().when(crudService).delete(validUuid);

        // When & Then
        mockMvc.perform(delete("/test/{uuid}", validUuid))
                .andExpect(status().isNoContent());

        verify(crudService).delete(validUuid);
    }

    @Test
    void delete_WithInvalidUuid_ShouldReturnBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(delete("/test/{uuid}", "invalid-uuid"))
                .andExpect(status().isBadRequest());

        verify(crudService, never()).delete(anyString());
    }

    @Test
    void exists_WithValidUuid_ShouldReturnTrue() throws Exception {
        // Given
        when(crudService.exists(validUuid)).thenReturn(true);

        // When & Then
        mockMvc.perform(get("/test/{uuid}/exists", validUuid))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(crudService).exists(validUuid);
    }

    @Test
    void exists_WithInvalidUuid_ShouldReturnBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(get("/test/{uuid}/exists", "invalid-uuid"))
                .andExpect(status().isBadRequest());

        verify(crudService, never()).exists(anyString());
    }

    @Test
    void count_ShouldReturnEntityCount() throws Exception {
        // Given
        when(crudService.count()).thenReturn(5L);

        // When & Then
        mockMvc.perform(get("/test/count"))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));

        verify(crudService).count();
    }

    // Test controller implementation
    @RestController
    @RequestMapping("/test")
    @Validated
    static class TestBaseController extends BaseController<TestEntity> {

        public TestBaseController(CrudService<TestEntity> crudService) {
            super(crudService);
        }
    }

    // Test entity implementation
    @Getter
    @Setter
    static class TestEntity extends BaseEntity implements Updatable<TestEntity> {
        
        @NotBlank(message = "Name is required")
        private String name;

        @Override
        public void doUpdate(TestEntity source) {
            this.name = source.getName();
        }
    }
} 