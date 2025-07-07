package com.vazant.logix.orders.domain.organization;

import com.vazant.logix.orders.domain.common.BaseEntity;
import com.vazant.logix.orders.domain.common.Updatable;
import com.vazant.logix.orders.domain.user.User;
import com.vazant.logix.orders.infrastructure.utils.JiltBuilder;
import com.vazant.logix.shared.Constants;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

/**
 * Entity representing an organization in the system.
 * <p>
 * Stores organization details and associated users, and supports updating from another instance.
 */
@Entity
@Table(name = Constants.ENTITY_ORGANIZATION)
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

  /**
   * Default constructor for JPA.
   */
  protected Organization() {}

  /**
   * Constructs a new Organization with the specified details.
   *
   * @param name the organization name
   * @param email the organization email
   * @param address the address
   * @param phoneNumber the phone number
   */
  @JiltBuilder
  public Organization(String name, String email, String address, String phoneNumber) {
    this.name = name;
    this.email = email;
    this.address = address;
    this.phoneNumber = phoneNumber;
  }

  /**
   * Returns the organization name.
   *
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the organization name.
   *
   * @param name the name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Returns the organization email.
   *
   * @return the email
   */
  public String getEmail() {
    return email;
  }

  /**
   * Sets the organization email.
   *
   * @param email the email
   */
  public void setEmail(String email) {
    this.email = email;
  }

  /**
   * Returns the address of the organization.
   *
   * @return the address
   */
  public String getAddress() {
    return address;
  }

  /**
   * Sets the address of the organization.
   *
   * @param address the address
   */
  public void setAddress(String address) {
    this.address = address;
  }

  /**
   * Returns the phone number of the organization.
   *
   * @return the phone number
   */
  public String getPhoneNumber() {
    return phoneNumber;
  }

  /**
   * Sets the phone number of the organization.
   *
   * @param phoneNumber the phone number
   */
  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  /**
   * Returns the set of users associated with the organization.
   *
   * @return the set of users
   */
  public Set<User> getUsers() {
    return users;
  }

  /**
   * Sets the users associated with the organization.
   *
   * @param users the set of users
   */
  public void setUsers(Set<User> users) {
    this.users = users;
  }

  /**
   * Updates this organization from another instance.
   *
   * @param updated the updated organization
   */
  @Override
  public void doUpdate(Organization updated) {
    this.name = updated.name;
    this.email = updated.email;
    this.address = updated.address;
    this.phoneNumber = updated.phoneNumber;
  }
}
