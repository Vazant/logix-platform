package com.vazant.logix.orders.dto.user;

import jakarta.validation.constraints.NotBlank;

public record AccountActivationRequest(
    @NotBlank(message = "Token is required") String token,
    @NotBlank(message = "New password is required") String newPassword
) {} 