package com.vazant.logix.orders.dto.organization;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Request DTO for organization creation.
 * <p>
 * Contains organization name, email, address, and phone, all validated.
 *
 * @param name the name of the organization
 * @param email the contact email of the organization
 * @param address the address of the organization (optional)
 * @param phone the contact phone number of the organization (optional)
 */
public record OrganizationRequest(
    @NotBlank(message = "{validation.organization.name.required}")
    @Size(min = 2, max = 100, message = "{validation.organization.name.size}")
    String name,
    
    @NotBlank(message = "{validation.organization.email.required}")
    @Email(message = "{validation.organization.email.invalid}")
    @Size(max = 255, message = "{validation.organization.email.size}")
    String email,
    
    @Size(max = 500, message = "{validation.organization.address.size}")
    String address,
    
    @Pattern(regexp = "\\+?[0-9\\- ]{7,20}", message = "{validation.organization.phone.invalid}")
    String phone
) {}
