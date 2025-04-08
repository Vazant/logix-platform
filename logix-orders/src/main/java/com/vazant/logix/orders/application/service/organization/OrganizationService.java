package com.vazant.logix.orders.application.service.organization;

import com.vazant.logix.orders.application.service.common.AbstractCrudService;
import com.vazant.logix.orders.application.service.shared.email.EmailService;
import com.vazant.logix.orders.domain.organization.Organization;
import com.vazant.logix.orders.domain.user.User;
import com.vazant.logix.orders.dto.organization.OrganizationRequest;
import com.vazant.logix.orders.dto.organization.OrganizationSuperAdminRequest;
import com.vazant.logix.orders.infrastructure.repository.organization.OrganizationRepository;
import jakarta.transaction.Transactional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class OrganizationService extends AbstractCrudService<Organization> {

  private final OrganizationRepository organizationRepository;
  private final UserService userService;
  private final EmailService emailService;

  public OrganizationService(
      OrganizationRepository organizationRepository,
      UserService userService,
      EmailService emailService) {
    super(Organization.class);
    this.organizationRepository = organizationRepository;
    this.userService = userService;
    this.emailService = emailService;
  }

  @Override
  protected JpaRepository<Organization, UUID> getRepository() {
    return organizationRepository;
  }

  @Transactional
  public Organization createWithSuperAdmin(
      OrganizationRequest orgRequest, OrganizationSuperAdminRequest adminRequest) {
    Organization organization =
        new Organization(
            orgRequest.name(), orgRequest.email(), orgRequest.address(), orgRequest.phoneNumber());

    Organization savedOrg = create(organization); // из AbstractCrudService

    String tempPassword = userService.generateTemporaryPassword();

    User superAdmin =
        new User(
            adminRequest.username(),
            adminRequest.email(),
            userService.encodePassword(tempPassword),
            null,
            null);

    superAdmin.setOrganization(savedOrg);
    superAdmin.addRole(UserRole.ORG_SUPER_ADMIN);

    userService.createUser(
        superAdmin); // ты можешь в нём использовать тоже AbstractCrudService<User>

    emailService.sendEmail(superAdmin.getEmail(), tempPassword);

    return savedOrg;
  }
}
