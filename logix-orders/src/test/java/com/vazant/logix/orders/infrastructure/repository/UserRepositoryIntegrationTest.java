package com.vazant.logix.orders.infrastructure.repository;

import com.vazant.logix.orders.domain.organization.Organization;
import com.vazant.logix.orders.domain.user.Person;
import com.vazant.logix.orders.domain.user.SystemResponsibility;
import com.vazant.logix.orders.domain.user.User;
import com.vazant.logix.orders.domain.user.UserGroup;
import com.vazant.logix.orders.domain.user.UserGroupName;
import com.vazant.logix.orders.domain.user.UserResponsibility;
import com.vazant.logix.orders.infrastructure.repository.organization.OrganizationRepository;
import com.vazant.logix.orders.infrastructure.repository.user.PersonRepository;
import com.vazant.logix.orders.infrastructure.repository.user.UserGroupRepository;
import com.vazant.logix.orders.infrastructure.repository.user.UserRepository;
import com.vazant.logix.orders.infrastructure.repository.user.UserResponsibilityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryIntegrationTest {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PersonRepository personRepository;

  @Autowired
  private OrganizationRepository organizationRepository;

  @Autowired
  private UserGroupRepository userGroupRepository;

  @Autowired
  private UserResponsibilityRepository userResponsibilityRepository;

  @Autowired
  private TestEntityManager entityManager;

  private Organization organization1;
  private Organization organization2;
  private Person person1;
  private Person person2;
  private Person person3;
  private User user1;
  private User user2;
  private User user3;
  private UserGroup adminGroup;
  private UserGroup userGroup;
  private UserResponsibility adminResponsibility;
  private UserResponsibility userResponsibility;

  @BeforeEach
  void setUp() {
    userRepository.deleteAll();
    personRepository.deleteAll();
    organizationRepository.deleteAll();
    userGroupRepository.deleteAll();
    userResponsibilityRepository.deleteAll();

    // Create organizations
    organization1 = new Organization("Test Org 1", "org1@example.com", "Address 1", "+1234567890");
    organization2 = new Organization("Test Org 2", "org2@example.com", "Address 2", "+1234567891");
    organizationRepository.saveAll(List.of(organization1, organization2));

    // Create persons
    person1 = new Person();
    person1.setFirstName("John");
    person1.setLastName("Doe");

    person2 = new Person();
    person2.setFirstName("Jane");
    person2.setLastName("Smith");

    person3 = new Person();
    person3.setFirstName("Bob");
    person3.setLastName("Johnson");

    personRepository.saveAll(List.of(person1, person2, person3));

    // Create responsibilities
    adminResponsibility = new UserResponsibility(SystemResponsibility.ADMIN);
    userResponsibility = new UserResponsibility(SystemResponsibility.USER);
    userResponsibilityRepository.saveAll(List.of(adminResponsibility, userResponsibility));

    // Create groups
    adminGroup = new UserGroup(UserGroupName.ADMIN, "admin@example.com", organization1.getName(), "Admin group");
    userGroup = new UserGroup(UserGroupName.USER, "user@example.com", organization1.getName(), "User group");
    adminGroup.addResponsibility(adminResponsibility);
    userGroup.addResponsibility(userResponsibility);
    userGroupRepository.saveAll(List.of(adminGroup, userGroup));

    // Create users
    user1 = new User("john.doe", "john@example.com", "password123", person1, null);
    user1.setOrganization(organization1);
    user1.addGroup(adminGroup);

    user2 = new User("jane.smith", "jane@example.com", "password456", person2, null);
    user2.setOrganization(organization1);
    user2.addGroup(userGroup);

    user3 = new User("bob.johnson", "bob@example.com", "password789", person3, null);
    user3.setOrganization(organization2);
    user3.addGroup(userGroup);

    userRepository.saveAll(List.of(user1, user2, user3));
    entityManager.flush();
    entityManager.clear();
  }

  @Test
  void shouldFindByUsername() {
    // When
    var found = userRepository.findByUsername("john.doe");

    // Then
    assertThat(found).isPresent();
    assertThat(found.get().getUsername()).isEqualTo("john.doe");
    assertThat(found.get().getEmail()).isEqualTo("john@example.com");
    assertThat(found.get().getPerson().getFirstName()).isEqualTo("John");
  }

  @Test
  void shouldReturnEmptyWhenUsernameNotFound() {
    // When
    var found = userRepository.findByUsername("nonexistent");

    // Then
    assertThat(found).isEmpty();
  }

  @Test
  void shouldFindByOrganizationUuid() {
    // When
    List<User> found = userRepository.findByOrganizationUuid(organization1.getUuid());

    // Then
    assertThat(found).hasSize(2);
    assertThat(found).allMatch(u -> u.getOrganization().getUuid().equals(organization1.getUuid()));
    assertThat(found).extracting("username").containsExactlyInAnyOrder("john.doe", "jane.smith");
  }

  @Test
  void shouldReturnEmptyListWhenOrganizationNotFound() {
    // When
    List<User> found = userRepository.findByOrganizationUuid(UUID.randomUUID());

    // Then
    assertThat(found).isEmpty();
  }

  @Test
  void shouldFindByResponsibility() {
    // When
    List<User> adminUsers = userRepository.findByResponsibility(SystemResponsibility.ADMIN);
    List<User> regularUsers = userRepository.findByResponsibility(SystemResponsibility.USER);

    // Then
    assertThat(adminUsers).hasSize(1);
    assertThat(adminUsers.get(0).getUsername()).isEqualTo("john.doe");
    
    assertThat(regularUsers).hasSize(2);
    assertThat(regularUsers).extracting("username").containsExactlyInAnyOrder("jane.smith", "bob.johnson");
  }

  @Test
  void shouldCheckHasResponsibility() {
    // Given
    User adminUser = userRepository.findByUsername("john.doe").orElseThrow();
    User regularUser = userRepository.findByUsername("jane.smith").orElseThrow();

    // When
    boolean adminHasAdminResponsibility = userRepository.hasResponsibility(adminUser.getUuid(), SystemResponsibility.ADMIN);
    boolean adminHasUserResponsibility = userRepository.hasResponsibility(adminUser.getUuid(), SystemResponsibility.USER);
    boolean userHasAdminResponsibility = userRepository.hasResponsibility(regularUser.getUuid(), SystemResponsibility.ADMIN);
    boolean userHasUserResponsibility = userRepository.hasResponsibility(regularUser.getUuid(), SystemResponsibility.USER);

    // Then
    assertThat(adminHasAdminResponsibility).isTrue();
    assertThat(adminHasUserResponsibility).isFalse();
    assertThat(userHasAdminResponsibility).isFalse();
    assertThat(userHasUserResponsibility).isTrue();
  }

  @Test
  void shouldReturnFalseForNonExistentUser() {
    // When
    boolean hasResponsibility = userRepository.hasResponsibility(UUID.randomUUID(), SystemResponsibility.ADMIN);

    // Then
    assertThat(hasResponsibility).isFalse();
  }

  @Test
  void shouldFindAllUsers() {
    // When
    List<User> allUsers = userRepository.findAll();

    // Then
    assertThat(allUsers).hasSize(3);
    assertThat(allUsers).extracting("username")
        .containsExactlyInAnyOrder("john.doe", "jane.smith", "bob.johnson");
  }

  @Test
  void shouldSaveUser() {
    // Given
    Person newPerson = new Person();
    newPerson.setFirstName("Alice");
    newPerson.setLastName("Brown");
    personRepository.save(newPerson);

    User newUser = new User("alice.brown", "alice@example.com", "password123", newPerson, null);
    newUser.setOrganization(organization1);

    // When
    User saved = userRepository.save(newUser);

    // Then
    assertThat(saved).isNotNull();
    assertThat(saved.getUuid()).isNotNull();
    assertThat(saved.getUsername()).isEqualTo("alice.brown");
    assertThat(saved.getEmail()).isEqualTo("alice@example.com");
    assertThat(saved.getPerson().getFirstName()).isEqualTo("Alice");
  }

  @Test
  void shouldUpdateUser() {
    // Given
    User user = userRepository.findByUsername("john.doe").orElseThrow();
    user.setEmail("john.updated@example.com");
    user.getPerson().setFirstName("Jonathan");

    // When
    User updated = userRepository.save(user);

    // Then
    assertThat(updated.getEmail()).isEqualTo("john.updated@example.com");
    assertThat(updated.getPerson().getFirstName()).isEqualTo("Jonathan");
    
    // Verify in database
    User found = userRepository.findByUsername("john.doe").orElseThrow();
    assertThat(found.getEmail()).isEqualTo("john.updated@example.com");
    assertThat(found.getPerson().getFirstName()).isEqualTo("Jonathan");
  }

  @Test
  void shouldDeleteUser() {
    // Given
    User user = userRepository.findByUsername("john.doe").orElseThrow();
    UUID userUuid = user.getUuid();

    // When
    userRepository.deleteById(userUuid);

    // Then
    assertThat(userRepository.findById(userUuid)).isEmpty();
    assertThat(userRepository.findByUsername("john.doe")).isEmpty();
    assertThat(userRepository.count()).isEqualTo(2);
  }

  @Test
  void shouldCountUsers() {
    // When
    long count = userRepository.count();

    // Then
    assertThat(count).isEqualTo(3);
  }

  @Test
  void shouldCheckIfUserExists() {
    // Given
    User user = userRepository.findByUsername("john.doe").orElseThrow();

    // When
    boolean exists = userRepository.existsById(user.getUuid());

    // Then
    assertThat(exists).isTrue();
  }

  @Test
  void shouldCheckIfUserNotExists() {
    // When
    boolean exists = userRepository.existsById(UUID.randomUUID());

    // Then
    assertThat(exists).isFalse();
  }

  @Test
  void shouldFindById() {
    // Given
    User user = userRepository.findByUsername("john.doe").orElseThrow();

    // When
    User found = userRepository.findById(user.getUuid()).orElseThrow();

    // Then
    assertThat(found.getUuid()).isEqualTo(user.getUuid());
    assertThat(found.getUsername()).isEqualTo("john.doe");
  }

  @Test
  void shouldReturnEmptyWhenIdNotFound() {
    // When
    var found = userRepository.findById(UUID.randomUUID());

    // Then
    assertThat(found).isEmpty();
  }

  @Test
  void shouldHandleUserWithMultipleGroups() {
    // Given
    User user = userRepository.findByUsername("jane.smith").orElseThrow();
    user.addGroup(adminGroup);
    userRepository.save(user);

    // When
    boolean hasAdminResponsibility = userRepository.hasResponsibility(user.getUuid(), SystemResponsibility.ADMIN);
    boolean hasUserResponsibility = userRepository.hasResponsibility(user.getUuid(), SystemResponsibility.USER);

    // Then
    assertThat(hasAdminResponsibility).isTrue();
    assertThat(hasUserResponsibility).isTrue();
  }

  @Test
  void shouldHandleDisabledUser() {
    // Given
    User user = userRepository.findByUsername("john.doe").orElseThrow();
    user.setEnabled(false);
    userRepository.save(user);

    // When
    User found = userRepository.findByUsername("john.doe").orElseThrow();

    // Then
    assertThat(found.isEnabled()).isFalse();
  }
} 