-- liquibase formatted sql

-- changeset Vazant:1743536053373-1
CREATE TABLE orders
(
    uuid         UUID PRIMARY KEY,
    customer_id  TEXT,
    warehouse_id TEXT,
    amount       DOUBLE PRECISION,
    description  TEXT,
    created_at   TIMESTAMP WITHOUT TIME ZONE,
    updated_at   TIMESTAMP WITHOUT TIME ZONE
);
