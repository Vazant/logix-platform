package com.vazant.logix.orders.dto.user;

import jakarta.validation.constraints.NotBlank;

/**
 * Request DTO for account activation.
 * <p>
 * Contains the activation token and the new password to set.
 *
 * @param token the activation token
 * @param newPassword the new password to set
 */
public record AccountActivationRequest(
    @NotBlank(message = "{validation.activation.token.required}")
    String token,
    
    @NotBlank(message = "{validation.activation.password.required}")
    String newPassword
) {} 