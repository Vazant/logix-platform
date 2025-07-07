package com.vazant.logix.orders.domain.user;

import com.vazant.logix.orders.domain.common.BaseEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entity representing an activation token for user account activation.
 * <p>
 * Stores the token value, associated user, expiration time, and usage status.
 */
@Entity
@Table(name = "activation_tokens")
public class ActivationToken extends BaseEntity {

  @Column(nullable = false, unique = true)
  private String token;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_uuid", nullable = false)
  private User user;

  @Column(nullable = false)
  private LocalDateTime expiresAt;

  @Column(nullable = false)
  private boolean used = false;

  /**
   * Default constructor for JPA.
   */
  public ActivationToken() {}

  /**
   * Constructs a new ActivationToken with the specified token, user, and expiration time.
   *
   * @param token the activation token value
   * @param user the associated user
   * @param expiresAt the expiration date and time
   */
  public ActivationToken(String token, User user, LocalDateTime expiresAt) {
    this.token = token;
    this.user = user;
    this.expiresAt = expiresAt;
  }

  /**
   * Returns the activation token value.
   *
   * @return the token value
   */
  public String getToken() {
    return token;
  }

  /**
   * Sets the activation token value.
   *
   * @param token the token value
   */
  public void setToken(String token) {
    this.token = token;
  }

  /**
   * Returns the associated user.
   *
   * @return the user
   */
  public User getUser() {
    return user;
  }

  /**
   * Sets the associated user.
   *
   * @param user the user
   */
  public void setUser(User user) {
    this.user = user;
  }

  /**
   * Returns the expiration date and time of the token.
   *
   * @return the expiration date and time
   */
  public LocalDateTime getExpiresAt() {
    return expiresAt;
  }

  /**
   * Sets the expiration date and time of the token.
   *
   * @param expiresAt the expiration date and time
   */
  public void setExpiresAt(LocalDateTime expiresAt) {
    this.expiresAt = expiresAt;
  }

  /**
   * Returns whether the token has been used.
   *
   * @return true if used, false otherwise
   */
  public boolean isUsed() {
    return used;
  }

  /**
   * Sets the usage status of the token.
   *
   * @param used true if used, false otherwise
   */
  public void setUsed(boolean used) {
    this.used = used;
  }

  /**
   * Checks if the token is expired based on the current time.
   *
   * @return true if expired, false otherwise
   */
  public boolean isExpired() {
    return LocalDateTime.now().isAfter(expiresAt);
  }

  /**
   * Checks if the token is valid (not used and not expired).
   *
   * @return true if valid, false otherwise
   */
  public boolean isValid() {
    return !used && !isExpired();
  }
} 