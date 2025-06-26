package com.vazant.logix.orders.application.service.user;

import com.vazant.logix.orders.domain.user.ActivationToken;
import com.vazant.logix.orders.domain.user.User;
import com.vazant.logix.orders.dto.user.AccountActivationRequest;
import com.vazant.logix.orders.infrastructure.repository.user.ActivationTokenRepository;
import com.vazant.logix.orders.infrastructure.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountActivationService {

  private final ActivationTokenRepository activationTokenRepository;
  private final UserRepository userRepository;
  private final UserService userService;

  /**
   * Создает токен активации для пользователя
   */
  @Transactional
  public String createActivationToken(User user) {
    // Удаляем старые токены для этого пользователя
    activationTokenRepository.deleteByUserId(user.getUuid());
    
    String token = UUID.randomUUID().toString();
    LocalDateTime expiresAt = LocalDateTime.now().plusHours(24); // Токен действителен 24 часа
    
    ActivationToken activationToken = new ActivationToken(token, user, expiresAt);
    activationTokenRepository.save(activationToken);
    
    log.info("Created activation token for user: {}", user.getUsername());
    return token;
  }

  /**
   * Активирует аккаунт пользователя по токену
   */
  @Transactional
  public boolean activateAccount(AccountActivationRequest request) {
    ActivationToken token = activationTokenRepository
        .findByTokenAndUsedFalse(request.token())
        .orElse(null);

    if (token == null) {
      log.warn("Invalid or used activation token: {}", request.token());
      return false;
    }

    if (token.isExpired()) {
      log.warn("Expired activation token: {}", request.token());
      return false;
    }

    User user = token.getUser();
    user.setPassword(userService.encodePassword(request.newPassword()));
    user.setEnabled(true);
    
    token.setUsed(true);
    
    userRepository.save(user);
    activationTokenRepository.save(token);
    
    log.info("Successfully activated account for user: {}", user.getUsername());
    return true;
  }

  /**
   * Проверяет валидность токена активации
   */
  public boolean isValidToken(String token) {
    return activationTokenRepository
        .findByTokenAndUsedFalse(token)
        .map(ActivationToken::isValid)
        .orElse(false);
  }

  /**
   * Очищает просроченные токены
   */
  @Transactional
  public void cleanupExpiredTokens() {
    activationTokenRepository.deleteExpiredTokens();
    log.info("Cleaned up expired activation tokens");
  }
} 