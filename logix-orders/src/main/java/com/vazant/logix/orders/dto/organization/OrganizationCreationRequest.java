package com.vazant.logix.orders.dto.organization;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

/**
 * Request DTO for creating an organization along with its super admin.
 * <p>
 * Contains the organization details and the super admin details, both validated.
 *
 * @param organizationRequest the organization details
 * @param superAdminRequest the super admin details
 */
public record OrganizationCreationRequest(
    @NotNull(message = "{validation.organization.creation.organizationRequest.required}")
    @Valid
    OrganizationRequest organizationRequest,
    
    @NotNull(message = "{validation.organization.creation.superAdminRequest.required}")
    @Valid
    OrganizationSuperAdminRequest superAdminRequest
) {}
