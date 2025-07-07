package com.vazant.logix.orders.domain.user;

import com.vazant.logix.orders.domain.common.BaseEntity;
import com.vazant.logix.orders.domain.common.Updatable;
import com.vazant.logix.orders.infrastructure.utils.JiltBuilder;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Entity representing a user group in the system.
 * <p>
 * Stores group name, contact email, organization, description, users, and responsibilities.
 * Supports updating from another instance.
 */
@Entity
@Table(name = "user_groups")
public class UserGroup extends BaseEntity implements Updatable<UserGroup> {

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(nullable = false, unique = true)
  private UserGroupName groupName;
  @Email
  @Column(nullable = false)
  private String email; // Контактный email группы
  private String organization; // ID или имя организации (можно заменить на @ManyToOne позже)
  private String description; // Дополнительное описание
  @ManyToMany(mappedBy = "groups")
  private List<User> users = new ArrayList<>();
  @ManyToMany
  @JoinTable(
      name = "user_responsibilities",
      joinColumns = @JoinColumn(name = "group_uuid"),
      inverseJoinColumns = @JoinColumn(name = "user_responsibility_uuid"))
  private Set<UserResponsibility> responsibilities = new HashSet<>();

  /**
   * Constructs a new UserGroup with the specified details.
   *
   * @param groupName the group name
   * @param email the contact email
   * @param organization the organization (ID or name)
   * @param description the group description
   */
  @JiltBuilder
  public UserGroup(UserGroupName groupName, String email, String organization, String description) {
    this.groupName = groupName;
    this.email = email;
    this.organization = organization;
    this.description = description;
  }

  /**
   * Returns the group name.
   *
   * @return the group name
   */
  public UserGroupName getGroupName() {
    return groupName;
  }

  /**
   * Sets the group name.
   *
   * @param groupName the group name
   */
  public void setGroupName(UserGroupName groupName) {
    this.groupName = groupName;
  }

  /**
   * Returns the contact email.
   *
   * @return the contact email
   */
  public String getEmail() {
    return email;
  }

  /**
   * Sets the contact email.
   *
   * @param email the contact email
   */
  public void setEmail(String email) {
    this.email = email;
  }

  /**
   * Returns the organization (ID or name).
   *
   * @return the organization
   */
  public String getOrganization() {
    return organization;
  }

  /**
   * Sets the organization (ID or name).
   *
   * @param organization the organization
   */
  public void setOrganization(String organization) {
    this.organization = organization;
  }

  /**
   * Returns the group description.
   *
   * @return the description
   */
  public String getDescription() {
    return description;
  }

  /**
   * Sets the group description.
   *
   * @param description the description
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Returns the list of users in the group.
   *
   * @return the list of users
   */
  public List<User> getUsers() {
    return users;
  }

  /**
   * Sets the list of users in the group.
   *
   * @param users the list of users
   */
  public void setUsers(List<User> users) {
    this.users = users;
  }

  /**
   * Returns the set of responsibilities assigned to the group.
   *
   * @return the set of responsibilities
   */
  public Set<UserResponsibility> getResponsibilities() {
    return responsibilities;
  }

  /**
   * Sets the set of responsibilities assigned to the group.
   *
   * @param responsibilities the set of responsibilities
   */
  public void setResponsibilities(Set<UserResponsibility> responsibilities) {
    this.responsibilities = responsibilities;
  }

  /**
   * Adds a user to the group and updates the user's group set.
   *
   * @param user the user to add
   * @throws IllegalArgumentException if user is null
   */
  public void addUser(User user) {
    if (user == null) throw new IllegalArgumentException("User must not be null");
    if (!this.users.contains(user)) {
      this.users.add(user);
      user.getGroups().add(this);
    }
  }

  /**
   * Removes a user from the group and updates the user's group set.
   *
   * @param user the user to remove
   * @throws IllegalArgumentException if user is null
   */
  public void removeUser(User user) {
    if (user == null) throw new IllegalArgumentException("User must not be null");
    if (this.users.remove(user)) {
      user.getGroups().remove(this);
    }
  }

  /**
   * Adds a responsibility to the group.
   *
   * @param responsibility the responsibility to add
   * @throws IllegalArgumentException if responsibility is null
   */
  public void addResponsibility(UserResponsibility responsibility) {
    if (responsibility == null)
      throw new IllegalArgumentException("Responsibility must not be null");
    this.responsibilities.add(responsibility);
  }

  /**
   * Removes a responsibility from the group.
   *
   * @param responsibility the responsibility to remove
   * @throws IllegalArgumentException if responsibility is null
   */
  public void removeResponsibility(UserResponsibility responsibility) {
    if (responsibility == null)
      throw new IllegalArgumentException("Responsibility must not be null");
    this.responsibilities.remove(responsibility);
  }

  /**
   * Updates this user group from another instance.
   *
   * @param updated the updated user group
   */
  @Override
  public void doUpdate(UserGroup updated) {
    this.groupName = updated.getGroupName();
    this.email = updated.getEmail();
    this.organization = updated.getOrganization();
    this.description = updated.getDescription();
  }
}
