package com.vazant.logix.orders.infrastructure.repository.user;

import com.vazant.logix.orders.domain.user.ActivationToken;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ActivationTokenRepository extends JpaRepository<ActivationToken, UUID> {
  
  Optional<ActivationToken> findByToken(String token);
  
  Optional<ActivationToken> findByTokenAndUsedFalse(String token);
  
  @Modifying
  @Query("DELETE FROM ActivationToken at WHERE at.user.uuid = :userId")
  void deleteByUserId(@Param("userId") UUID userId);
  
  @Modifying
  @Query("DELETE FROM ActivationToken at WHERE at.expiresAt < CURRENT_TIMESTAMP")
  void deleteExpiredTokens();
} 