package com.vazant.logix.orders.presentation.controller.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for ActivationPageController.
 * Tests page rendering endpoints.
 */
@WebMvcTest(ActivationPageController.class)
class ActivationPageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void showActivationPage_ShouldReturnActivationPage() throws Exception {
        // When & Then
        mockMvc.perform(get("/activate"))
                .andExpect(status().isOk())
                .andExpect(view().name("activate"));
    }

    @Test
    void showActivationPage_WithTrailingSlash_ShouldReturnActivationPage() throws Exception {
        // When & Then
        mockMvc.perform(get("/activate/"))
                .andExpect(status().isOk())
                .andExpect(view().name("activate"));
    }

    @Test
    void showActivationPage_WithUpperCasePath_ShouldReturnNotFound() throws Exception {
        // When & Then
        mockMvc.perform(get("/ACTIVATE"))
                .andExpect(status().isNotFound());
    }

    @Test
    void showActivationPage_WithInvalidPath_ShouldReturnNotFound() throws Exception {
        // When & Then
        mockMvc.perform(get("/invalid-path"))
                .andExpect(status().isNotFound());
    }

    @Test
    void showActivationPage_WithQueryParameters_ShouldReturnActivationPage() throws Exception {
        // When & Then
        mockMvc.perform(get("/activate")
                        .param("token", "test-token")
                        .param("email", "test@example.com"))
                .andExpect(status().isOk())
                .andExpect(view().name("activate"));
    }

    @Test
    void showActivationPage_WithSpecialCharactersInQuery_ShouldReturnActivationPage() throws Exception {
        // When & Then
        mockMvc.perform(get("/activate")
                        .param("token", "token-with-special-chars!@#$%^&*()")
                        .param("email", "test+tag@example.com"))
                .andExpect(status().isOk())
                .andExpect(view().name("activate"));
    }

    @Test
    void showActivationPage_WithEmptyQueryParameters_ShouldReturnActivationPage() throws Exception {
        // When & Then
        mockMvc.perform(get("/activate")
                        .param("token", "")
                        .param("email", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("activate"));
    }

    @Test
    void showActivationPage_WithPostMethod_ShouldReturnMethodNotAllowed() throws Exception {
        // When & Then
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post("/activate"))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void showActivationPage_WithPutMethod_ShouldReturnMethodNotAllowed() throws Exception {
        // When & Then
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put("/activate"))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void showActivationPage_WithDeleteMethod_ShouldReturnMethodNotAllowed() throws Exception {
        // When & Then
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete("/activate"))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void showActivationPage_WithPatchMethod_ShouldReturnMethodNotAllowed() throws Exception {
        // When & Then
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch("/activate"))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void showActivationPage_WithHeadMethod_ShouldReturnMethodNotAllowed() throws Exception {
        // When & Then
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.head("/activate"))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void showActivationPage_WithOptionsMethod_ShouldReturnMethodNotAllowed() throws Exception {
        // When & Then
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options("/activate"))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void showActivationPage_WithTraceMethod_ShouldReturnMethodNotAllowed() throws Exception {
        // When & Then
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request(HttpMethod.TRACE, "/activate"))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    void showActivationPage_WithValidToken_ShouldReturnActivationPage() throws Exception {
        // Given
        String validToken = "test-token";

        // When & Then
        mockMvc.perform(get("/activate")
                        .param("token", validToken))
                .andExpect(status().isOk())
                .andExpect(view().name("activate"))
                .andExpect(model().attribute("token", validToken));
    }
} 