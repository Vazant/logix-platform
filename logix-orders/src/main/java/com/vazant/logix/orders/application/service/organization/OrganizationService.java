package com.vazant.logix.orders.application.service.organization;

import com.vazant.logix.orders.application.service.common.AbstractCrudService;
import com.vazant.logix.orders.application.service.shared.email.EmailService;
import com.vazant.logix.orders.application.service.shared.email.model.ActivationEmailModel;
import com.vazant.logix.orders.application.service.user.AccountActivationService;
import com.vazant.logix.orders.application.service.user.UserService;
import com.vazant.logix.orders.domain.organization.Organization;
import com.vazant.logix.orders.domain.organization.OrganizationBuilder;
import com.vazant.logix.orders.domain.user.User;
import com.vazant.logix.orders.dto.organization.OrganizationRequest;
import com.vazant.logix.orders.dto.organization.OrganizationSuperAdminRequest;
import com.vazant.logix.orders.infrastructure.repository.organization.OrganizationRepository;
import jakarta.transaction.Transactional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class OrganizationService extends AbstractCrudService<Organization> {

  private final OrganizationRepository organizationRepository;
  private final UserService userService;
  private final EmailService emailService;
  private final AccountActivationService activationService;
  
  @Value("${app.activation.base-url:http://localhost:8080}")
  private String activationBaseUrl;

  public OrganizationService(
      OrganizationRepository organizationRepository,
      UserService userService,
      EmailService emailService,
      AccountActivationService activationService) {
    super(Organization.class);
    this.organizationRepository = organizationRepository;
    this.userService = userService;
    this.emailService = emailService;
    this.activationService = activationService;
  }

  @Override
  protected JpaRepository<Organization, UUID> getRepository() {
    return organizationRepository;
  }

  /**
   * Создаёт организацию и супер-администратора.
   *
   * @param orgRequest данные по организации
   * @param adminRequest данные по пользователю
   * @return сохранённая организация
   */
  @Transactional
  public Organization createWithSuperAdmin(
      OrganizationRequest orgRequest, OrganizationSuperAdminRequest adminRequest) {

    Organization organization =
        OrganizationBuilder.organization()
            .name(orgRequest.name())
            .email(orgRequest.email())
            .address(orgRequest.address())
            .phoneNumber(orgRequest.phoneNumber())
            .build();

    Organization savedOrg = create(organization);

    String tempPassword = userService.generateTemporaryPassword();

    User superAdmin = userService.createOrgSuperAdmin(savedOrg, adminRequest, tempPassword);
    
    // Создаем токен активации
    String activationToken = activationService.createActivationToken(superAdmin);
    String activationLink = activationBaseUrl + "/api/account/activate?token=" + activationToken;
    
    emailService.sendEmail(
        superAdmin.getEmail(),
        "Activate your account",
        "email/templates/activationEmail.jte",
        new ActivationEmailModel(superAdmin.getUsername(), tempPassword, activationLink));

    return savedOrg;
  }
}
