package com.vazant.logix.orders.infrastructure.repository.media;

import com.vazant.logix.orders.domain.media.Image;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, UUID> {}
