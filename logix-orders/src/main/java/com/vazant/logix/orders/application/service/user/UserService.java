package com.vazant.logix.orders.application.service.user;

import com.vazant.logix.orders.application.service.common.AbstractCrudService;
import com.vazant.logix.orders.domain.organization.Organization;
import com.vazant.logix.orders.domain.user.ActivationToken;
import com.vazant.logix.orders.domain.user.Person;
import com.vazant.logix.orders.domain.user.SystemResponsibility;
import com.vazant.logix.orders.domain.user.User;
import com.vazant.logix.orders.domain.user.UserGroup;
import com.vazant.logix.orders.dto.user.AccountActivationRequest;
import com.vazant.logix.orders.dto.user.UserGroupResponsibilityRequest;
import com.vazant.logix.orders.dto.organization.OrganizationSuperAdminRequest;
import com.vazant.logix.orders.infrastructure.repository.user.ActivationTokenRepository;
import com.vazant.logix.orders.infrastructure.repository.user.PersonRepository;
import com.vazant.logix.orders.infrastructure.repository.user.UserGroupRepository;
import com.vazant.logix.orders.infrastructure.repository.user.UserRepository;
import com.vazant.logix.orders.infrastructure.utils.UuidUtils;
import com.vazant.logix.orders.infrastructure.config.OrdersProperties;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for managing users and user-related operations.
 * <p>
 * Provides business logic for user creation, activation, group assignment, and queries by organization or responsibility.
 */
@Slf4j
@Service
@Transactional
public class UserService extends AbstractCrudService<User> {

  private final PersonRepository personRepository;
  private final UserGroupRepository userGroupRepository;
  private final ActivationTokenRepository activationTokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;
  private final OrdersProperties ordersProperties;

  public UserService(
      UserRepository userRepository,
      PersonRepository personRepository,
      UserGroupRepository userGroupRepository,
      ActivationTokenRepository activationTokenRepository,
      PasswordEncoder passwordEncoder,
      OrdersProperties ordersProperties) {
    super(userRepository, User.class);
    this.userRepository = userRepository;
    this.personRepository = personRepository;
    this.userGroupRepository = userGroupRepository;
    this.activationTokenRepository = activationTokenRepository;
    this.passwordEncoder = passwordEncoder;
    this.ordersProperties = ordersProperties;
  }

  /**
   * Returns the entity name for logging and error messages.
   *
   * @return the entity name
   */
  @Override
  protected String getEntityName() {
    return "User";
  }

  /**
   * Creates a new user with activation token.
   *
   * @param person the person data
   * @param organization the organization
   * @param password the password
   * @return the created user
   */
  public User createUser(Person person, Organization organization, String password) {
    // Create person first
    Person savedPerson = personRepository.save(person);

    // Create user
    User user = new User();
    user.setPerson(savedPerson);
    user.setOrganization(organization);
    user.setEnabled(false);
    user.setPassword(passwordEncoder.encode(password));

    User savedUser = create(user);

    // Create activation token
    ActivationToken token = new ActivationToken();
    token.setUser(savedUser);
    token.setToken(UUID.randomUUID().toString());
    token.setExpiresAt(LocalDateTime.now().plusHours(ordersProperties.getCache().getTtlHours()));
    activationTokenRepository.save(token);

    log.info("Created user with activation token: {}", savedUser.getUuid());
    return savedUser;
  }

  /**
   * Activates a user account using activation token.
   *
   * @param request the activation request
   * @return true if activation successful
   */
  public boolean activateAccount(AccountActivationRequest request) {
    Optional<ActivationToken> tokenOpt = activationTokenRepository.findByToken(request.token());

    if (tokenOpt.isEmpty()) {
      log.warn("Invalid activation token: {}", request.token());
      return false;
    }

    ActivationToken token = tokenOpt.get();
    if (token.getExpiresAt().isBefore(LocalDateTime.now())) {
      log.warn("Expired activation token: {}", request.token());
      activationTokenRepository.delete(token);
      return false;
    }

    User user = token.getUser();
    user.setEnabled(true);
    user.setPassword(passwordEncoder.encode(request.newPassword()));
    update(user.getUuid().toString(), user);

    activationTokenRepository.delete(token);
    log.info("Activated user account: {}", user.getUuid());
    return true;
  }

  /**
   * Assigns user to a group with responsibilities.
   *
   * @param userId the user ID
   * @param request the group responsibility request
   * @return the user group
   */
  public UserGroup assignToGroup(String userId, UserGroupResponsibilityRequest request) {
    User user = findByUuid(userId);
    // Note: This is a simplified implementation - you may need to adjust based on your domain model
    log.info("Assigning user {} to group with responsibility: {}", userId, request.getResponsibility());
    return null; // Placeholder - implement based on your domain model
  }

  /**
   * Finds users by organization.
   *
   * @param organizationId the organization ID
   * @return list of users
   */
  public List<User> findByOrganization(String organizationId) {
    return userRepository.findByOrganizationUuid(UuidUtils.parse(organizationId));
  }

  /**
   * Finds users by responsibility.
   *
   * @param responsibility the responsibility
   * @return list of users
   */
  public List<User> findByResponsibility(SystemResponsibility responsibility) {
    return userRepository.findByResponsibility(responsibility);
  }

  /**
   * Checks if user has specific responsibility.
   *
   * @param userId the user ID
   * @param responsibility the responsibility
   * @return true if user has responsibility
   */
  public boolean hasResponsibility(String userId, SystemResponsibility responsibility) {
    return userRepository.hasResponsibility(UuidUtils.parse(userId), responsibility);
  }

  /**
   * Generates a temporary random password.
   */
  public String generateTemporaryPassword() {
    // Простейшая генерация, можно заменить на более сложную
    return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 12);
  }

  /**
   * Creates a super admin user for the organization.
   */
  public User createOrgSuperAdmin(Organization organization, OrganizationSuperAdminRequest adminRequest, String password) {
    Person person = new Person();
    person.setFirstName(adminRequest.firstName());
    person.setLastName(adminRequest.lastName());
    // Person не имеет поля email - email хранится в User

    User user = new User();
    user.setPerson(person);
    user.setOrganization(organization);
    user.setUsername(adminRequest.username());
    user.setEmail(adminRequest.email());
    user.setPassword(passwordEncoder.encode(password));
    user.setEnabled(true);

    User savedUser = create(user);
    log.info("Created super admin user for organization: {}", savedUser.getUuid());
    return savedUser;
  }
}
