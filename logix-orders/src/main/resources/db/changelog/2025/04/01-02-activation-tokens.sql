--liquibase formatted sql

-- changeset Vazant:1747885706576-1
CREATE TABLE activation_tokens
(
    uuid       UUID         NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
    token      VARCHAR(255) NOT NULL,
    user_uuid  UUID         NOT NULL,
    expires_at TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    used       BOOLEAN      NOT NULL DEFAULT FALSE,
    CONSTRAINT pk_activation_tokens PRIMARY KEY (uuid),
    CONSTRAINT uk_activation_tokens_token UNIQUE (token),
    CONSTRAINT fk_activation_tokens_user FOREIGN KEY (user_uuid) REFERENCES users (uuid) ON DELETE CASCADE
);

-- changeset Vazant:1747885706576-2
CREATE INDEX idx_activation_tokens_token ON activation_tokens (token);
CREATE INDEX idx_activation_tokens_user_uuid ON activation_tokens (user_uuid);
CREATE INDEX idx_activation_tokens_expires_at ON activation_tokens (expires_at); 