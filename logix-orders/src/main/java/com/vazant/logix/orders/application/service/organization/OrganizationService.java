package com.vazant.logix.orders.application.service.organization;

import com.vazant.logix.orders.application.service.common.AbstractCrudService;
import com.vazant.logix.orders.application.service.shared.email.EmailService;
import com.vazant.logix.orders.application.service.shared.email.model.ActivationEmailModel;
import com.vazant.logix.orders.application.service.user.AccountActivationService;
import com.vazant.logix.orders.application.service.user.UserService;
import com.vazant.logix.orders.domain.organization.Organization;
import com.vazant.logix.orders.domain.user.User;
import com.vazant.logix.orders.dto.organization.OrganizationRequest;
import com.vazant.logix.orders.dto.organization.OrganizationSuperAdminRequest;
import com.vazant.logix.orders.infrastructure.repository.organization.OrganizationRepository;
import com.vazant.logix.orders.infrastructure.config.OrdersProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for managing organizations and their super administrators.
 * <p>
 * Provides business logic for creating organizations and associated super admin users,
 * including sending activation emails and handling activation tokens.
 */
@Slf4j
@Service
@Transactional
public class OrganizationService extends AbstractCrudService<Organization> {

  private final UserService userService;
  private final EmailService emailService;
  private final AccountActivationService activationService;
  private final OrdersProperties ordersProperties;
  
  public OrganizationService(
      OrganizationRepository repository,
      UserService userService,
      EmailService emailService,
      AccountActivationService activationService,
      OrdersProperties ordersProperties) {
    super(repository, Organization.class);
    this.userService = userService;
    this.emailService = emailService;
    this.activationService = activationService;
    this.ordersProperties = ordersProperties;
  }

  /**
   * Creates an organization and a super administrator user.
   *
   * @param orgRequest organization data
   * @param adminRequest super admin user data
   * @return the saved organization
   */
  @Transactional
  public Organization createWithSuperAdmin(
      OrganizationRequest orgRequest, OrganizationSuperAdminRequest adminRequest) {

    Organization organization = new Organization(
        orgRequest.name(),
        orgRequest.email(),
        orgRequest.address(),
        orgRequest.phone()
    );

    Organization savedOrg = create(organization);

    String tempPassword = userService.generateTemporaryPassword();

    User superAdmin = userService.createOrgSuperAdmin(savedOrg, adminRequest, tempPassword);
    
    // Создаем токен активации
    String activationToken = activationService.createActivationToken(superAdmin);
    String activationLink = ordersProperties.getActivationBaseUrl() + "/api/account/activate?token=" + activationToken;
    
    emailService.sendEmail(
        superAdmin.getEmail(),
        "Activate your account",
        "email/templates/activationEmail.jte",
        new ActivationEmailModel(superAdmin.getUsername(), tempPassword, activationLink));

    return savedOrg;
  }
}
