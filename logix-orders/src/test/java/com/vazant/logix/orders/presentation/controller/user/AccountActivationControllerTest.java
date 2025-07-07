package com.vazant.logix.orders.presentation.controller.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vazant.logix.orders.application.service.user.AccountActivationService;
import com.vazant.logix.orders.dto.user.AccountActivationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for AccountActivationController.
 * Tests account activation and token validation endpoints.
 */
@WebMvcTest(AccountActivationController.class)
class AccountActivationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AccountActivationService activationService;

    private AccountActivationRequest validActivationRequest;
    private String validToken;

    @BeforeEach
    void setUp() {
        validToken = "valid-activation-token-123";
        validActivationRequest = new AccountActivationRequest(
                validToken,
                "newPassword123"
        );
    }

    @Test
    void activateAccount_WithValidRequest_ShouldReturnSuccess() throws Exception {
        // Given
        when(activationService.activateAccount(any(AccountActivationRequest.class))).thenReturn(true);

        // When & Then
        mockMvc.perform(post("/api/account/activate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validActivationRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Account activated successfully"));

        verify(activationService).activateAccount(any(AccountActivationRequest.class));
    }

    @Test
    void activateAccount_WithInvalidRequest_ShouldReturnFailure() throws Exception {
        // Given
        when(activationService.activateAccount(any(AccountActivationRequest.class))).thenReturn(false);

        // When & Then
        mockMvc.perform(post("/api/account/activate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validActivationRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Invalid or expired activation token"));

        verify(activationService).activateAccount(any(AccountActivationRequest.class));
    }

    @Test
    void activateAccount_WithInvalidToken_ShouldReturnBadRequest() throws Exception {
        // Given
        AccountActivationRequest invalidRequest = new AccountActivationRequest(
                "", // Invalid: empty token
                "newPassword123"
        );

        // When & Then
        mockMvc.perform(post("/api/account/activate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(activationService, never()).activateAccount(any(AccountActivationRequest.class));
    }

    @Test
    void activateAccount_WithInvalidPassword_ShouldReturnBadRequest() throws Exception {
        // Given
        AccountActivationRequest invalidRequest = new AccountActivationRequest(
                validToken,
                "short" // Invalid: short password
        );

        // When & Then
        mockMvc.perform(post("/api/account/activate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(activationService, never()).activateAccount(any(AccountActivationRequest.class));
    }

    @Test
    void activateAccount_WithMissingFields_ShouldReturnBadRequest() throws Exception {
        // Given
        AccountActivationRequest invalidRequest = new AccountActivationRequest(
                null, // Invalid: null token
                null  // Invalid: null password
        );

        // When & Then
        mockMvc.perform(post("/api/account/activate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());

        verify(activationService, never()).activateAccount(any(AccountActivationRequest.class));
    }

    @Test
    void validateToken_WithValidToken_ShouldReturnValid() throws Exception {
        // Given
        when(activationService.isValidToken(validToken)).thenReturn(true);

        // When & Then
        mockMvc.perform(get("/api/account/activate/validate")
                        .param("token", validToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.valid").value(true))
                .andExpect(jsonPath("$.message").value("Token is valid"));

        verify(activationService).isValidToken(validToken);
    }

    @Test
    void validateToken_WithInvalidToken_ShouldReturnInvalid() throws Exception {
        // Given
        String invalidToken = "invalid-token";
        when(activationService.isValidToken(invalidToken)).thenReturn(false);

        // When & Then
        mockMvc.perform(get("/api/account/activate/validate")
                        .param("token", invalidToken))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.valid").value(false))
                .andExpect(jsonPath("$.message").value("Token is invalid or expired"));

        verify(activationService).isValidToken(invalidToken);
    }

    @Test
    void validateToken_WithEmptyToken_ShouldReturnBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/account/activate/validate")
                        .param("token", ""))
                .andExpect(status().isBadRequest());

        verify(activationService, never()).isValidToken(anyString());
    }

    @Test
    void validateToken_WithMissingToken_ShouldReturnBadRequest() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/account/activate/validate"))
                .andExpect(status().isBadRequest());

        verify(activationService, never()).isValidToken(anyString());
    }

    @Test
    void activateAccount_WithServiceException_ShouldReturnInternalServerError() throws Exception {
        // Given
        when(activationService.activateAccount(any(AccountActivationRequest.class)))
                .thenThrow(new RuntimeException("Service error"));

        // When & Then
        mockMvc.perform(post("/api/account/activate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validActivationRequest)))
                .andExpect(status().isInternalServerError());

        verify(activationService).activateAccount(any(AccountActivationRequest.class));
    }

    @Test
    void validateToken_WithServiceException_ShouldReturnInternalServerError() throws Exception {
        // Given
        when(activationService.isValidToken(anyString()))
                .thenThrow(new RuntimeException("Service error"));

        // When & Then
        mockMvc.perform(get("/api/account/activate/validate")
                        .param("token", validToken))
                .andExpect(status().isInternalServerError());

        verify(activationService).isValidToken(validToken);
    }

    @Test
    void activateAccount_WithWeakPassword_ShouldReturnBadRequest() throws Exception {
        // Given
        AccountActivationRequest weakPasswordRequest = new AccountActivationRequest(
                validToken,
                "123" // Invalid: too short password
        );

        // When & Then
        mockMvc.perform(post("/api/account/activate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(weakPasswordRequest)))
                .andExpect(status().isBadRequest());

        verify(activationService, never()).activateAccount(any(AccountActivationRequest.class));
    }

    @Test
    void activateAccount_WithSpecialCharactersInToken_ShouldWork() throws Exception {
        // Given
        String specialToken = "token-with-special-chars!@#$%^&*()";
        AccountActivationRequest specialTokenRequest = new AccountActivationRequest(
                specialToken,
                "newPassword123"
        );
        when(activationService.activateAccount(any(AccountActivationRequest.class))).thenReturn(true);

        // When & Then
        mockMvc.perform(post("/api/account/activate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(specialTokenRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        verify(activationService).activateAccount(any(AccountActivationRequest.class));
    }
} 