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
import lombok.Setter;
import org.jilt.Opt;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Entity representing a user in the system.
 * <p>
 * Implements Spring Security's UserDetails for authentication and authorization.
 * Stores credentials, personal info, organization, and group memberships.
 */
@Setter
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

  /**
   * Default constructor for JPA.
   */
  public User() {}

  /**
   * Constructs a new User with the specified details.
   *
   * @param username the username
   * @param email the email address
   * @param password the password
   * @param person the associated person entity
   * @param pictureUrl the URL of the user's picture (optional)
   */
  @JiltBuilder
  public User(
      String username, String email, String password, Person person, @Opt String pictureUrl) {
    this.username = username;
    this.email = email;
    this.password = password;
    this.pictureUrl = pictureUrl;
    this.person = person;
  }

  /**
   * Adds a group to the user's group set.
   *
   * @param group the group to add
   */
  public void addGroup(UserGroup group) {
    this.groups.add(group);
  }

  /**
   * Returns the organization the user belongs to.
   *
   * @return the organization
   */
  public Organization getOrganization() {
    return organization;
  }

  /**
   * Updates this user from another instance.
   *
   * @param updated the updated user
   */
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

  /**
   * Returns the authorities granted to the user.
   *
   * @return the authorities
   */
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return groups.stream()
        .flatMap(group -> group.getResponsibilities().stream())
        .map(responsibility -> new SimpleGrantedAuthority(responsibility.getName()))
        .collect(Collectors.toSet());
  }

  /**
   * Returns the user's password.
   *
   * @return the password
   */
  @Override
  public String getPassword() {
    return password;
  }

  /**
   * Returns the user's username.
   *
   * @return the username
   */
  @Override
  public String getUsername() {
    return username;
  }

  /**
   * Indicates whether the user's account is non-expired.
   *
   * @return true if the account is non-expired
   */
  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  /**
   * Indicates whether the user's account is non-locked.
   *
   * @return true if the account is non-locked
   */
  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  /**
   * Indicates whether the user's credentials are non-expired.
   *
   * @return true if the credentials are non-expired
   */
  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  /**
   * Indicates whether the user is enabled.
   *
   * @return true if enabled
   */
  @Override
  public boolean isEnabled() {
    return enabled;
  }

  /**
   * Returns the user's email address.
   *
   * @return the email address
   */
  public String getEmail() {
    return email;
  }

  /**
   * Returns the URL of the user's picture.
   *
   * @return the picture URL
   */
  public String getPictureUrl() {
    return pictureUrl;
  }

  /**
   * Returns the associated person entity.
   *
   * @return the person
   */
  public Person getPerson() {
    return person;
  }

  /**
   * Returns the set of groups the user belongs to.
   *
   * @return the set of groups
   */
  public Set<UserGroup> getGroups() {
    return groups;
  }
}
