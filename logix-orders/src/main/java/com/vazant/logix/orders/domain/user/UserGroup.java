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

  @JiltBuilder
  public UserGroup(UserGroupName groupName, String email, String organization, String description) {
    this.groupName = groupName;
    this.email = email;
    this.organization = organization;
    this.description = description;
  }

  public UserGroupName getGroupName() {
    return groupName;
  }

  public void setGroupName(UserGroupName groupName) {
    this.groupName = groupName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
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

  public void addUser(User user) {
    if (user == null) throw new IllegalArgumentException("User must not be null");
    if (!this.users.contains(user)) {
      this.users.add(user);
      user.getGroups().add(this);
    }
  }

  public void removeUser(User user) {
    if (user == null) throw new IllegalArgumentException("User must not be null");
    if (this.users.remove(user)) {
      user.getGroups().remove(this);
    }
  }

  public void addResponsibility(UserResponsibility responsibility) {
    if (responsibility == null)
      throw new IllegalArgumentException("Responsibility must not be null");
    this.responsibilities.add(responsibility);
  }

  public void removeResponsibility(UserResponsibility responsibility) {
    if (responsibility == null)
      throw new IllegalArgumentException("Responsibility must not be null");
    this.responsibilities.remove(responsibility);
  }

  @Override
  public void doUpdate(UserGroup updated) {
    this.groupName = updated.getGroupName();
    this.email = updated.getEmail();
    this.organization = updated.getOrganization();
    this.description = updated.getDescription();
  }
}
