package com.vazant.logix.orders.domain.media;

import com.vazant.logix.orders.domain.common.BaseEntity;
import com.vazant.logix.orders.domain.common.Updatable;
import com.vazant.logix.orders.infrastructure.utils.JiltBuilder;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

/**
 * Entity representing an image associated with other domain objects.
 * <p>
 * Stores the image URL and alternative text, and supports updating from another instance.
 */
@Entity
@Table(name = "images")
public class Image extends BaseEntity implements Updatable<Image> {
  @Column(nullable = false)
  @NotBlank(message = "URL is mandatory")
  private String url;

  @Column(nullable = false)
  @NotBlank(message = "Alt text is required")
  private String altText;

  /**
   * Default constructor for JPA.
   */
  protected Image() {}

  /**
   * Constructs a new Image with the specified URL and alt text.
   *
   * @param url the image URL
   * @param altText the alternative text for the image
   */
  @JiltBuilder
  public Image(String url, String altText) {
    this.url = url;
    this.altText = altText;
  }

  /**
   * Updates this image from another instance.
   *
   * @param updated the updated image
   */
  @Override
  public void doUpdate(Image updated) {
    this.url = updated.getUrl();
    this.altText = updated.getAltText();
  }

  /**
   * Returns the image URL.
   *
   * @return the image URL
   */
  public String getUrl() {
    return url;
  }

  /**
   * Returns the alternative text for the image.
   *
   * @return the alt text
   */
  public String getAltText() {
    return altText;
  }

  /**
   * Sets the image URL.
   *
   * @param url the image URL
   */
  public void setUrl(String url) {
    this.url = url;
  }

  /**
   * Sets the alternative text for the image.
   *
   * @param altText the alt text
   */
  public void setAltText(String altText) {
    this.altText = altText;
  }
}
