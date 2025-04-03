package com.vazant.logix.orders.domain.customer;

import com.vazant.logix.orders.common.BaseEntity;
import com.vazant.logix.orders.sdk.utils.JiltBuilder;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "customers")
public class Customer extends BaseEntity {

  @NotBlank(message = "First name is required")
  @Size(max = 100)
  private String firstName;

  @NotBlank(message = "Last name is required")
  @Size(max = 100)
  private String lastName;

  @Email(message = "Invalid email format")
  @NotBlank(message = "Email is required")
  @Size(max = 255)
  @Column(unique = true, nullable = false)
  private String email;

  @Pattern(regexp = "\\+?[0-9\\- ]{7,20}", message = "Invalid phone number")
  private String phone;

  @Size(max = 255)
  private String address;

  @Size(max = 100)
  private String city;

  @Size(max = 100)
  private String state;

  @Size(max = 20)
  private String zip;

  @Size(max = 100)
  private String country;

  protected Customer() {}

  @JiltBuilder
  public Customer(
      String firstName,
      String lastName,
      String email,
      String phone,
      String address,
      String city,
      String state,
      String zip,
      String country) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.phone = phone;
    this.address = address;
    this.city = city;
    this.state = state;
    this.zip = zip;
    this.country = country;
  }

  public String getFullName() {
    return firstName + " " + lastName;
  }

  public String getCity() {
    return city;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public String getEmail() {
    return email;
  }

  public String getPhone() {
    return phone;
  }

  public String getAddress() {
    return address;
  }

  public String getState() {
    return state;
  }

  public String getZip() {
    return zip;
  }

  public String getCountry() {
    return country;
  }
}
