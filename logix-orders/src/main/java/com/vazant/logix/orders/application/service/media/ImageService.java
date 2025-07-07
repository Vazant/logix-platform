package com.vazant.logix.orders.application.service.media;

import com.vazant.logix.orders.application.service.common.AbstractCrudService;
import com.vazant.logix.orders.domain.media.Image;
import com.vazant.logix.orders.infrastructure.repository.media.ImageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for managing images.
 */
@Slf4j
@Service
@Transactional
public class ImageService extends AbstractCrudService<Image> {

  public ImageService(ImageRepository imageRepository) {
    super(imageRepository, Image.class);
  }

  @Override
  protected String getEntityName() {
    return "Image";
  }
}
