package com.vazant.logix.orders.domain.user;

/**
 * Enumeration of system-level user responsibilities (roles) with associated i18n keys.
 * <p>
 * Used for role-based access control and internationalization.
 */
public enum SystemResponsibility {
  /** Organization super administrator role. */
  ORG_SUPER_ADMIN("role.org.super_admin"),
  /** Administrator role. */
  ADMIN("role.admin"),
  /** Manager role. */
  MANAGER("role.manager"),
  /** Support role. */
  SUPPORT("role.support"),
  /** Regular user role. */
  USER("role.user");

  private final String i18nKey;

  /**
   * Constructs a SystemResponsibility with the specified i18n key.
   *
   * @param i18nKey the internationalization key for the role
   */
  SystemResponsibility(String i18nKey) {
    this.i18nKey = i18nKey;
  }

  /**
   * Returns the i18n key for this responsibility.
   *
   * @return the i18n key
   */
  public String getI18nKey() {
    return i18nKey;
  }

  /**
   * Returns the name of the responsibility (enum constant name).
   *
   * @return the name of the responsibility
   */
  public String getName() {
    return name();
  }
}
