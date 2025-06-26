package com.vazant.logix.orders.presentation.controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ActivationPageController {

  /**
   * Отображает страницу активации аккаунта
   */
  @GetMapping("/activate")
  public String showActivationPage() {
    return "activate";
  }
} 