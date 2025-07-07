package com.vazant.logix.orders.dto.organization;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for creating a super admin for an organization.
 * <p>
 * Contains the complete user information for the super admin, all validated.
 *
 * @param firstName the first name of the super admin
 * @param lastName the last name of the super admin
 * @param username the username for the super admin
 * @param email the email address for the super admin
 */
public record OrganizationSuperAdminRequest(
    @NotBlank(message = "{validation.organization.superAdmin.firstName.required}")
    @Size(min = 2, max = 50, message = "{validation.organization.superAdmin.firstName.size}")
    String firstName,
    
    @NotBlank(message = "{validation.organization.superAdmin.lastName.required}")
    @Size(min = 2, max = 50, message = "{validation.organization.superAdmin.lastName.size}")
    String lastName,
    
    @NotBlank(message = "{validation.organization.superAdmin.username.required}")
    @Size(min = 3, max = 50, message = "{validation.organization.superAdmin.username.size}")
    @Pattern(regexp = "^[a-zA-Z0-9_-]+$", message = "{validation.organization.superAdmin.username.pattern}")
    String username,
    
    @NotBlank(message = "{validation.organization.superAdmin.email.required}")
    @Email(message = "{validation.organization.superAdmin.email.invalid}")
    @Size(max = 255, message = "{validation.organization.superAdmin.email.size}")
    String email
) {}
