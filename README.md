# Logix Platform

Микросервисная платформа для управления заказами, построенная на Spring Boot 3 и Java 21.

## 📊 Качество проекта

**Статус:** 🟠 Средний  
**Последний аудит:** 2025-01-27  

Подробный анализ качества проекта доступен в [README_ANALYSIS.md](./README_ANALYSIS.md) и [PROJECT_AUDIT.md](./PROJECT_AUDIT.md).

## 🚀 Быстрый старт

```bash
# Клонирование репозитория
git clone <repository-url>
cd logix-platform

# Запуск с помощью Docker Compose
docker-compose up -d

# Или запуск отдельных сервисов
mvn clean install
java -jar logix-orders/target/logix-orders-1.0.0-SNAPSHOT.jar
```

## 📁 Структура проекта

```
logix-platform/
├── logix-orders/          # Основной сервис управления заказами
├── logix-currency/        # Сервис конвертации валют
├── logix-devtools/        # Инструменты разработки
├── logix-data-gen/        # Генерация тестовых данных
├── logix-kafka-shared/    # Общие DTO для Kafka
└── infra/                 # Инфраструктурные компоненты
```

## 🛠 Технологии

- **Java 21**
- **Spring Boot 3.3.10**
- **PostgreSQL**
- **Redis**
- **Apache Kafka**
- **Maven**

## 📚 Документация

- [Анализ качества проекта](./README_ANALYSIS.md)
- [Подробный аудит](./PROJECT_AUDIT.md)
- [Система активации аккаунтов](./logix-orders/ACCOUNT_ACTIVATION_README.md)

## 🔧 Разработка

### Требования
- Java 21+
- Maven 3.8+
- Docker & Docker Compose
- PostgreSQL
- Redis
- Apache Kafka

### Настройка окружения
1. Установите зависимости
2. Настройте базу данных
3. Запустите инфраструктурные сервисы
4. Запустите приложения

## 📞 Поддержка

Для вопросов и предложений обращайтесь к команде разработки.