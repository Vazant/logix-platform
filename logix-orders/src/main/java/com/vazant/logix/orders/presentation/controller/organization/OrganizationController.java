package com.vazant.logix.orders.presentation.controller.organization;

import com.vazant.logix.orders.application.service.common.CrudService;
import com.vazant.logix.orders.application.service.organization.OrganizationService;
import com.vazant.logix.orders.domain.organization.Organization;
import com.vazant.logix.orders.dto.organization.OrganizationCreationRequest;
import com.vazant.logix.orders.presentation.controller.common.BaseController;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing organizations.
 * <p>
 * Provides CRUD operations and organization-specific endpoints like creation with super admin.
 * Extends BaseController to inherit common CRUD operations and validation.
 */
@RestController
@RequestMapping("/api/organizations")
public class OrganizationController extends BaseController<Organization> {

  private final OrganizationService organizationService;

  public OrganizationController(CrudService<Organization> organizationCrudService, 
                               OrganizationService organizationService) {
    super(organizationCrudService);
    this.organizationService = organizationService;
  }

  /**
   * Creates an organization with a super administrator user.
   *
   * @param request the organization creation request
   * @return the created organization
   */
  @PostMapping("/with-super-admin")
  public ResponseEntity<Organization> createOrganizationWithSuperAdmin(
      @Valid @RequestBody OrganizationCreationRequest request) {
    Organization organization =
        organizationService.createWithSuperAdmin(
            request.organizationRequest(), request.superAdminRequest());
    return ResponseEntity.ok(organization);
  }

  /**
   * Find organizations by name containing the search term.
   *
   * @param name the name to search for
   * @return list of matching organizations
   */
  @GetMapping("/search")
  public ResponseEntity<List<Organization>> searchByName(@RequestParam String name) {
    // TODO: Implement organization search by name
    return ResponseEntity.ok(List.of());
  }
}
