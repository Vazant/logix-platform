package com.vazant.logix.orders.presentation.controller.user;

import com.vazant.logix.orders.application.service.user.AccountActivationService;
import com.vazant.logix.orders.dto.user.AccountActivationRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
@Slf4j
public class AccountActivationController {

  private final AccountActivationService activationService;

  /**
   * Активирует аккаунт пользователя
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
   * Проверяет валидность токена активации
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