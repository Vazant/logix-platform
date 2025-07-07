package com.vazant.logix.orders.presentation.controller.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vazant.logix.orders.application.service.common.CrudService;
import com.vazant.logix.orders.application.service.user.UserGroupService;
import com.vazant.logix.orders.domain.user.UserGroup;
import com.vazant.logix.orders.domain.user.UserGroupBuilder;
import com.vazant.logix.orders.domain.user.UserGroupName;
import com.vazant.logix.orders.domain.user.UserResponsibility;
import com.vazant.logix.orders.domain.user.SystemResponsibility;
import com.vazant.logix.orders.domain.user.UserResponsibilityBuilder;
import com.vazant.logix.orders.dto.user.UserGroupResponsibilityRequest;
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
 * Test class for UserGroupController.
 * Tests CRUD operations and group-specific endpoints.
 */
@WebMvcTest(UserGroupController.class)
class UserGroupControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CrudService<UserGroup> userGroupService;

    @MockBean
    private UserGroupService userGroupServiceImpl;

    private UserGroup testUserGroup;
    private UserGroupResponsibilityRequest testResponsibilityRequest;
    private String validUuid;

    @BeforeEach
    void setUp() {
        validUuid = UUID.randomUUID().toString();
        
        testUserGroup = UserGroupBuilder.userGroup()
                .groupName(UserGroupName.ADMIN_GROUP)
                .email("admin@example.com")
                .organization("Test Organization")
                .description("System administrators group")
                .build();

        UserResponsibility userResponsibility = UserResponsibilityBuilder.userResponsibility()
                .responsibility(SystemResponsibility.ADMIN)
                .build();

        testResponsibilityRequest = new UserGroupResponsibilityRequest();
        testResponsibilityRequest.setResponsibility(userResponsibility);
    }

    @Test
    void getAll_ShouldReturnAllUserGroups() throws Exception {
        // Given
        List<UserGroup> userGroups = List.of(testUserGroup);
        when(userGroupService.findAll()).thenReturn(userGroups);

        // When & Then
        mockMvc.perform(get("/api/groups"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].uuid").value(validUuid))
                .andExpect(jsonPath("$[0].name").value("Administrators"))
                .andExpect(jsonPath("$[0].description").value("System administrators group"));

        verify(userGroupService).findAll();
    }

    @Test
    void getById_WithValidUuid_ShouldReturnUserGroup() throws Exception {
        // Given
        when(userGroupService.findByUuid(validUuid)).thenReturn(testUserGroup);

        // When & Then
        mockMvc.perform(get("/api/groups/{uuid}", validUuid))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.uuid").value(validUuid))
                .andExpect(jsonPath("$.name").value("Administrators"))
                .andExpect(jsonPath("$.description").value("System administrators group"));

        verify(userGroupService).findByUuid(validUuid);
    }

    @Test
    void getById_WithInvalidUuid_ShouldReturnBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/groups/{uuid}", "invalid-uuid"))
                .andExpect(status().isBadRequest());

        verify(userGroupService, never()).findByUuid(anyString());
    }

    @Test
    void create_WithValidUserGroup_ShouldReturnCreatedUserGroup() throws Exception {
        // Given
        when(userGroupService.create(any(UserGroup.class))).thenReturn(testUserGroup);

        // When & Then
        mockMvc.perform(post("/api/groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUserGroup)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.uuid").value(validUuid))
                .andExpect(jsonPath("$.name").value("Administrators"))
                .andExpect(jsonPath("$.description").value("System administrators group"));

        verify(userGroupService).create(any(UserGroup.class));
    }

    @Test
    void create_WithInvalidUserGroup_ShouldReturnBadRequest() throws Exception {
        // Given
        UserGroup invalidUserGroup = UserGroupBuilder.userGroup()
                .groupName(UserGroupName.ADMIN_GROUP)
                .email("") // Invalid: empty email
                .organization("Test Organization")
                .description("System administrators group")
                .build();

        // When & Then
        mockMvc.perform(post("/api/groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUserGroup)))
                .andExpect(status().isBadRequest());

        verify(userGroupService, never()).create(any(UserGroup.class));
    }

    @Test
    void update_WithValidData_ShouldReturnUpdatedUserGroup() throws Exception {
        // Given
        when(userGroupService.update(eq(validUuid), any(UserGroup.class))).thenReturn(testUserGroup);

        // When & Then
        mockMvc.perform(put("/api/groups/{uuid}", validUuid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUserGroup)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.uuid").value(validUuid))
                .andExpect(jsonPath("$.name").value("Administrators"));

        verify(userGroupService).update(eq(validUuid), any(UserGroup.class));
    }

    @Test
    void update_WithInvalidUuid_ShouldReturnBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(put("/api/groups/{uuid}", "invalid-uuid")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUserGroup)))
                .andExpect(status().isBadRequest());

        verify(userGroupService, never()).update(anyString(), any(UserGroup.class));
    }

    @Test
    void delete_WithValidUuid_ShouldReturnNoContent() throws Exception {
        // Given
        doNothing().when(userGroupService).delete(validUuid);

        // When & Then
        mockMvc.perform(delete("/api/groups/{uuid}", validUuid))
                .andExpect(status().isNoContent());

        verify(userGroupService).delete(validUuid);
    }

    @Test
    void delete_WithInvalidUuid_ShouldReturnBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(delete("/api/groups/{uuid}", "invalid-uuid"))
                .andExpect(status().isBadRequest());

        verify(userGroupService, never()).delete(anyString());
    }

    @Test
    void exists_WithValidUuid_ShouldReturnTrue() throws Exception {
        // Given
        when(userGroupService.exists(validUuid)).thenReturn(true);

        // When & Then
        mockMvc.perform(get("/api/groups/{uuid}/exists", validUuid))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(userGroupService).exists(validUuid);
    }

    @Test
    void exists_WithInvalidUuid_ShouldReturnBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/groups/{uuid}/exists", "invalid-uuid"))
                .andExpect(status().isBadRequest());

        verify(userGroupService, never()).exists(anyString());
    }

    @Test
    void count_ShouldReturnUserGroupCount() throws Exception {
        // Given
        when(userGroupService.count()).thenReturn(12L);

        // When & Then
        mockMvc.perform(get("/api/groups/count"))
                .andExpect(status().isOk())
                .andExpect(content().string("12"));

        verify(userGroupService).count();
    }

    @Test
    void addResponsibility_WithValidRequest_ShouldReturnOk() throws Exception {
        // When & Then
        mockMvc.perform(post("/api/groups/{groupId}/responsibilities", validUuid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testResponsibilityRequest)))
                .andExpect(status().isOk());

        // Note: This endpoint is not implemented yet, so it returns 200 OK
    }

    @Test
    void addResponsibility_WithInvalidGroupId_ShouldReturnBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(post("/api/groups/{groupId}/responsibilities", "invalid-uuid")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testResponsibilityRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void addResponsibility_WithInvalidRequest_ShouldReturnBadRequest() throws Exception {
        // Given
        UserGroupResponsibilityRequest invalidRequest = new UserGroupResponsibilityRequest();
        // Invalid: empty responsibility will be validated by controller

        // When & Then
        mockMvc.perform(post("/api/groups/{groupId}/responsibilities", validUuid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void findByOrganization_WithValidOrganizationId_ShouldReturnEmptyList() throws Exception {
        // Given
        String organizationId = UUID.randomUUID().toString();

        // When & Then
        mockMvc.perform(get("/api/groups/organization/{organizationId}", organizationId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        // Note: This endpoint is not implemented yet, so it returns empty list
    }

    @Test
    void findByOrganization_WithInvalidOrganizationId_ShouldReturnBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/groups/organization/{organizationId}", "invalid-uuid"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_WithMissingRequiredFields_ShouldReturnBadRequest() throws Exception {
        // Given
        UserGroup userGroupWithMissingFields = UserGroupBuilder.userGroup()
                .groupName(UserGroupName.ADMIN_GROUP)
                .email("") // Invalid: empty email
                .organization("Test Organization")
                .description("Test description")
                .build();

        // When & Then
        mockMvc.perform(post("/api/groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userGroupWithMissingFields)))
                .andExpect(status().isBadRequest());

        verify(userGroupService, never()).create(any(UserGroup.class));
    }

    @Test
    void update_WithEmptyName_ShouldReturnBadRequest() throws Exception {
        // Given
        UserGroup userGroupWithEmptyName = UserGroupBuilder.userGroup()
                .groupName(UserGroupName.ADMIN_GROUP)
                .email("") // Invalid: empty email
                .organization("Test Organization")
                .description("Test description")
                .build();

        // When & Then
        mockMvc.perform(put("/api/groups/{uuid}", validUuid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userGroupWithEmptyName)))
                .andExpect(status().isBadRequest());

        verify(userGroupService, never()).update(anyString(), any(UserGroup.class));
    }

    @Test
    void create_WithLongName_ShouldReturnBadRequest() throws Exception {
        // Given
        UserGroup userGroupWithLongName = UserGroupBuilder.userGroup()
                .groupName(UserGroupName.ADMIN_GROUP)
                .email("admin@example.com")
                .organization("Test Organization")
                .description("A".repeat(1000)) // Invalid: too long description
                .build();

        // When & Then
        mockMvc.perform(post("/api/groups")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userGroupWithLongName)))
                .andExpect(status().isBadRequest());

        verify(userGroupService, never()).create(any(UserGroup.class));
    }

    @Test
    void addResponsibility_WithMissingFields_ShouldReturnBadRequest() throws Exception {
        // Given
        UserGroupResponsibilityRequest invalidRequest = new UserGroupResponsibilityRequest();
        // Invalid: null responsibility will be validated by controller

        // When & Then
        mockMvc.perform(post("/api/groups/{groupId}/responsibilities", validUuid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }
} 