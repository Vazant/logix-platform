package com.vazant.logix.orders.domain.user;

public enum SystemResponsibility {
  ORG_SUPER_ADMIN("role.org.super_admin"),
  MANAGER("role.manager"),
  SUPPORT("role.support"),
  USER("role.user");

  private final String i18nKey;

  SystemResponsibility(String i18nKey) {
    this.i18nKey = i18nKey;
  }

  public String getI18nKey() {
    return i18nKey;
  }

  public String getName() {
    return name();
  }
}
