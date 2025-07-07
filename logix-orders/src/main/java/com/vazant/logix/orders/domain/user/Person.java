package com.vazant.logix.orders.domain.user;

import com.vazant.logix.orders.domain.common.BaseEntity;
import com.vazant.logix.orders.domain.common.Updatable;
import com.vazant.logix.orders.infrastructure.utils.JiltBuilder;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

/**
 * Entity representing a person in the system.
 * <p>
 * Stores personal information such as name, phone number, and birth date.
 * Supports updating from another instance.
 */
@Entity
@Table(name = "persons")
public class Person extends BaseEntity implements Updatable<Person> {

  @NotBlank(message = "First name is required")
  @Column(nullable = false)
  private String firstName;

  @NotBlank(message = "Last name is required")
  @Column(nullable = false)
  private String lastName;

  @Column
  private String phoneNumber;

  @Column
  private LocalDate birthDate;

  /**
   * Default constructor for JPA.
   */
  public Person() {}

  /**
   * Constructs a new Person with the specified details.
   *
   * @param firstName the first name
   * @param lastName the last name
   * @param phoneNumber the phone number
   * @param birthDate the birth date
   */
  @JiltBuilder
  public Person(String firstName, String lastName, String phoneNumber, LocalDate birthDate) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.phoneNumber = phoneNumber;
    this.birthDate = birthDate;
  }

  /**
   * Returns the first name.
   *
   * @return the first name
   */
  public String getFirstName() {
    return firstName;
  }

  /**
   * Sets the first name.
   *
   * @param firstName the first name
   */
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  /**
   * Returns the last name.
   *
   * @return the last name
   */
  public String getLastName() {
    return lastName;
  }

  /**
   * Sets the last name.
   *
   * @param lastName the last name
   */
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  /**
   * Returns the phone number.
   *
   * @return the phone number
   */
  public String getPhoneNumber() {
    return phoneNumber;
  }

  /**
   * Sets the phone number.
   *
   * @param phoneNumber the phone number
   */
  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  /**
   * Returns the birth date.
   *
   * @return the birth date
   */
  public LocalDate getBirthDate() {
    return birthDate;
  }

  /**
   * Sets the birth date.
   *
   * @param birthDate the birth date
   */
  public void setBirthDate(LocalDate birthDate) {
    this.birthDate = birthDate;
  }

  /**
   * Updates this person from another instance.
   *
   * @param updated the updated person
   * @throws IllegalArgumentException if updated is null
   */
  @Override
  public void doUpdate(Person updated) {
    if (updated == null) {
      throw new IllegalArgumentException("Updated person must not be null");
    }
    this.firstName = updated.getFirstName();
    this.lastName = updated.getLastName();
    this.phoneNumber = updated.getPhoneNumber();
    this.birthDate = updated.getBirthDate();
  }
}
