package com.vazant.logix.orders.infrastructure.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

/**
 * Утилита для работы с UUID.
 * Предоставляет методы для парсинга, валидации и генерации UUID.
 */
public final class UuidUtils {

    private static final Pattern UUID_PATTERN = Pattern.compile(
        "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$"
    );
    
    private static final Pattern COMPACT_UUID_PATTERN = Pattern.compile("^[0-9a-fA-F]{32}$");
    
    private static final ConcurrentHashMap<String, UUID> CACHE = new ConcurrentHashMap<>();
    private static final int MAX_CACHE_SIZE = 1000;

    private UuidUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Парсит строку в UUID.
     *
     * @param uuidString строка для парсинга
     * @return UUID
     * @throws IllegalArgumentException если строка не является валидным UUID
     */
    public static UUID parse(String uuidString) {
        if (uuidString == null || uuidString.trim().isEmpty()) {
            throw new IllegalArgumentException("UUID string cannot be null or empty");
        }

        // Проверяем кэш
        UUID cached = CACHE.get(uuidString);
        if (cached != null) {
            return cached;
        }

        String normalized = uuidString.trim();
        
        if (!isValidUuid(normalized)) {
            throw new IllegalArgumentException("Invalid UUID format: " + uuidString);
        }

        try {
            UUID uuid = UUID.fromString(normalized);
            
            // Кэшируем результат
            if (CACHE.size() < MAX_CACHE_SIZE) {
                CACHE.put(uuidString, uuid);
            }
            
            return uuid;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid UUID format: " + uuidString, e);
        }
    }

    /**
     * Парсит строку в UUID, возвращает null если парсинг не удался.
     *
     * @param uuidString строка для парсинга
     * @return UUID или null
     */
    public static UUID parseOrNull(String uuidString) {
        try {
            return parse(uuidString);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Проверяет, является ли строка валидным UUID.
     *
     * @param uuidString строка для проверки
     * @return true, если строка является валидным UUID
     */
    public static boolean isValidUuid(String uuidString) {
        if (uuidString == null || uuidString.trim().isEmpty()) {
            return false;
        }
        
        String normalized = uuidString.trim();
        return UUID_PATTERN.matcher(normalized).matches();
    }

    /**
     * Нормализует UUID строку (убирает пробелы, приводит к нижнему регистру).
     *
     * @param uuidString строка для нормализации
     * @return нормализованная строка
     */
    public static String normalize(String uuidString) {
        if (uuidString == null) {
            return null;
        }
        return uuidString.trim().toLowerCase();
    }

    /**
     * Генерирует новый UUID.
     *
     * @return новый UUID
     */
    public static UUID generate() {
        return UUID.randomUUID();
    }

    /**
     * Генерирует UUID на основе имени (использует SHA-256 хеш).
     *
     * @param name имя для генерации UUID
     * @return UUID на основе имени
     * @throws IllegalArgumentException если имя null или пустое
     */
    public static UUID generateFromName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(name.getBytes(StandardCharsets.UTF_8));
            
            // Берем первые 16 байт для UUID
            long msb = 0;
            long lsb = 0;
            
            for (int i = 0; i < 8; i++) {
                msb = (msb << 8) | (hash[i] & 0xff);
            }
            
            for (int i = 8; i < 16; i++) {
                lsb = (lsb << 8) | (hash[i] & 0xff);
            }
            
            // Устанавливаем версию (4) и вариант (2)
            msb &= ~(0xf << 12);
            msb |= 4 << 12;
            lsb &= ~(0x3L << 62);
            lsb |= 2L << 62;
            
            return new UUID(msb, lsb);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }

    /**
     * Конвертирует UUID в компактную строку (без дефисов).
     *
     * @param uuid UUID для конвертации
     * @return компактная строка UUID
     * @throws IllegalArgumentException если UUID null
     */
    public static String toCompactString(UUID uuid) {
        if (uuid == null) {
            throw new IllegalArgumentException("UUID cannot be null");
        }
        return uuid.toString().replace("-", "");
    }

    /**
     * Парсит компактную строку UUID (без дефисов).
     *
     * @param compactString компактная строка UUID
     * @return UUID
     * @throws IllegalArgumentException если строка не является валидным компактным UUID
     */
    public static UUID parseCompact(String compactString) {
        if (compactString == null || compactString.length() != 32) {
            throw new IllegalArgumentException("Compact UUID must be exactly 32 characters");
        }
        
        if (!COMPACT_UUID_PATTERN.matcher(compactString).matches()) {
            throw new IllegalArgumentException("Invalid compact UUID format: " + compactString);
        }
        
        // Вставляем дефисы в правильные позиции
        String formatted = compactString.substring(0, 8) + "-" +
                          compactString.substring(8, 12) + "-" +
                          compactString.substring(12, 16) + "-" +
                          compactString.substring(16, 20) + "-" +
                          compactString.substring(20, 32);
        
        return UUID.fromString(formatted);
    }

    /**
     * Очищает кэш UUID.
     */
    public static void clearCache() {
        CACHE.clear();
    }

    /**
     * Получает размер кэша UUID.
     *
     * @return размер кэша
     */
    public static int getCacheSize() {
        return CACHE.size();
    }
}
