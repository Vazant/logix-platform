package com.vazant.logix.orders.domain.user;

import com.vazant.logix.orders.domain.common.BaseEntity;
import com.vazant.logix.orders.domain.common.Updatable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Сущность группы пользователей. Содержит основные поля, а также коллекции пользователей и
 * обязанностей. Для обновления коллекций используются бизнес-методы.
 */
@Entity
@Table(name = "user_groups")
public class UserGroup extends BaseEntity implements Updatable<UserGroup> {

  @NotBlank private String name;

  @Email @NotBlank private String email; // Контактный email группы

  @Enumerated(EnumType.STRING)
  private GroupType groupType; // Тип группы (например, ADMIN, SUPPORT, CUSTOMER)

  // Поле для организации, к которой принадлежит группа.
  // В будущем можно заменить на связь @ManyToOne с сущностью Organization.
  private String organization;

  // Дополнительное описание группы
  private String description;

  // Список пользователей, принадлежащих группе.
  // Инициализируем для избежания NPE.
  @ManyToMany(mappedBy = "groups")
  private List<User> users = new ArrayList<>();

  // Набор обязанностей (ролей), связанных с группой.
  // Инициализируем для избежания NPE.
  @ManyToMany
  @JoinTable(
      name = "user_responsibilities",
      joinColumns = @JoinColumn(name = "group_uuid"),
      inverseJoinColumns = @JoinColumn(name = "user_responsibility_uuid"))
  private Set<UserResponsibility> responsibilities = new HashSet<>();

  // Геттеры и сеттеры

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public GroupType getGroupType() {
    return groupType;
  }

  public void setGroupType(GroupType groupType) {
    this.groupType = groupType;
  }

  public String getOrganization() {
    return organization;
  }

  public void setOrganization(String organization) {
    this.organization = organization;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public List<User> getUsers() {
    return users;
  }

  public void setUsers(List<User> users) {
    this.users = users;
  }

  public Set<UserResponsibility> getResponsibilities() {
    return responsibilities;
  }

  public void setResponsibilities(Set<UserResponsibility> responsibilities) {
    this.responsibilities = responsibilities;
  }

  /**
   * Добавляет пользователя в группу, если его там ещё нет. Если связь двунаправленная, добавляет
   * группу и у пользователя.
   *
   * @param user пользователь для добавления
   */
  public void addUser(User user) {
    if (user == null) {
      throw new IllegalArgumentException("User must not be null");
    }
    if (!this.users.contains(user)) {
      this.users.add(user);
      // Двунаправленная связь: добавляем группу и к пользователю
      user.getGroups().add(this);
    }
  }

  /**
   * Удаляет пользователя из группы.
   *
   * @param user пользователь для удаления
   */
  public void removeUser(User user) {
    if (user == null) {
      throw new IllegalArgumentException("User must not be null");
    }
    if (this.users.remove(user)) {
      // Двунаправленная связь: удаляем группу у пользователя
      user.getGroups().remove(this);
    }
  }

  /**
   * Добавляет обязанность в группу.
   *
   * @param responsibility обязанность для добавления
   */
  public void addResponsibility(UserResponsibility responsibility) {
    if (responsibility == null) {
      throw new IllegalArgumentException("Responsibility must not be null");
    }
    this.responsibilities.add(responsibility);
  }

  /**
   * Удаляет обязанность из группы.
   *
   * @param responsibility обязанность для удаления
   */
  public void removeResponsibility(UserResponsibility responsibility) {
    if (responsibility == null) {
      throw new IllegalArgumentException("Responsibility must not be null");
    }
    this.responsibilities.remove(responsibility);
  }

  /**
   * Метод обновления основных полей группы. Коллекции (users и responsibilities) обновлять
   * рекомендуется через бизнес-методы.
   *
   * @param updated объект UserGroup с обновлёнными данными
   */
  @Override
  public void doUpdate(UserGroup updated) {
    this.name = updated.getName();
    this.email = updated.getEmail();
    this.groupType = updated.getGroupType();
    this.organization = updated.getOrganization();
    this.description = updated.getDescription();
    // Коллекции обновлять отдельно через add/remove методы
  }
}
