# Система активации аккаунтов

## Обзор

Система активации аккаунтов позволяет пользователям активировать свои аккаунты по ссылке, отправленной на email. При создании организации и супер-администратора автоматически генерируется токен активации и отправляется email с ссылкой для активации.

## Компоненты системы

### 1. Доменная модель
- `ActivationToken` - сущность для хранения токенов активации
- Связь с пользователем через `user_uuid`
- Поля: `token`, `expiresAt`, `used`

### 2. Репозиторий
- `ActivationTokenRepository` - для работы с токенами в базе данных
- Методы для поиска, удаления и очистки токенов

### 3. Сервис
- `AccountActivationService` - основная логика активации
- Создание токенов, активация аккаунтов, валидация

### 4. Контроллеры
- `AccountActivationController` - REST API для активации
- `ActivationPageController` - отображение HTML страницы

### 5. DTO
- `AccountActivationRequest` - запрос на активацию аккаунта

## API Endpoints

### POST /api/account/activate
Активирует аккаунт пользователя.

**Request Body:**
```json
{
  "token": "uuid-token-here",
  "newPassword": "new-password-here"
}
```

**Response:**
```json
{
  "success": true,
  "message": "Account activated successfully"
}
```

### GET /api/account/activate/validate?token=uuid-token-here
Проверяет валидность токена активации.

**Response:**
```json
{
  "valid": true,
  "message": "Token is valid"
}
```

### GET /activate?token=uuid-token-here
Отображает HTML страницу для активации аккаунта.

## Процесс активации

1. **Создание организации и супер-администратора:**
   - При вызове `OrganizationService.createWithSuperAdmin()`
   - Создается пользователь с временным паролем
   - Генерируется токен активации (действителен 24 часа)
   - Отправляется email с ссылкой активации

2. **Активация аккаунта:**
   - Пользователь переходит по ссылке из email
   - Открывается HTML страница с формой для ввода нового пароля
   - При отправке формы вызывается API активации
   - Токен проверяется на валидность и срок действия
   - Пароль пользователя обновляется, аккаунт активируется
   - Токен помечается как использованный

## Конфигурация

В `application.yml` можно настроить базовый URL для ссылок активации:

```yaml
app:
  activation:
    base-url: https://your-domain.com
```

## Безопасность

- Токены действительны только 24 часа
- Токен может быть использован только один раз
- Пароль должен содержать минимум 8 символов
- Просроченные токены автоматически удаляются

## База данных

Таблица `activation_tokens` создается автоматически через Liquibase миграцию:

```sql
CREATE TABLE activation_tokens (
    uuid UUID NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
    token VARCHAR(255) NOT NULL,
    user_uuid UUID NOT NULL,
    expires_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    used BOOLEAN NOT NULL DEFAULT FALSE,
    CONSTRAINT pk_activation_tokens PRIMARY KEY (uuid),
    CONSTRAINT uk_activation_tokens_token UNIQUE (token),
    CONSTRAINT fk_activation_tokens_user FOREIGN KEY (user_uuid) REFERENCES users (uuid) ON DELETE CASCADE
);
```

## Использование

1. Запустите приложение
2. Создайте организацию через API
3. Проверьте email супер-администратора
4. Перейдите по ссылке активации
5. Введите новый пароль
6. Аккаунт активирован и готов к использованию 