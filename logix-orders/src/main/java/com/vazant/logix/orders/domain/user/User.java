package com.vazant.logix.orders.domain.user;

import com.vazant.logix.orders.domain.common.BaseEntity;
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
public class User extends BaseEntity implements UserDetails {

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

  // Ссылка на аватар или путь к изображению
  private String pictureUrl;

  // Флаг, показывающий, активна ли учётная запись
  @Column(nullable = false)
  private boolean enabled = true;

  // Связь один-к-одному с сущностью Person для хранения профильных данных
  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "person_uuid", referencedColumnName = "uuid")
  private Person person;

  // Связь many-to-many с группами, к которым принадлежит пользователь
  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "user_groups",
      joinColumns = @JoinColumn(name = "user_uuid"),
      inverseJoinColumns = @JoinColumn(name = "group_uuid"))
  private Set<UserGroup> groups = new HashSet<>();

  public User() {}

  public User(String username, String email, String password, String pictureUrl, Person person) {
    this.username = username;
    this.email = email;
    this.password = password;
    this.pictureUrl = pictureUrl;
    this.person = person;
  }

  // Геттеры и сеттеры

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

  public void addGroup(UserGroup group) {
    groups.add(group);
  }

  // Реализация UserDetails

  /**
   * Собираем GrantedAuthority на основе ролей (или обязанностей) из групп. Предполагается, что у
   * каждой группы есть метод getRoles(), возвращающий Set<Role>, а у Role есть метод getName(),
   * возвращающий название роли.
   */
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
    return true; // При необходимости можно добавить бизнес-логику
  }

  @Override
  public boolean isAccountNonLocked() {
    return true; // При необходимости можно добавить бизнес-логику
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true; // При необходимости можно добавить бизнес-логику
  }

  @Override
  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }
}
