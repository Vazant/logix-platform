package com.vazant.logix.orders.presentation.controller.media;

import com.vazant.logix.orders.application.service.media.ImageService;
import com.vazant.logix.orders.domain.media.Image;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/images")
public class ImageController {

  private final ImageService imageService;

  public ImageController(ImageService imageService) {
    this.imageService = imageService;
  }

  @GetMapping
  public ResponseEntity<List<Image>> getAllImages() {
    List<Image> images = imageService.findAll();
    return ResponseEntity.ok(images);
  }

  @GetMapping("/{imageUuid}")
  public ResponseEntity<Image> getImage(@PathVariable String imageUuid) {
    Image image = imageService.findByUuid(imageUuid);
    return ResponseEntity.ok(image);
  }

  @PostMapping
  public ResponseEntity<Image> createImage(@RequestBody Image image) {
    Image created = imageService.create(image);
    return ResponseEntity.ok(created);
  }

  @PutMapping("/{imageUuid}")
  public ResponseEntity<Image> updateImage(
      @PathVariable String imageUuid, @RequestBody Image image) {
    Image updated = imageService.update(imageUuid, image);
    return ResponseEntity.ok(updated);
  }

  @DeleteMapping("/{imageUuid}")
  public ResponseEntity<Void> deleteImage(@PathVariable String imageUuid) {
    imageService.delete(imageUuid);
    return ResponseEntity.noContent().build();
  }
}
