package com.vazant.logix.orders.application.service.user;

import com.vazant.logix.orders.domain.user.ActivationToken;
import com.vazant.logix.orders.domain.user.User;
import com.vazant.logix.orders.dto.user.AccountActivationRequest;
import com.vazant.logix.orders.infrastructure.repository.user.ActivationTokenRepository;
import com.vazant.logix.orders.infrastructure.repository.user.UserRepository;
import com.vazant.logix.orders.infrastructure.config.OrdersProperties;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

/**
 * Service for handling user account activation.
 * Manages creation, validation, and removal of activation tokens, as well as user activation.
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AccountActivationService {

  private final ActivationTokenRepository activationTokenRepository;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final OrdersProperties ordersProperties;

  /**
   * Activates a user account using the provided token and new password.
   *
   * @param request the activation request containing token and new password
   * @return true if activation was successful, false otherwise
   */
  public boolean activateAccount(AccountActivationRequest request) {
    Assert.notNull(request, "Activation request must not be null");
    Assert.hasText(request.token(), "Token must not be null or empty");
    Assert.hasText(request.newPassword(), "New password must not be null or empty");
    
    log.info("Attempting to activate account with token: {}", request.token());

    ActivationToken token = activationTokenRepository.findByToken(request.token())
        .orElse(null);

    if (token == null || token.getExpiresAt().isBefore(LocalDateTime.now())) {
      log.warn("Invalid or expired activation token: {}", request.token());
      if (token != null) {
        activationTokenRepository.delete(token);
      }
      return false;
    }

    User user = token.getUser();
    user.setPassword(passwordEncoder.encode(request.newPassword()));
    user.setEnabled(true);
    userRepository.save(user);

    activationTokenRepository.delete(token);
    log.info("Successfully activated account for user: {}", user.getUuid());
    return true;
  }

  /**
   * Validates if an activation token is valid and not expired.
   *
   * @param token the activation token to validate
   * @return true if token is valid, false otherwise
   */
  public boolean isValidToken(String token) {
    Assert.hasText(token, "Token must not be null or empty");
    log.debug("Validating activation token: {}", token);

    ActivationToken activationToken = activationTokenRepository.findByToken(token)
        .orElse(null);

    if (activationToken == null) {
      return false;
    }

    boolean isValid = !activationToken.getExpiresAt().isBefore(LocalDateTime.now());

    if (!isValid) {
      log.debug("Token expired, removing: {}", token);
      activationTokenRepository.delete(activationToken);
    }

    return isValid;
  }

  /**
   * Creates and saves an activation token for the user and returns the token string.
   *
   * @param user the user to create token for
   * @return the created token string
   */
  public String createActivationToken(User user) {
    Assert.notNull(user, "User must not be null");
    
    String token = java.util.UUID.randomUUID().toString();
    ActivationToken activationToken = new ActivationToken();
    activationToken.setUser(user);
    activationToken.setToken(token);
    activationToken.setExpiresAt(LocalDateTime.now().plusHours(ordersProperties.getCache().getTtlHours()));
    activationTokenRepository.save(activationToken);
    log.info("Created activation token for user {}: {}", user.getUuid(), token);
    return token;
  }
} 