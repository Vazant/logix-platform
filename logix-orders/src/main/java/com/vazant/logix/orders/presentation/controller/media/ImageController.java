package com.vazant.logix.orders.presentation.controller.media;

import com.vazant.logix.orders.application.service.common.CrudService;
import com.vazant.logix.orders.domain.media.Image;
import com.vazant.logix.orders.presentation.controller.common.BaseController;
import com.vazant.logix.orders.presentation.validation.ValidUuid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * REST controller for managing images.
 * <p>
 * Provides CRUD operations and image-specific endpoints like file upload and optimization.
 * Extends BaseController to inherit common CRUD operations and validation.
 */
@RestController
@RequestMapping("/api/images")
public class ImageController extends BaseController<Image> {

  public ImageController(CrudService<Image> imageService) {
    super(imageService);
  }

  /**
   * Upload a new image file.
   *
   * @param file the image file to upload
   * @return the created image entity
   */
  @PostMapping("/upload")
  public ResponseEntity<Image> uploadImage(@RequestParam("file") MultipartFile file) {
    // TODO: Implement image upload functionality
    // This would require adding upload logic to ImageService
    return ResponseEntity.ok().build();
  }

  /**
   * Find images by product UUID.
   *
   * @param productId the product UUID
   * @return list of images for the product
   */
  @GetMapping("/product/{productId}")
  public ResponseEntity<List<Image>> findByProduct(@PathVariable @ValidUuid String productId) {
    // TODO: Implement image search by product
    return ResponseEntity.ok(List.of());
  }

  /**
   * Resize an image to specified dimensions.
   *
   * @param imageId the image UUID
   * @param width the target width
   * @param height the target height
   * @return the resized image
   */
  @PostMapping("/{imageId}/resize")
  public ResponseEntity<Image> resizeImage(
      @PathVariable @ValidUuid String imageId,
      @RequestParam int width,
      @RequestParam int height) {
    // TODO: Implement image resize functionality
    return ResponseEntity.ok().build();
  }

  /**
   * Optimize an image for web delivery.
   *
   * @param imageId the image UUID
   * @return the optimized image
   */
  @PostMapping("/{imageId}/optimize")
  public ResponseEntity<Image> optimizeImage(@PathVariable @ValidUuid String imageId) {
    // TODO: Implement image optimization
    return ResponseEntity.ok().build();
  }
}
