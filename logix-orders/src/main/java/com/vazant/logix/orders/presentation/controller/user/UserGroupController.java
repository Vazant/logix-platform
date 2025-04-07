package com.vazant.logix.orders.presentation.controller.user;

import com.vazant.logix.orders.application.service.user.UserGroupService;
import com.vazant.logix.orders.domain.user.UserGroup;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/groups")
public class UserGroupController {

  private final UserGroupService userGroupService;

  public UserGroupController(UserGroupService userGroupService) {
    this.userGroupService = userGroupService;
  }

  @GetMapping
  public ResponseEntity<List<UserGroup>> getAllGroups() {
    List<UserGroup> groups = userGroupService.findAll();
    return ResponseEntity.ok(groups);
  }

  @GetMapping("/{groupUuid}")
  public ResponseEntity<UserGroup> getGroup(@PathVariable String groupUuid) {
    UserGroup group = userGroupService.findByUuid(groupUuid);
    return ResponseEntity.ok(group);
  }

  @PostMapping
  public ResponseEntity<UserGroup> createGroup(@RequestBody UserGroup group) {
    UserGroup created = userGroupService.create(group);
    return ResponseEntity.ok(created);
  }

  @PutMapping("/{groupUuid}")
  public ResponseEntity<UserGroup> updateGroup(
      @PathVariable String groupUuid, @RequestBody UserGroup group) {
    UserGroup updated = userGroupService.update(groupUuid, group);
    return ResponseEntity.ok(updated);
  }

  @DeleteMapping("/{groupUuid}")
  public ResponseEntity<Void> deleteGroup(@PathVariable String groupUuid) {
    userGroupService.delete(groupUuid);
    return ResponseEntity.noContent().build();
  }

  // Пример эндпоинта для добавления обязанности в группу
  @PostMapping("/{groupUuid}/responsibilities")
  public ResponseEntity<UserGroup> addResponsibility(
      @PathVariable String groupUuid, @RequestBody UserGroupResponsibilityRequest request) {
    // UserGroupResponsibilityRequest — DTO, содержащий данные для создания или поиска обязанности
    // Здесь предполагается, что вы реализуете логику конвертации DTO в UserResponsibility
    // Например:
    // UserResponsibility responsibility = convertToUserResponsibility(request);
    // Для простоты примера, пусть request уже является объектом UserResponsibility
    UserGroup updatedGroup =
        userGroupService.addResponsibility(groupUuid, request.getResponsibility());
    return ResponseEntity.ok(updatedGroup);
  }

  // Аналогичный эндпоинт можно сделать для удаления обязанности или добавления/удаления
  // пользователей
}
