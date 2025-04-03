package com.vazant.logix.orders.domain.media;

import com.vazant.logix.orders.common.BaseEntity;
import com.vazant.logix.orders.sdk.utils.JiltBuilder;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "images")
public class Image extends BaseEntity {
  @Column(nullable = false)
  @NotBlank(message = "URL is mandatory")
  private String url;

  @Column(nullable = false)
  @NotBlank(message = "Alt text is required")
  private String altText;

  protected Image() {}

  @JiltBuilder
  public Image(String url, String altText) {
    this.url = url;
    this.altText = altText;
  }

  public String getUrl() {
    return url;
  }

  public String getAltText() {
    return altText;
  }
}
