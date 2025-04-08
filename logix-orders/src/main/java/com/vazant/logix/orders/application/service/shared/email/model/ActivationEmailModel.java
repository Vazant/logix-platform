package com.vazant.logix.orders.application.service.shared.email.model;

public record ActivationEmailModel(String username, String tempPassword, String activationLink) {}
