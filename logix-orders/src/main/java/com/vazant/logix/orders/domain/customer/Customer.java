package com.vazant.logix.orders.domain.customer;

import com.vazant.logix.orders.domain.common.BaseEntity;
import com.vazant.logix.orders.domain.common.Updatable;
import com.vazant.logix.orders.infrastructure.utils.JiltBuilder;
import com.vazant.logix.shared.Constants;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Entity representing a customer in the system.
 * <p>
 * Stores personal and contact information for a customer and supports updating from another instance.
 */
@Entity
@Table(name = Constants.ENTITY_CUSTOMER)
public class Customer extends BaseEntity implements Updatable<Customer> {

  @NotBlank(message = "{validation.customer.firstName.required}")
  @Size(max = 100)
  private String firstName;

  @NotBlank(message = "{validation.customer.lastName.required}")
  @Size(max = 100)
  private String lastName;

  @Email(message = "{validation.customer.email.invalid}")
  @NotBlank(message = "{validation.customer.email.required}")
  @Size(max = 255)
  @Column(unique = true, nullable = false)
  private String email;

  @Pattern(regexp = "\\+?[0-9\\- ]{7,20}", message = "{validation.customer.phone.invalid}")
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

  /**
   * Default constructor for JPA.
   */
  protected Customer() {}

  /**
   * Constructs a new Customer with all fields.
   *
   * @param firstName the first name
   * @param lastName the last name
   * @param email the email address
   * @param phone the phone number
   * @param address the address
   * @param city the city
   * @param state the state
   * @param zip the zip code
   * @param country the country
   */
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

  /**
   * Returns the full name of the customer.
   *
   * @return the full name
   */
  public String getFullName() {
    return firstName + " " + lastName;
  }

  /**
   * Returns the city of the customer.
   *
   * @return the city
   */
  public String getCity() {
    return city;
  }

  /**
   * Returns the first name of the customer.
   *
   * @return the first name
   */
  public String getFirstName() {
    return firstName;
  }

  /**
   * Returns the last name of the customer.
   *
   * @return the last name
   */
  public String getLastName() {
    return lastName;
  }

  /**
   * Returns the email address of the customer.
   *
   * @return the email address
   */
  public String getEmail() {
    return email;
  }

  /**
   * Returns the phone number of the customer.
   *
   * @return the phone number
   */
  public String getPhone() {
    return phone;
  }

  /**
   * Returns the address of the customer.
   *
   * @return the address
   */
  public String getAddress() {
    return address;
  }

  /**
   * Returns the state of the customer.
   *
   * @return the state
   */
  public String getState() {
    return state;
  }

  /**
   * Returns the zip code of the customer.
   *
   * @return the zip code
   */
  public String getZip() {
    return zip;
  }

  /**
   * Returns the country of the customer.
   *
   * @return the country
   */
  public String getCountry() {
    return country;
  }

  /**
   * Sets the first name of the customer.
   *
   * @param firstName the first name
   */
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  /**
   * Sets the last name of the customer.
   *
   * @param lastName the last name
   */
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  /**
   * Sets the email address of the customer.
   *
   * @param email the email address
   */
  public void setEmail(String email) {
    this.email = email;
  }

  /**
   * Sets the phone number of the customer.
   *
   * @param phone the phone number
   */
  public void setPhone(String phone) {
    this.phone = phone;
  }

  /**
   * Sets the address of the customer.
   *
   * @param address the address
   */
  public void setAddress(String address) {
    this.address = address;
  }

  /**
   * Sets the city of the customer.
   *
   * @param city the city
   */
  public void setCity(String city) {
    this.city = city;
  }

  /**
   * Sets the state of the customer.
   *
   * @param state the state
   */
  public void setState(String state) {
    this.state = state;
  }

  /**
   * Sets the zip code of the customer.
   *
   * @param zip the zip code
   */
  public void setZip(String zip) {
    this.zip = zip;
  }

  /**
   * Sets the country of the customer.
   *
   * @param country the country
   */
  public void setCountry(String country) {
    this.country = country;
  }

  /**
   * Updates this customer from another instance.
   *
   * @param updated the updated customer
   */
  @Override
  public void doUpdate(Customer updated) {
    this.firstName = updated.getFirstName();
    this.lastName = updated.getLastName();
    this.email = updated.getEmail();
    this.phone = updated.getPhone();
    this.address = updated.getAddress();
    this.city = updated.getCity();
    this.state = updated.getState();
    this.zip = updated.getZip();
    this.country = updated.getCountry();
  }

  /**
   * Returns a string representation of the customer.
   *
   * @return string representation
   */
  @Override
  public String toString() {
    return "Customer{" +
        "uuid=" + getUuid() +
        ", firstName='" + firstName + '\'' +
        ", lastName='" + lastName + '\'' +
        ", email='" + email + '\'' +
        ", phone='" + phone + '\'' +
        '}';
  }
}
