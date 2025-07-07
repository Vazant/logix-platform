package com.vazant.logix.orders.infrastructure.utils;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * Утилита для работы с интернационализированными сообщениями.
 * Предоставляет удобные методы для получения локализованных сообщений.
 */
@Component
public class MessageUtils {

    private final MessageSource messageSource;

    public MessageUtils(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * Получает сообщение по ключу с текущей локалью.
     *
     * @param code ключ сообщения
     * @return локализованное сообщение
     */
    public String getMessage(String code) {
        return getMessage(code, (Object[]) null);
    }

    /**
     * Получает сообщение по ключу с параметрами и текущей локалью.
     *
     * @param code ключ сообщения
     * @param args параметры для подстановки
     * @return локализованное сообщение
     */
    public String getMessage(String code, Object... args) {
        return getMessage(code, args, LocaleContextHolder.getLocale());
    }

    /**
     * Получает сообщение по ключу с параметрами и указанной локалью.
     *
     * @param code ключ сообщения
     * @param args параметры для подстановки
     * @param locale локаль
     * @return локализованное сообщение
     */
    public String getMessage(String code, Object[] args, Locale locale) {
        return messageSource.getMessage(code, args, code, locale);
    }

    /**
     * Получает сообщение по ключу с указанной локалью.
     *
     * @param code ключ сообщения
     * @param locale локаль
     * @return локализованное сообщение
     */
    public String getMessage(String code, Locale locale) {
        return getMessage(code, (Object[]) null, locale);
    }

    /**
     * Получает сообщение валидации.
     *
     * @param code ключ сообщения валидации
     * @param args параметры для подстановки
     * @return локализованное сообщение валидации
     */
    public String getValidationMessage(String code, Object... args) {
        return getMessage("validation." + code, args);
    }

    /**
     * Получает сообщение об ошибке.
     *
     * @param code ключ сообщения об ошибке
     * @param args параметры для подстановки
     * @return локализованное сообщение об ошибке
     */
    public String getErrorMessage(String code, Object... args) {
        return getMessage("error." + code, args);
    }

    /**
     * Получает сообщение успеха.
     *
     * @param code ключ сообщения успеха
     * @param args параметры для подстановки
     * @return локализованное сообщение успеха
     */
    public String getSuccessMessage(String code, Object... args) {
        return getMessage("success." + code, args);
    }

    /**
     * Получает информационное сообщение.
     *
     * @param code ключ информационного сообщения
     * @param args параметры для подстановки
     * @return локализованное информационное сообщение
     */
    public String getInfoMessage(String code, Object... args) {
        return getMessage("info." + code, args);
    }

    /**
     * Получает предупреждающее сообщение.
     *
     * @param code ключ предупреждающего сообщения
     * @param args параметры для подстановки
     * @return локализованное предупреждающее сообщение
     */
    public String getWarningMessage(String code, Object... args) {
        return getMessage("warning." + code, args);
    }

    /**
     * Получает сообщение интерфейса.
     *
     * @param code ключ сообщения интерфейса
     * @param args параметры для подстановки
     * @return локализованное сообщение интерфейса
     */
    public String getUiMessage(String code, Object... args) {
        return getMessage("ui." + code, args);
    }

    /**
     * Получает системное сообщение.
     *
     * @param code ключ системного сообщения
     * @param args параметры для подстановки
     * @return локализованное системное сообщение
     */
    public String getSystemMessage(String code, Object... args) {
        return getMessage("system." + code, args);
    }

    /**
     * Получает сообщение email.
     *
     * @param code ключ сообщения email
     * @param args параметры для подстановки
     * @return локализованное сообщение email
     */
    public String getEmailMessage(String code, Object... args) {
        return getMessage("email." + code, args);
    }

    /**
     * Проверяет, существует ли сообщение с указанным ключом.
     *
     * @param code ключ сообщения
     * @return true, если сообщение существует
     */
    public boolean hasMessage(String code) {
        try {
            String message = getMessage(code);
            return !code.equals(message); // Если сообщение не найдено, возвращается сам код
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Получает текущую локаль.
     *
     * @return текущая локаль
     */
    public Locale getCurrentLocale() {
        return LocaleContextHolder.getLocale();
    }

    /**
     * Устанавливает локаль для текущего потока.
     *
     * @param locale локаль для установки
     */
    public void setLocale(Locale locale) {
        LocaleContextHolder.setLocale(locale);
    }

    /**
     * Очищает локаль для текущего потока.
     */
    public void clearLocale() {
        LocaleContextHolder.resetLocaleContext();
    }
} 