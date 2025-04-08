package com.vazant.logix.orders.presentation.controller.organization;

import com.vazant.logix.orders.application.service.organization.OrganizationService;
import com.vazant.logix.orders.domain.organization.Organization;
import com.vazant.logix.orders.dto.organization.OrganizationCreationRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/organizations")
public class OrganizationController {

  private final OrganizationService organizationService;

  public OrganizationController(OrganizationService organizationService) {
    this.organizationService = organizationService;
  }

  @PostMapping
  public ResponseEntity<Organization> createOrganization(
      @RequestBody OrganizationCreationRequest request) {
    Organization organization =
        organizationService.createWithSuperAdmin(
            request.organizationRequest(), request.organizationSuperAdminRequest());
    return ResponseEntity.ok(organization);
  }
}
