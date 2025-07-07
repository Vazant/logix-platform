package com.vazant.logix.orders.infrastructure.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.StaticMessageSource;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

class MessageUtilsTest {

    private MessageUtils messageUtils;
    private StaticMessageSource messageSource;

    @BeforeEach
    void setUp() {
        messageSource = new StaticMessageSource();
        messageUtils = new MessageUtils(messageSource);
        
        // Добавляем тестовые сообщения
        messageSource.addMessage("test.message", Locale.ENGLISH, "Test message");
        messageSource.addMessage("test.message", Locale.of("ru"), "Тестовое сообщение");
        messageSource.addMessage("test.with.params", Locale.ENGLISH, "Hello {0}, you are {1} years old");
        messageSource.addMessage("test.with.params", Locale.of("ru"), "Привет {0}, тебе {1} лет");
        messageSource.addMessage("validation.common.required", Locale.ENGLISH, "This field is required");
        messageSource.addMessage("validation.common.required", Locale.of("ru"), "Это поле обязательно");
        messageSource.addMessage("error.common.notFound", Locale.ENGLISH, "Resource not found");
        messageSource.addMessage("error.common.notFound", Locale.of("ru"), "Ресурс не найден");
        messageSource.addMessage("success.test", Locale.ENGLISH, "Operation successful");
        messageSource.addMessage("info.test", Locale.ENGLISH, "Information message");
        messageSource.addMessage("warning.test", Locale.ENGLISH, "Warning message");
        messageSource.addMessage("ui.test", Locale.ENGLISH, "UI message");
        messageSource.addMessage("system.test", Locale.ENGLISH, "System message");
        messageSource.addMessage("email.test", Locale.ENGLISH, "Email message");
    }

    @Test
    void getMessage_WithValidCode_ReturnsMessage() {
        String message = messageUtils.getMessage("test.message");
        assertEquals("Test message", message);
    }

    @Test
    void getMessage_WithValidCodeAndParams_ReturnsFormattedMessage() {
        String message = messageUtils.getMessage("test.with.params", "John", 25);
        assertEquals("Hello John, you are 25 years old", message);
    }

    @Test
    void getMessage_WithValidCodeAndLocale_ReturnsLocalizedMessage() {
        String message = messageUtils.getMessage("test.message", Locale.of("ru"));
        assertEquals("Тестовое сообщение", message);
    }

    @Test
    void getMessage_WithValidCodeAndParamsAndLocale_ReturnsLocalizedFormattedMessage() {
        String message = messageUtils.getMessage("test.with.params", new Object[]{"Иван", 30}, Locale.of("ru"));
        assertEquals("Привет Иван, тебе 30 лет", message);
    }

    @Test
    void getMessage_WithInvalidCode_ReturnsCode() {
        String message = messageUtils.getMessage("invalid.code");
        assertEquals("invalid.code", message);
    }

    @Test
    void getValidationMessage_WithValidCode_ReturnsValidationMessage() {
        String message = messageUtils.getValidationMessage("common.required");
        assertEquals("This field is required", message);
    }

    @Test
    void getErrorMessage_WithValidCode_ReturnsErrorMessage() {
        String message = messageUtils.getErrorMessage("common.notFound");
        assertEquals("Resource not found", message);
    }

    @Test
    void getSuccessMessage_WithValidCode_ReturnsSuccessMessage() {
        String message = messageUtils.getSuccessMessage("test");
        assertEquals("Operation successful", message);
    }

    @Test
    void getInfoMessage_WithValidCode_ReturnsInfoMessage() {
        String message = messageUtils.getInfoMessage("test");
        assertEquals("Information message", message);
    }

    @Test
    void getWarningMessage_WithValidCode_ReturnsWarningMessage() {
        String message = messageUtils.getWarningMessage("test");
        assertEquals("Warning message", message);
    }

    @Test
    void getUiMessage_WithValidCode_ReturnsUiMessage() {
        String message = messageUtils.getUiMessage("test");
        assertEquals("UI message", message);
    }

    @Test
    void getSystemMessage_WithValidCode_ReturnsSystemMessage() {
        String message = messageUtils.getSystemMessage("test");
        assertEquals("System message", message);
    }

    @Test
    void getEmailMessage_WithValidCode_ReturnsEmailMessage() {
        String message = messageUtils.getEmailMessage("test");
        assertEquals("Email message", message);
    }

    @Test
    void hasMessage_WithValidCode_ReturnsTrue() {
        assertTrue(messageUtils.hasMessage("test.message"));
    }

    @Test
    void hasMessage_WithInvalidCode_ReturnsFalse() {
        assertFalse(messageUtils.hasMessage("invalid.code"));
    }

    @Test
    void getCurrentLocale_ReturnsCurrentLocale() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        assertEquals(Locale.ENGLISH, messageUtils.getCurrentLocale());
    }

    @Test
    void setLocale_SetsLocaleForCurrentThread() {
        Locale russianLocale = Locale.of("ru");
        messageUtils.setLocale(russianLocale);
        assertEquals(russianLocale, messageUtils.getCurrentLocale());
    }

    @Test
    void clearLocale_ClearsLocaleForCurrentThread() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        messageUtils.clearLocale();
        assertNull(messageUtils.getCurrentLocale());
    }

    @Test
    void getMessage_WithNullCode_ReturnsCode() {
        String message = messageUtils.getMessage(null);
        assertNull(message);
    }

    @Test
    void getMessage_WithEmptyCode_ReturnsCode() {
        String message = messageUtils.getMessage("");
        assertEquals("", message);
    }

    @Test
    void getMessage_WithNullArgs_ReturnsMessage() {
        String message = messageUtils.getMessage("test.message", (Object[]) null);
        assertEquals("Test message", message);
    }

    @Test
    void getMessage_WithEmptyArgs_ReturnsMessage() {
        String message = messageUtils.getMessage("test.message", new Object[0]);
        assertEquals("Test message", message);
    }
} 