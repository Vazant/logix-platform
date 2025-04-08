package com.vazant.logix.orders.domain.organization;

import com.vazant.logix.orders.domain.common.BaseEntity;
import com.vazant.logix.orders.domain.common.Updatable;
import com.vazant.logix.orders.domain.user.User;
import com.vazant.logix.orders.infrastructure.utils.JiltBuilder;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "organizations")
public class Organization extends BaseEntity implements Updatable<Organization> {

  @NotBlank
  @Column(nullable = false, unique = true)
  private String name;

  @Email
  @Column(nullable = false)
  private String email;

  private String address;
  private String phoneNumber;

  @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<User> users = new HashSet<>();

  protected Organization() {}

  @JiltBuilder
  public Organization(String name, String email, String address, String phoneNumber) {
    this.name = name;
    this.email = email;
    this.address = address;
    this.phoneNumber = phoneNumber;
  }

  // Getters and Setters
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

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public Set<User> getUsers() {
    return users;
  }

  public void setUsers(Set<User> users) {
    this.users = users;
  }

  // Updatable implementation
  @Override
  public void doUpdate(Organization updated) {
    this.name = updated.name;
    this.email = updated.email;
    this.address = updated.address;
    this.phoneNumber = updated.phoneNumber;
  }
}
