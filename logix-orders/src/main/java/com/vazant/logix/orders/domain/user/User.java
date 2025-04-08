package com.vazant.logix.orders.domain.user;

import com.vazant.logix.orders.domain.common.BaseEntity;
import com.vazant.logix.orders.domain.common.Updatable;
import com.vazant.logix.orders.domain.organization.Organization;
import com.vazant.logix.orders.infrastructure.utils.JiltBuilder;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "users")
public class User extends BaseEntity implements UserDetails, Updatable<User> {

  @NotBlank(message = "Username is required")
  @Column(unique = true, nullable = false)
  private String username;

  @NotBlank(message = "Email is required")
  @Email(message = "Invalid email")
  @Column(unique = true, nullable = false)
  private String email;

  @NotBlank(message = "Password is required")
  @Column(nullable = false)
  private String password;

  private String pictureUrl;

  @Column(nullable = false)
  private boolean enabled = true;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "person_uuid", referencedColumnName = "uuid")
  private Person person;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "organization_uuid")
  private Organization organization;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "user_groups",
      joinColumns = @JoinColumn(name = "user_uuid"),
      inverseJoinColumns = @JoinColumn(name = "group_uuid"))
  private Set<UserGroup> groups = new HashSet<>();

  public User() {}

  @JiltBuilder
  public User(String username, String email, String password, String pictureUrl, Person person) {
    this.username = username;
    this.email = email;
    this.password = password;
    this.pictureUrl = pictureUrl;
    this.person = person;
  }

  public void addGroup(UserGroup group) {
    this.groups.add(group);
  }

  public Organization getOrganization() {
    return organization;
  }

  public void setOrganization(Organization organization) {
    this.organization = organization;
  }

  @Override
  public void doUpdate(User updated) {
    this.username = updated.username;
    this.email = updated.email;
    this.pictureUrl = updated.pictureUrl;
    this.enabled = updated.enabled;

    if (updated.person != null) {
      this.person = updated.person;
    }
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return groups.stream()
        .flatMap(group -> group.getResponsibilities().stream())
        .map(responsibility -> new SimpleGrantedAuthority(responsibility.getName()))
        .collect(Collectors.toSet());
  }

  @Override
  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPictureUrl() {
    return pictureUrl;
  }

  public void setPictureUrl(String pictureUrl) {
    this.pictureUrl = pictureUrl;
  }

  public Person getPerson() {
    return person;
  }

  public void setPerson(Person person) {
    this.person = person;
  }

  public Set<UserGroup> getGroups() {
    return groups;
  }

  public void setGroups(Set<UserGroup> groups) {
    this.groups = groups;
  }
}
