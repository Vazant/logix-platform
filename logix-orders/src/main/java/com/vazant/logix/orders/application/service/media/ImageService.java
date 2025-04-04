package com.vazant.logix.orders.application.service.media;

import com.vazant.logix.orders.application.service.common.AbstractCrudService;
import com.vazant.logix.orders.domain.media.Image;
import com.vazant.logix.orders.infrastructure.repository.media.ImageRepository;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class ImageService extends AbstractCrudService<Image> {
  private final ImageRepository imageRepository;

  public ImageService(ImageRepository imageRepository) {
    super(Image.class);
    this.imageRepository = imageRepository;
  }

  @Override
  protected JpaRepository<Image, UUID> getRepository() {
    return imageRepository;
  }
}
