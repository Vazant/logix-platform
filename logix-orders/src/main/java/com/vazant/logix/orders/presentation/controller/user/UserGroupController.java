package com.vazant.logix.orders.presentation.controller.user;

import com.vazant.logix.orders.application.service.common.CrudService;
import com.vazant.logix.orders.domain.user.UserGroup;
import com.vazant.logix.orders.dto.user.UserGroupResponsibilityRequest;
import com.vazant.logix.orders.presentation.controller.common.BaseController;
import com.vazant.logix.orders.presentation.validation.ValidUuid;
import java.util.List;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing user groups.
 * <p>
 * Provides CRUD operations and group-specific endpoints like adding responsibilities.
 * Extends BaseController to inherit common CRUD operations and validation.
 */
@RestController
@RequestMapping("/api/groups")
public class UserGroupController extends BaseController<UserGroup> {

  public UserGroupController(CrudService<UserGroup> userGroupService) {
    super(userGroupService);
  }

  /**
   * Add a responsibility to a user group.
   *
   * @param groupId the group UUID
   * @param request the responsibility request
   * @return the updated group
   */
  @PostMapping("/{groupId}/responsibilities")
  public ResponseEntity<UserGroup> addResponsibility(
      @PathVariable @ValidUuid String groupId, 
      @Valid @RequestBody UserGroupResponsibilityRequest request) {
    // TODO: Implement responsibility addition
    // This would require adding logic to UserGroupService
    return ResponseEntity.ok().build();
  }

  /**
   * Find groups by organization.
   *
   * @param organizationId the organization UUID
   * @return list of groups in the organization
   */
  @GetMapping("/organization/{organizationId}")
  public ResponseEntity<List<UserGroup>> findByOrganization(@PathVariable @ValidUuid String organizationId) {
    // TODO: Implement group search by organization
    return ResponseEntity.ok(List.of());
  }
}
