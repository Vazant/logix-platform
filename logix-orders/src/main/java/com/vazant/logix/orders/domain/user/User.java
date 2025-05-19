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

  public User() {}

  @JiltBuilder
  public User(
      String username, String email, String password, Person person, @Opt String pictureUrl) {
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

  @Override
  public String getUsername() {
    return username;
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

  public String getEmail() {
    return email;
  }

  public String getPictureUrl() {
    return pictureUrl;
  }

  public Person getPerson() {
    return person;
  }

  public Set<UserGroup> getGroups() {
    return groups;
  }
}
