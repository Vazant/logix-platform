package com.vazant.logix.orders.infrastructure.repository;

import com.vazant.logix.orders.domain.organization.Organization;
import com.vazant.logix.orders.domain.user.ActivationToken;
import com.vazant.logix.orders.domain.user.Person;
import com.vazant.logix.orders.domain.user.User;
import com.vazant.logix.orders.infrastructure.repository.organization.OrganizationRepository;
import com.vazant.logix.orders.infrastructure.repository.user.ActivationTokenRepository;
import com.vazant.logix.orders.infrastructure.repository.user.PersonRepository;
import com.vazant.logix.orders.infrastructure.repository.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class ActivationTokenRepositoryIntegrationTest {

  @Autowired
  private ActivationTokenRepository activationTokenRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PersonRepository personRepository;

  @Autowired
  private OrganizationRepository organizationRepository;

  @Autowired
  private TestEntityManager entityManager;

  private Organization organization;
  private Person person1;
  private Person person2;
  private User user1;
  private User user2;
  private ActivationToken token1;
  private ActivationToken token2;
  private ActivationToken expiredToken;
  private ActivationToken usedToken;

  @BeforeEach
  void setUp() {
    activationTokenRepository.deleteAll();
    userRepository.deleteAll();
    personRepository.deleteAll();
    organizationRepository.deleteAll();

    // Create test data
    organization = new Organization("Test Organization", "test@example.com", "Test Address", "+1234567890");
    organizationRepository.save(organization);

    person1 = new Person();
    person1.setFirstName("John");
    person1.setLastName("Doe");
    personRepository.save(person1);

    person2 = new Person();
    person2.setFirstName("Jane");
    person2.setLastName("Smith");
    personRepository.save(person2);

    user1 = new User("john.doe", "john@example.com", "password123", person1, null);
    user1.setOrganization(organization);
    userRepository.save(user1);

    user2 = new User("jane.smith", "jane@example.com", "password456", person2, null);
    user2.setOrganization(organization);
    userRepository.save(user2);

    // Create tokens
    token1 = new ActivationToken();
    token1.setToken("valid-token-1");
    token1.setUser(user1);
    token1.setExpiresAt(LocalDateTime.now().plusHours(24));
    token1.setUsed(false);

    token2 = new ActivationToken();
    token2.setToken("valid-token-2");
    token2.setUser(user2);
    token2.setExpiresAt(LocalDateTime.now().plusHours(24));
    token2.setUsed(false);

    expiredToken = new ActivationToken();
    expiredToken.setToken("expired-token");
    expiredToken.setUser(user1);
    expiredToken.setExpiresAt(LocalDateTime.now().minusHours(1));
    expiredToken.setUsed(false);

    usedToken = new ActivationToken();
    usedToken.setToken("used-token");
    usedToken.setUser(user2);
    usedToken.setExpiresAt(LocalDateTime.now().plusHours(24));
    usedToken.setUsed(true);

    activationTokenRepository.saveAll(List.of(token1, token2, expiredToken, usedToken));
    entityManager.flush();
    entityManager.clear();
  }

  @Test
  void shouldFindByToken() {
    // When
    Optional<ActivationToken> found = activationTokenRepository.findByToken("valid-token-1");

    // Then
    assertThat(found).isPresent();
    assertThat(found.get().getToken()).isEqualTo("valid-token-1");
    assertThat(found.get().getUser().getUsername()).isEqualTo("john.doe");
    assertThat(found.get().isUsed()).isFalse();
  }

  @Test
  void shouldReturnEmptyWhenTokenNotFound() {
    // When
    Optional<ActivationToken> found = activationTokenRepository.findByToken("nonexistent-token");

    // Then
    assertThat(found).isEmpty();
  }

  @Test
  void shouldFindByTokenAndUsedFalse() {
    // When
    Optional<ActivationToken> validToken = activationTokenRepository.findByTokenAndUsedFalse("valid-token-1");
    Optional<ActivationToken> usedToken = activationTokenRepository.findByTokenAndUsedFalse("used-token");
    Optional<ActivationToken> expiredToken = activationTokenRepository.findByTokenAndUsedFalse("expired-token");

    // Then
    assertThat(validToken).isPresent();
    assertThat(validToken.get().isUsed()).isFalse();
    
    assertThat(usedToken).isEmpty(); // Used token should not be found
    
    assertThat(expiredToken).isPresent(); // Expired but unused token should be found
    assertThat(expiredToken.get().isUsed()).isFalse();
  }

  @Test
  void shouldReturnEmptyWhenTokenUsed() {
    // When
    Optional<ActivationToken> found = activationTokenRepository.findByTokenAndUsedFalse("used-token");

    // Then
    assertThat(found).isEmpty();
  }

  @Test
  void shouldDeleteByUserId() {
    // Given
    User user = userRepository.findByUsername("john.doe").orElseThrow();
    UUID userId = user.getUuid();

    // Verify tokens exist before deletion
    List<ActivationToken> tokensBefore = activationTokenRepository.findAll();
    assertThat(tokensBefore).anyMatch(t -> t.getUser().getUuid().equals(userId));

    // When
    activationTokenRepository.deleteByUserId(userId);

    // Then
    List<ActivationToken> tokensAfter = activationTokenRepository.findAll();
    assertThat(tokensAfter).noneMatch(t -> t.getUser().getUuid().equals(userId));
    
    // Verify other tokens still exist
    assertThat(tokensAfter).anyMatch(t -> t.getUser().getUuid().equals(user2.getUuid()));
  }

  @Test
  void shouldDeleteExpiredTokens() {
    // Given - verify expired token exists
    List<ActivationToken> tokensBefore = activationTokenRepository.findAll();
    assertThat(tokensBefore).anyMatch(t -> t.getToken().equals("expired-token"));

    // When
    activationTokenRepository.deleteExpiredTokens();

    // Then
    List<ActivationToken> tokensAfter = activationTokenRepository.findAll();
    assertThat(tokensAfter).noneMatch(t -> t.getToken().equals("expired-token"));
    
    // Verify valid tokens still exist
    assertThat(tokensAfter).anyMatch(t -> t.getToken().equals("valid-token-1"));
    assertThat(tokensAfter).anyMatch(t -> t.getToken().equals("valid-token-2"));
    assertThat(tokensAfter).anyMatch(t -> t.getToken().equals("used-token"));
  }

  @Test
  void shouldFindAllTokens() {
    // When
    List<ActivationToken> allTokens = activationTokenRepository.findAll();

    // Then
    assertThat(allTokens).hasSize(4);
    assertThat(allTokens).extracting("token")
        .containsExactlyInAnyOrder("valid-token-1", "valid-token-2", "expired-token", "used-token");
  }

  @Test
  void shouldSaveToken() {
    // Given
    User user = userRepository.findByUsername("john.doe").orElseThrow();
    ActivationToken newToken = new ActivationToken();
    newToken.setToken("new-token");
    newToken.setUser(user);
    newToken.setExpiresAt(LocalDateTime.now().plusHours(24));
    newToken.setUsed(false);

    // When
    ActivationToken saved = activationTokenRepository.save(newToken);

    // Then
    assertThat(saved).isNotNull();
    assertThat(saved.getUuid()).isNotNull();
    assertThat(saved.getToken()).isEqualTo("new-token");
    assertThat(saved.getUser().getUuid()).isEqualTo(user.getUuid());
    assertThat(saved.isUsed()).isFalse();
  }

  @Test
  void shouldUpdateToken() {
    // Given
    ActivationToken token = activationTokenRepository.findByToken("valid-token-1").orElseThrow();
    token.setUsed(true);

    // When
    ActivationToken updated = activationTokenRepository.save(token);

    // Then
    assertThat(updated.isUsed()).isTrue();
    
    // Verify in database
    ActivationToken found = activationTokenRepository.findByToken("valid-token-1").orElseThrow();
    assertThat(found.isUsed()).isTrue();
  }

  @Test
  void shouldDeleteToken() {
    // Given
    ActivationToken token = activationTokenRepository.findByToken("valid-token-1").orElseThrow();
    UUID tokenUuid = token.getUuid();

    // When
    activationTokenRepository.deleteById(tokenUuid);

    // Then
    assertThat(activationTokenRepository.findById(tokenUuid)).isEmpty();
    assertThat(activationTokenRepository.findByToken("valid-token-1")).isEmpty();
    assertThat(activationTokenRepository.count()).isEqualTo(3);
  }

  @Test
  void shouldCountTokens() {
    // When
    long count = activationTokenRepository.count();

    // Then
    assertThat(count).isEqualTo(4);
  }

  @Test
  void shouldCheckIfTokenExists() {
    // Given
    ActivationToken token = activationTokenRepository.findByToken("valid-token-1").orElseThrow();

    // When
    boolean exists = activationTokenRepository.existsById(token.getUuid());

    // Then
    assertThat(exists).isTrue();
  }

  @Test
  void shouldCheckIfTokenNotExists() {
    // When
    boolean exists = activationTokenRepository.existsById(UUID.randomUUID());

    // Then
    assertThat(exists).isFalse();
  }

  @Test
  void shouldFindById() {
    // Given
    ActivationToken token = activationTokenRepository.findByToken("valid-token-1").orElseThrow();

    // When
    ActivationToken found = activationTokenRepository.findById(token.getUuid()).orElseThrow();

    // Then
    assertThat(found.getUuid()).isEqualTo(token.getUuid());
    assertThat(found.getToken()).isEqualTo("valid-token-1");
  }

  @Test
  void shouldReturnEmptyWhenIdNotFound() {
    // When
    Optional<ActivationToken> found = activationTokenRepository.findById(UUID.randomUUID());

    // Then
    assertThat(found).isEmpty();
  }

  @Test
  void shouldHandleTokenWithNullExpiration() {
    // Given
    User user = userRepository.findByUsername("john.doe").orElseThrow();
    ActivationToken token = new ActivationToken();
    token.setToken("null-expiration-token");
    token.setUser(user);
    token.setExpiresAt(null);
    token.setUsed(false);

    // When
    ActivationToken saved = activationTokenRepository.save(token);

    // Then
    assertThat(saved.getExpiresAt()).isNull();
    assertThat(saved.getToken()).isEqualTo("null-expiration-token");
  }

  @Test
  void shouldHandleMultipleTokensForSameUser() {
    // Given
    User user = userRepository.findByUsername("john.doe").orElseThrow();
    ActivationToken token3 = new ActivationToken();
    token3.setToken("valid-token-3");
    token3.setUser(user);
    token3.setExpiresAt(LocalDateTime.now().plusHours(24));
    token3.setUsed(false);

    activationTokenRepository.save(token3);

    // When
    activationTokenRepository.deleteByUserId(user.getUuid());

    // Then
    List<ActivationToken> remainingTokens = activationTokenRepository.findAll();
    assertThat(remainingTokens).noneMatch(t -> t.getUser().getUuid().equals(user.getUuid()));
    assertThat(remainingTokens).hasSize(2); // Only tokens for user2 should remain
  }

  @Test
  void shouldHandleTokensWithDifferentExpirationTimes() {
    // Given
    User user = userRepository.findByUsername("john.doe").orElseThrow();
    
    ActivationToken token1 = new ActivationToken();
    token1.setToken("token-expires-soon");
    token1.setUser(user);
    token1.setExpiresAt(LocalDateTime.now().plusMinutes(30));
    token1.setUsed(false);

    ActivationToken token2 = new ActivationToken();
    token2.setToken("token-expires-later");
    token2.setUser(user);
    token2.setExpiresAt(LocalDateTime.now().plusDays(7));
    token2.setUsed(false);

    activationTokenRepository.saveAll(List.of(token1, token2));

    // When
    activationTokenRepository.deleteExpiredTokens();

    // Then - both tokens should still exist as they're not expired
    List<ActivationToken> remainingTokens = activationTokenRepository.findAll();
    assertThat(remainingTokens).anyMatch(t -> t.getToken().equals("token-expires-soon"));
    assertThat(remainingTokens).anyMatch(t -> t.getToken().equals("token-expires-later"));
  }
} 