package com.vazant.logix.orders.presentation.controller.user;

import com.vazant.logix.orders.application.service.user.AccountActivationService;
import com.vazant.logix.orders.dto.user.AccountActivationRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST controller for user account activation and token validation.
 * <p>
 * Provides endpoints for account activation and token validation.
 */
@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountActivationController {

  private final AccountActivationService activationService;

  /**
   * Activates user account using activation token.
   *
   * @param request the activation request containing token and new password
   * @return activation result with success status and message
   */
  @PostMapping("/activate")
  public ResponseEntity<Map<String, Object>> activateAccount(
      @Valid @RequestBody AccountActivationRequest request) {
    
    boolean success = activationService.activateAccount(request);
    
    if (success) {
      return ResponseEntity.ok(Map.of(
          "success", true,
          "message", "Account activated successfully"
      ));
    } else {
      return ResponseEntity.badRequest().body(Map.of(
          "success", false,
          "message", "Invalid or expired activation token"
      ));
    }
  }

  /**
   * Checks the validity of the activation token.
   *
   * @param token the activation token to validate
   * @return validation result with validity status and message
   */
  @GetMapping("/activate/validate")
  public ResponseEntity<Map<String, Object>> validateToken(@RequestParam String token) {
    boolean isValid = activationService.isValidToken(token);
    
    return ResponseEntity.ok(Map.of(
        "valid", isValid,
        "message", isValid ? "Token is valid" : "Token is invalid or expired"
    ));
  }
} 