package com.vazant.logix.orders.presentation.controller.organization;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vazant.logix.orders.application.service.common.CrudService;
import com.vazant.logix.orders.application.service.organization.OrganizationService;
import com.vazant.logix.orders.domain.organization.Organization;
import com.vazant.logix.orders.domain.organization.OrganizationBuilder;
import com.vazant.logix.orders.dto.organization.OrganizationCreationRequest;
import com.vazant.logix.orders.dto.organization.OrganizationRequest;
import com.vazant.logix.orders.dto.organization.OrganizationSuperAdminRequest;
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
 * Test class for OrganizationController.
 * Tests CRUD operations and organization-specific endpoints.
 */
@WebMvcTest(OrganizationController.class)
class OrganizationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CrudService<Organization> organizationCrudService;

    @MockBean
    private OrganizationService organizationService;

    private Organization testOrganization;
    private OrganizationCreationRequest testCreationRequest;
    private String validUuid;

    @BeforeEach
    void setUp() {
        validUuid = UUID.randomUUID().toString();
        testOrganization = OrganizationBuilder.organization()
                .name("Test Organization")
                .email("test@organization.com")
                .address("Test organization address")
                .phoneNumber("+1234567890")
                .build();
        OrganizationRequest orgRequest = new OrganizationRequest(
                "Test Organization",
                "test@organization.com",
                "Test organization address",
                "+1234567890"
        );
        OrganizationSuperAdminRequest superAdminRequest = new OrganizationSuperAdminRequest(
                "John",
                "Doe",
                "johndoe",
                "admin@organization.com"
        );
        testCreationRequest = new OrganizationCreationRequest(orgRequest, superAdminRequest);
    }

    @Test
    void getAll_ShouldReturnAllOrganizations() throws Exception {
        // Given
        List<Organization> organizations = List.of(testOrganization);
        when(organizationCrudService.findAll()).thenReturn(organizations);

        // When & Then
        mockMvc.perform(get("/api/organizations"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].uuid").value(validUuid))
                .andExpect(jsonPath("$[0].name").value("Test Organization"))
                .andExpect(jsonPath("$[0].description").value("Test organization description"));

        verify(organizationCrudService).findAll();
    }

    @Test
    void getById_WithValidUuid_ShouldReturnOrganization() throws Exception {
        // Given
        when(organizationCrudService.findByUuid(validUuid)).thenReturn(testOrganization);

        // When & Then
        mockMvc.perform(get("/api/organizations/{uuid}", validUuid))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.uuid").value(validUuid))
                .andExpect(jsonPath("$.name").value("Test Organization"))
                .andExpect(jsonPath("$.description").value("Test organization description"));

        verify(organizationCrudService).findByUuid(validUuid);
    }

    @Test
    void getById_WithInvalidUuid_ShouldReturnBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/organizations/{uuid}", "invalid-uuid"))
                .andExpect(status().isBadRequest());

        verify(organizationCrudService, never()).findByUuid(anyString());
    }

    @Test
    void create_WithValidOrganization_ShouldReturnCreatedOrganization() throws Exception {
        // Given
        when(organizationCrudService.create(any(Organization.class))).thenReturn(testOrganization);

        // When & Then
        mockMvc.perform(post("/api/organizations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testOrganization)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.uuid").value(validUuid))
                .andExpect(jsonPath("$.name").value("Test Organization"));

        verify(organizationCrudService).create(any(Organization.class));
    }

    @Test
    void create_WithInvalidOrganization_ShouldReturnBadRequest() throws Exception {
        // Given
        Organization invalidOrganization = new Organization(
                "",
                "test@organization.com",
                "Test organization address",
                "+1234567890"
        ); // Invalid: empty name
        // When & Then
        mockMvc.perform(post("/api/organizations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidOrganization)))
                .andExpect(status().isBadRequest());
        verify(organizationCrudService, never()).create(any(Organization.class));
    }

    @Test
    void update_WithValidData_ShouldReturnUpdatedOrganization() throws Exception {
        // Given
        when(organizationCrudService.update(eq(validUuid), any(Organization.class))).thenReturn(testOrganization);

        // When & Then
        mockMvc.perform(put("/api/organizations/{uuid}", validUuid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testOrganization)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.uuid").value(validUuid))
                .andExpect(jsonPath("$.name").value("Test Organization"));

        verify(organizationCrudService).update(eq(validUuid), any(Organization.class));
    }

    @Test
    void update_WithInvalidUuid_ShouldReturnBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(put("/api/organizations/{uuid}", "invalid-uuid")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testOrganization)))
                .andExpect(status().isBadRequest());

        verify(organizationCrudService, never()).update(anyString(), any(Organization.class));
    }

    @Test
    void delete_WithValidUuid_ShouldReturnNoContent() throws Exception {
        // Given
        doNothing().when(organizationCrudService).delete(validUuid);

        // When & Then
        mockMvc.perform(delete("/api/organizations/{uuid}", validUuid))
                .andExpect(status().isNoContent());

        verify(organizationCrudService).delete(validUuid);
    }

    @Test
    void delete_WithInvalidUuid_ShouldReturnBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(delete("/api/organizations/{uuid}", "invalid-uuid"))
                .andExpect(status().isBadRequest());

        verify(organizationCrudService, never()).delete(anyString());
    }

    @Test
    void exists_WithValidUuid_ShouldReturnTrue() throws Exception {
        // Given
        when(organizationCrudService.exists(validUuid)).thenReturn(true);

        // When & Then
        mockMvc.perform(get("/api/organizations/{uuid}/exists", validUuid))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(organizationCrudService).exists(validUuid);
    }

    @Test
    void exists_WithInvalidUuid_ShouldReturnBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/organizations/{uuid}/exists", "invalid-uuid"))
                .andExpect(status().isBadRequest());

        verify(organizationCrudService, never()).exists(anyString());
    }

    @Test
    void count_ShouldReturnOrganizationCount() throws Exception {
        // Given
        when(organizationCrudService.count()).thenReturn(15L);

        // When & Then
        mockMvc.perform(get("/api/organizations/count"))
                .andExpect(status().isOk())
                .andExpect(content().string("15"));

        verify(organizationCrudService).count();
    }

    @Test
    void createOrganizationWithSuperAdmin_WithValidRequest_ShouldReturnCreatedOrganization() throws Exception {
        // Given
        when(organizationService.createWithSuperAdmin(any(OrganizationRequest.class), any(OrganizationSuperAdminRequest.class)))
                .thenReturn(testOrganization);

        // When & Then
        mockMvc.perform(post("/api/organizations/with-super-admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testCreationRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.uuid").value(validUuid))
                .andExpect(jsonPath("$.name").value("Test Organization"));

        verify(organizationService).createWithSuperAdmin(any(OrganizationRequest.class), any(OrganizationSuperAdminRequest.class));
    }

    @Test
    void createOrganizationWithSuperAdmin_WithInvalidRequest_ShouldReturnBadRequest() throws Exception {
        // Given
        OrganizationRequest invalidOrgRequest = new OrganizationRequest("", "", "", ""); // Invalid: empty name
        OrganizationSuperAdminRequest invalidAdminRequest = new OrganizationSuperAdminRequest(
                "invalid-email", // Invalid email
                "short", // Invalid: short password
                "", // Invalid: empty first name
                "" // Invalid: empty last name
        );
        OrganizationCreationRequest invalidRequest = new OrganizationCreationRequest(invalidOrgRequest, invalidAdminRequest);

        // When & Then
        mockMvc.perform(post("/api/organizations/with-super-admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(organizationService, never()).createWithSuperAdmin(any(OrganizationRequest.class), any(OrganizationSuperAdminRequest.class));
    }

    @Test
    void searchByName_WithValidName_ShouldReturnEmptyList() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/organizations/search")
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
        mockMvc.perform(get("/api/organizations/search")
                        .param("name", ""))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_WithMissingRequiredFields_ShouldReturnBadRequest() throws Exception {
        // Given
        Organization organizationWithMissingFields = new Organization(
                "",
                "test@organization.com",
                "Test organization address",
                "+1234567890"
        );
        // When & Then
        mockMvc.perform(post("/api/organizations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(organizationWithMissingFields)))
                .andExpect(status().isBadRequest());
        verify(organizationCrudService, never()).create(any(Organization.class));
    }

    @Test
    void update_WithEmptyName_ShouldReturnBadRequest() throws Exception {
        // Given
        Organization organizationWithEmptyName = new Organization(
                "",
                "test@organization.com",
                "Test organization address",
                "+1234567890"
        );
        // When & Then
        mockMvc.perform(put("/api/organizations/{uuid}", validUuid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(organizationWithEmptyName)))
                .andExpect(status().isBadRequest());
        verify(organizationCrudService, never()).update(anyString(), any(Organization.class));
    }
} 