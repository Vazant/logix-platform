package com.vazant.logix.orders.application.service.user;

import com.vazant.logix.orders.application.service.common.AbstractCrudService;
import com.vazant.logix.orders.domain.organization.Organization;
import com.vazant.logix.orders.domain.user.*;
import com.vazant.logix.orders.dto.organization.OrganizationSuperAdminRequest;
import com.vazant.logix.orders.infrastructure.repository.user.PersonRepository;
import com.vazant.logix.orders.infrastructure.repository.user.UserGroupRepository;
import com.vazant.logix.orders.infrastructure.repository.user.UserRepository;
import com.vazant.logix.orders.infrastructure.repository.user.UserResponsibilityRepository;
import jakarta.transaction.Transactional;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService extends AbstractCrudService<User> {

  private final UserRepository userRepository;
  private final UserGroupRepository groupRepository;
  private final UserResponsibilityRepository responsibilityRepository;
  private final PersonRepository personRepository;
  private final PasswordEncoder passwordEncoder;

  public UserService(
      UserRepository userRepository,
      UserGroupRepository groupRepository,
      UserResponsibilityRepository responsibilityRepository,
      PersonRepository personRepository,
      PasswordEncoder passwordEncoder) {
    super(User.class);
    this.userRepository = userRepository;
    this.groupRepository = groupRepository;
    this.responsibilityRepository = responsibilityRepository;
    this.personRepository = personRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public String generateTemporaryPassword() {
    String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    StringBuilder sb = new StringBuilder();
    ThreadLocalRandom random = ThreadLocalRandom.current();
    for (int i = 0; i < 12; i++) {
      sb.append(chars.charAt(random.nextInt(chars.length())));
    }
    return sb.toString();
  }

  public String encodePassword(String rawPassword) {
    return passwordEncoder.encode(rawPassword);
  }

  @Transactional
  public User createUser(User user) {
    if (user.getPerson() != null && user.getPerson().getUuid() == null) {
      personRepository.save(user.getPerson());
    }
    return userRepository.save(user);
  }

  @Transactional
  public void addUserToGroup(User user, UserGroupName groupName) {
    UserGroup group = getOrCreateGroupWithResponsibilities(groupName);
    user.addGroup(group);
    createUser(user);
  }

  public UserGroup getOrCreateGroupWithResponsibilities(UserGroupName groupName) {
    return groupRepository
        .findByGroupName(groupName)
        .orElseGet(
            () -> {
              Set<UserResponsibility> responsibilities = resolveUserResponsibilities(groupName);
              UserGroup group =
                  UserGroupBuilder.userGroup()
                      .groupName(groupName)
                      .email("group_" + groupName.name().toLowerCase() + "@system.local")
                      .organization("System")
                      .description("System-defined group: " + groupName.name())
                      .build();
              group.setResponsibilities(responsibilities);
              return groupRepository.save(group);
            });
  }

  private Set<UserResponsibility> resolveUserResponsibilities(UserGroupName groupName) {
    return resolveResponsibilities(groupName).stream()
        .map(
            r ->
                responsibilityRepository
                    .findByResponsibility(r)
                    .orElseGet(() -> responsibilityRepository.save(new UserResponsibility(r))))
        .collect(Collectors.toSet());
  }

  @Transactional
  public User createOrgSuperAdmin(
      Organization organization, OrganizationSuperAdminRequest adminRequest, String rawPassword) {

    User user =
        UserBuilder.user()
            .username(adminRequest.username())
            .email(adminRequest.email())
            .password(encodePassword(rawPassword))
            .person(null)
            .pictureUrl(null)
            .build();

    user.setOrganization(organization);
    user.setEnabled(true);

    UserGroup group = getOrCreateOrgSuperAdminGroup(organization.getName());
    group.addUser(user);
    user.getGroups().add(group);

    return userRepository.save(user);
  }

  private UserGroup getOrCreateOrgSuperAdminGroup(String organizationName) {
    return groupRepository
        .findByGroupNameAndOrganization(UserGroupName.ORG_SUPER_ADMIN_GROUP, organizationName)
        .orElseGet(
            () -> {
              Set<UserResponsibility> responsibilities =
                  resolveUserResponsibilities(UserGroupName.ORG_SUPER_ADMIN_GROUP);

              UserGroup group =
                  UserGroupBuilder.userGroup()
                      .groupName(UserGroupName.ORG_SUPER_ADMIN_GROUP)
                      .email("admin@" + organizationName.toLowerCase() + ".com")
                      .organization(organizationName)
                      .description("Group for Org Super Admins of " + organizationName)
                      .build();

              group.setResponsibilities(responsibilities);
              return groupRepository.save(group);
            });
  }

  private Set<SystemResponsibility> resolveResponsibilities(UserGroupName groupName) {
    return switch (groupName) {
      case ORG_SUPER_ADMIN_GROUP -> Set.of(SystemResponsibility.ORG_SUPER_ADMIN);
      case MANAGERS_GROUP -> Set.of(SystemResponsibility.MANAGER);
      case USERS_GROUP -> Set.of(SystemResponsibility.USER);
    };
  }

  @Override
  protected JpaRepository<User, UUID> getRepository() {
    return userRepository;
  }
}
