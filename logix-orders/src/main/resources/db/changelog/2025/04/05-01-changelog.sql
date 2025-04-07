-- liquibase formatted sql

-- changeset Vazant:1743806345747-1
CREATE TABLE categories
(
    uuid        UUID NOT NULL,
    created_at  TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
    updated_at  TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
    name        VARCHAR(255),
    description VARCHAR(1000),
    CONSTRAINT pk_categories PRIMARY KEY (uuid)
);

-- changeset Vazant:1743806345747-2
CREATE TABLE customers
(
    uuid       UUID         NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
    first_name VARCHAR(100),
    last_name  VARCHAR(100),
    email      VARCHAR(255) NOT NULL,
    phone      VARCHAR(255),
    address    VARCHAR(255),
    city       VARCHAR(100),
    state      VARCHAR(100),
    zip        VARCHAR(20),
    country    VARCHAR(100),
    CONSTRAINT pk_customers PRIMARY KEY (uuid)
);

-- changeset Vazant:1743806345747-3
CREATE TABLE images
(
    uuid       UUID         NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
    url        VARCHAR(255) NOT NULL,
    alt_text   VARCHAR(255) NOT NULL,
    CONSTRAINT pk_images PRIMARY KEY (uuid)
);

-- changeset Vazant:1743806345747-4
CREATE TABLE order_items
(
    uuid         UUID    NOT NULL,
    created_at   TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
    updated_at   TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
    order_uuid   UUID    NOT NULL,
    product_uuid UUID    NOT NULL,
    quantity     INTEGER NOT NULL,
    amount       DECIMAL NOT NULL,
    currency     VARCHAR(255) NOT NULL,
    CONSTRAINT pk_order_items PRIMARY KEY (uuid)
);

-- changeset Vazant:1743806345747-5
ALTER TABLE orders
    RENAME COLUMN customer_id TO customer_uuid,
    RENAME COLUMN warehouse_id TO warehouse_uuid;

ALTER TABLE orders
ALTER COLUMN customer_uuid TYPE UUID USING customer_uuid::uuid,
    ALTER COLUMN customer_uuid SET NOT NULL,
    ALTER COLUMN warehouse_uuid TYPE VARCHAR(255),
    ALTER COLUMN warehouse_uuid SET NOT NULL,
    ALTER COLUMN description TYPE VARCHAR(255),
    ALTER COLUMN amount TYPE DECIMAL USING amount::decimal,
    ADD COLUMN status VARCHAR(255) NOT NULL,
    ADD COLUMN currency VARCHAR(255);

-- changeset Vazant:1743806345747-6
CREATE TABLE product_prices
(
    uuid         UUID NOT NULL,
    created_at   TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
    updated_at   TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
    product_uuid UUID NOT NULL,
    amount       DECIMAL NOT NULL,
    currency     VARCHAR(255) NOT NULL,
    CONSTRAINT pk_product_prices PRIMARY KEY (uuid)
);

-- changeset Vazant:1743806345747-7
CREATE TABLE products
(
    uuid           UUID         NOT NULL,
    created_at     TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
    updated_at     TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
    name           VARCHAR(255),
    description    VARCHAR(1000),
    sku_code       VARCHAR(255) NOT NULL,
    stock_quantity INTEGER      NOT NULL,
    category_uuid    UUID         NOT NULL,
    image_uuid       UUID,
    width          DECIMAL,
    height         DECIMAL,
    length         DECIMAL,
    weight         DECIMAL,
    CONSTRAINT pk_products PRIMARY KEY (uuid)
);

-- changeset Vazant:1743806345747-8
ALTER TABLE customers
    ADD CONSTRAINT uc_customers_email UNIQUE (email);

-- changeset Vazant:1743806345747-9
ALTER TABLE products
    ADD CONSTRAINT uc_products_skucode UNIQUE (sku_code);

-- changeset Vazant:1743806345747-10
ALTER TABLE orders
    ADD CONSTRAINT FK_ORDERS_ON_CUSTOMER FOREIGN KEY (customer_uuid) REFERENCES customers (uuid);

-- changeset Vazant:1743806345747-11
ALTER TABLE order_items
    ADD CONSTRAINT FK_ORDER_ITEMS_ON_ORDER FOREIGN KEY (order_uuid) REFERENCES orders (uuid);

-- changeset Vazant:1743806345747-12
ALTER TABLE order_items
    ADD CONSTRAINT FK_ORDER_ITEMS_ON_PRODUCT FOREIGN KEY (product_uuid) REFERENCES products (uuid);

-- changeset Vazant:1743806345747-13
ALTER TABLE product_prices
    ADD CONSTRAINT FK_PRODUCT_PRICES_ON_PRODUCT FOREIGN KEY (product_uuid) REFERENCES products (uuid);
