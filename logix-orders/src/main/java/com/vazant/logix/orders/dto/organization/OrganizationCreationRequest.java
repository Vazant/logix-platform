package com.vazant.logix.orders.dto.organization;

public record OrganizationCreationRequest(
    OrganizationRequest organizationRequest,
    OrganizationSuperAdminRequest organizationSuperAdminRequest) {}
