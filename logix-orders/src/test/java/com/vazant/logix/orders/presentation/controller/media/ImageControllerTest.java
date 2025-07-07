package com.vazant.logix.orders.presentation.controller.media;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vazant.logix.orders.application.service.common.CrudService;
import com.vazant.logix.orders.application.service.media.ImageService;
import com.vazant.logix.orders.domain.media.Image;
import com.vazant.logix.orders.domain.media.ImageBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for ImageController.
 * Tests CRUD operations and image-specific endpoints.
 */
@WebMvcTest(ImageController.class)
class ImageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CrudService<Image> imageService;

    private Image testImage;
    private String validUuid;

    @BeforeEach
    void setUp() {
        validUuid = UUID.randomUUID().toString();
        testImage = ImageBuilder.image()
                .url("https://example.com/image.jpg")
                .altText("Test image alt text")
                .build();
        // uuid, createdAt, updatedAt можно мокать через сервис, если нужно
    }

    @Test
    void getAll_ShouldReturnAllImages() throws Exception {
        // Given
        List<Image> images = List.of(testImage);
        when(imageService.findAll()).thenReturn(images);

        // When & Then
        mockMvc.perform(get("/api/images"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].uuid").value(validUuid))
                .andExpect(jsonPath("$[0].url").value("https://example.com/image.jpg"))
                .andExpect(jsonPath("$[0].altText").value("Test image alt text"));

        verify(imageService).findAll();
    }

    @Test
    void getById_WithValidUuid_ShouldReturnImage() throws Exception {
        // Given
        when(imageService.findByUuid(validUuid)).thenReturn(testImage);

        // When & Then
        mockMvc.perform(get("/api/images/{uuid}", validUuid))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.uuid").value(validUuid))
                .andExpect(jsonPath("$.url").value("https://example.com/image.jpg"))
                .andExpect(jsonPath("$.altText").value("Test image alt text"));

        verify(imageService).findByUuid(validUuid);
    }

    @Test
    void getById_WithInvalidUuid_ShouldReturnBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/images/{uuid}", "invalid-uuid"))
                .andExpect(status().isBadRequest());

        verify(imageService, never()).findByUuid(anyString());
    }

    @Test
    void create_WithValidImage_ShouldReturnCreatedImage() throws Exception {
        // Given
        when(imageService.create(any(Image.class))).thenReturn(testImage);

        // When & Then
        mockMvc.perform(post("/api/images")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testImage)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.uuid").value(validUuid))
                .andExpect(jsonPath("$.url").value("https://example.com/image.jpg"));

        verify(imageService).create(any(Image.class));
    }

    @Test
    void create_WithInvalidImage_ShouldReturnBadRequest() throws Exception {
        // Given
        Image invalidImage = new Image("", "Test image alt text"); // Invalid: empty URL

        // When & Then
        mockMvc.perform(post("/api/images")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidImage)))
                .andExpect(status().isBadRequest());

        verify(imageService, never()).create(any(Image.class));
    }

    @Test
    void update_WithValidData_ShouldReturnUpdatedImage() throws Exception {
        // Given
        when(imageService.update(eq(validUuid), any(Image.class))).thenReturn(testImage);

        // When & Then
        mockMvc.perform(put("/api/images/{uuid}", validUuid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testImage)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.uuid").value(validUuid))
                .andExpect(jsonPath("$.url").value("https://example.com/image.jpg"));

        verify(imageService).update(eq(validUuid), any(Image.class));
    }

    @Test
    void update_WithInvalidUuid_ShouldReturnBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(put("/api/images/{uuid}", "invalid-uuid")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testImage)))
                .andExpect(status().isBadRequest());

        verify(imageService, never()).update(anyString(), any(Image.class));
    }

    @Test
    void delete_WithValidUuid_ShouldReturnNoContent() throws Exception {
        // Given
        doNothing().when(imageService).delete(validUuid);

        // When & Then
        mockMvc.perform(delete("/api/images/{uuid}", validUuid))
                .andExpect(status().isNoContent());

        verify(imageService).delete(validUuid);
    }

    @Test
    void delete_WithInvalidUuid_ShouldReturnBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(delete("/api/images/{uuid}", "invalid-uuid"))
                .andExpect(status().isBadRequest());

        verify(imageService, never()).delete(anyString());
    }

    @Test
    void exists_WithValidUuid_ShouldReturnTrue() throws Exception {
        // Given
        when(imageService.exists(validUuid)).thenReturn(true);

        // When & Then
        mockMvc.perform(get("/api/images/{uuid}/exists", validUuid))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(imageService).exists(validUuid);
    }

    @Test
    void exists_WithInvalidUuid_ShouldReturnBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/images/{uuid}/exists", "invalid-uuid"))
                .andExpect(status().isBadRequest());

        verify(imageService, never()).exists(anyString());
    }

    @Test
    void count_ShouldReturnImageCount() throws Exception {
        // Given
        when(imageService.count()).thenReturn(100L);

        // When & Then
        mockMvc.perform(get("/api/images/count"))
                .andExpect(status().isOk())
                .andExpect(content().string("100"));

        verify(imageService).count();
    }

    @Test
    void uploadImage_WithValidFile_ShouldReturnOk() throws Exception {
        // Given
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test-image.jpg",
                "image/jpeg",
                "test image content".getBytes()
        );

        // When & Then
        mockMvc.perform(multipart("/api/images/upload")
                        .file(file))
                .andExpect(status().isOk());

        // Note: This endpoint is not implemented yet, so it returns 200 OK
    }

    @Test
    void uploadImage_WithEmptyFile_ShouldReturnBadRequest() throws Exception {
        // Given
        MockMultipartFile emptyFile = new MockMultipartFile(
                "file",
                "",
                "image/jpeg",
                new byte[0]
        );

        // When & Then
        mockMvc.perform(multipart("/api/images/upload")
                        .file(emptyFile))
                .andExpect(status().isBadRequest());
    }

    @Test
    void uploadImage_WithInvalidContentType_ShouldReturnBadRequest() throws Exception {
        // Given
        MockMultipartFile invalidFile = new MockMultipartFile(
                "file",
                "test.txt",
                "text/plain",
                "test content".getBytes()
        );

        // When & Then
        mockMvc.perform(multipart("/api/images/upload")
                        .file(invalidFile))
                .andExpect(status().isBadRequest());
    }

    @Test
    void findByProduct_WithValidProductId_ShouldReturnEmptyList() throws Exception {
        // Given
        String productId = UUID.randomUUID().toString();

        // When & Then
        mockMvc.perform(get("/api/images/product/{productId}", productId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        // Note: This endpoint is not implemented yet, so it returns empty list
    }

    @Test
    void findByProduct_WithInvalidProductId_ShouldReturnBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/images/product/{productId}", "invalid-uuid"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void resizeImage_WithValidParameters_ShouldReturnOk() throws Exception {
        // When & Then
        mockMvc.perform(post("/api/images/{imageId}/resize", validUuid)
                        .param("width", "800")
                        .param("height", "600"))
                .andExpect(status().isOk());

        // Note: This endpoint is not implemented yet, so it returns 200 OK
    }

    @Test
    void resizeImage_WithInvalidImageId_ShouldReturnBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(post("/api/images/{imageId}/resize", "invalid-uuid")
                        .param("width", "800")
                        .param("height", "600"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void resizeImage_WithInvalidDimensions_ShouldReturnBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(post("/api/images/{imageId}/resize", validUuid)
                        .param("width", "-100") // Invalid: negative width
                        .param("height", "600"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void optimizeImage_WithValidImageId_ShouldReturnOk() throws Exception {
        // When & Then
        mockMvc.perform(post("/api/images/{imageId}/optimize", validUuid))
                .andExpect(status().isOk());

        // Note: This endpoint is not implemented yet, so it returns 200 OK
    }

    @Test
    void optimizeImage_WithInvalidImageId_ShouldReturnBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(post("/api/images/{imageId}/optimize", "invalid-uuid"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_WithMissingRequiredFields_ShouldReturnBadRequest() throws Exception {
        // Given
        Image imageWithMissingFields = new Image("", "Test alt text"); // Missing URL

        // When & Then
        mockMvc.perform(post("/api/images")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(imageWithMissingFields)))
                .andExpect(status().isBadRequest());

        verify(imageService, never()).create(any(Image.class));
    }

    @Test
    void update_WithEmptyFileName_ShouldReturnBadRequest() throws Exception {
        // Given
        Image imageWithEmptyUrl = new Image("", "Test alt text"); // Invalid: empty URL

        // When & Then
        mockMvc.perform(put("/api/images/{uuid}", validUuid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(imageWithEmptyUrl)))
                .andExpect(status().isBadRequest());

        verify(imageService, never()).update(anyString(), any(Image.class));
    }

    @Test
    void create_WithNegativeFileSize_ShouldReturnBadRequest() throws Exception {
        // Given
        Image imageWithEmptyAltText = new Image("https://example.com/image.jpg", ""); // Invalid: empty alt text

        // When & Then
        mockMvc.perform(post("/api/images")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(imageWithEmptyAltText)))
                .andExpect(status().isBadRequest());

        verify(imageService, never()).create(any(Image.class));
    }
} 