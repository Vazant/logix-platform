-- liquibase formatted sql

-- changeset Vazant:1747885706575-1
CREATE TABLE categories
(
    uuid        UUID NOT NULL,
    created_at  TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
    updated_at  TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
    name        VARCHAR(255),
    description VARCHAR(1000),
    CONSTRAINT pk_categories PRIMARY KEY (uuid)
);

-- changeset Vazant:1747885706575-2
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

-- changeset Vazant:1747885706575-3
CREATE TABLE images
(
    uuid       UUID         NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
    updated_at TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
    url        VARCHAR(255) NOT NULL,
    alt_text   VARCHAR(255) NOT NULL,
    CONSTRAINT pk_images PRIMARY KEY (uuid)
);

-- changeset Vazant:1747885706575-4
CREATE TABLE order_items
(
    uuid         UUID    NOT NULL,
    created_at   TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
    updated_at   TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
    order_uuid   UUID    NOT NULL,
    product_uuid UUID    NOT NULL,
    quantity     INTEGER NOT NULL,
    amount       DECIMAL,
    currency     VARCHAR(255),
    CONSTRAINT pk_order_items PRIMARY KEY (uuid)
);

-- changeset Vazant:1747885706575-5
CREATE TABLE orders
(
    uuid          UUID         NOT NULL,
    created_at    TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
    updated_at    TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
    customer_uuid UUID         NOT NULL,
    warehouse_id  VARCHAR(255) NOT NULL,
    description   VARCHAR(255),
    status        VARCHAR(255) NOT NULL,
    amount        DECIMAL,
    currency      VARCHAR(255),
    CONSTRAINT pk_orders PRIMARY KEY (uuid)
);

-- changeset Vazant:1747885706575-6
CREATE TABLE organizations
(
    uuid         UUID         NOT NULL,
    created_at   TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
    updated_at   TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
    name         VARCHAR(255) NOT NULL,
    email        VARCHAR(255) NOT NULL,
    address      VARCHAR(255),
    phone_number VARCHAR(255),
    CONSTRAINT pk_organizations PRIMARY KEY (uuid)
);

-- changeset Vazant:1747885706575-7
CREATE TABLE persons
(
    uuid         UUID         NOT NULL,
    created_at   TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
    updated_at   TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
    first_name   VARCHAR(255) NOT NULL,
    last_name    VARCHAR(255) NOT NULL,
    phone_number VARCHAR(255),
    birth_date   date,
    CONSTRAINT pk_persons PRIMARY KEY (uuid)
);

-- changeset Vazant:1747885706575-8
CREATE TABLE product_prices
(
    uuid         UUID NOT NULL,
    created_at   TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
    updated_at   TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
    product_uuid UUID NOT NULL,
    amount       DECIMAL,
    currency     VARCHAR(255),
    CONSTRAINT pk_product_prices PRIMARY KEY (uuid)
);

-- changeset Vazant:1747885706575-9
CREATE TABLE products
(
    uuid           UUID         NOT NULL,
    created_at     TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
    updated_at     TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
    name           VARCHAR(255),
    description    VARCHAR(1000),
    sku_code       VARCHAR(255) NOT NULL,
    stock_quantity INTEGER      NOT NULL,
    category_id    UUID         NOT NULL,
    image_id       UUID,
    width          DECIMAL,
    height         DECIMAL,
    length         DECIMAL,
    weight         DECIMAL,
    CONSTRAINT pk_products PRIMARY KEY (uuid)
);

-- changeset Vazant:1747885706575-10
CREATE TABLE user_groups
(
    uuid         UUID         NOT NULL,
    created_at   TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
    updated_at   TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
    group_name   VARCHAR(255) NOT NULL,
    email        VARCHAR(255) NOT NULL,
    organization VARCHAR(255),
    description  VARCHAR(255),
    CONSTRAINT pk_user_groups PRIMARY KEY (uuid)
);

-- changeset Vazant:1747885706575-11
CREATE TABLE user_responsibilities
(
    uuid           UUID         NOT NULL,
    created_at     TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
    updated_at     TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
    responsibility VARCHAR(255) NOT NULL,
    CONSTRAINT pk_user_responsibilities PRIMARY KEY (uuid)
);

-- changeset Vazant:1747885706575-12
CREATE TABLE users
(
    uuid              UUID         NOT NULL,
    created_at        TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
    updated_at        TIMESTAMP WITHOUT TIME ZONE DEFAULT NOW() NOT NULL,
    username          VARCHAR(255) NOT NULL,
    email             VARCHAR(255) NOT NULL,
    password          VARCHAR(255) NOT NULL,
    picture_url       VARCHAR(255),
    enabled           BOOLEAN      NOT NULL,
    person_uuid       UUID,
    organization_uuid UUID,
    CONSTRAINT pk_users PRIMARY KEY (uuid)
);

-- changeset Vazant:1747885706575-13
ALTER TABLE customers
    ADD CONSTRAINT uc_customers_email UNIQUE (email);

-- changeset Vazant:1747885706575-14
ALTER TABLE organizations
    ADD CONSTRAINT uc_organizations_name UNIQUE (name);

-- changeset Vazant:1747885706575-15
ALTER TABLE products
    ADD CONSTRAINT uc_products_skucode UNIQUE (sku_code);

-- changeset Vazant:1747885706575-16
ALTER TABLE user_groups
    ADD CONSTRAINT uc_user_groups_groupname UNIQUE (group_name);

-- changeset Vazant:1747885706575-17
ALTER TABLE users
    ADD CONSTRAINT uc_users_email UNIQUE (email);

-- changeset Vazant:1747885706575-18
ALTER TABLE users
    ADD CONSTRAINT uc_users_person_uuid UNIQUE (person_uuid);

-- changeset Vazant:1747885706575-19
ALTER TABLE users
    ADD CONSTRAINT uc_users_username UNIQUE (username);

-- changeset Vazant:1747885706575-20
ALTER TABLE orders
    ADD CONSTRAINT FK_ORDERS_ON_CUSTOMER_UUID FOREIGN KEY (customer_uuid) REFERENCES customers (uuid);

-- changeset Vazant:1747885706575-21
ALTER TABLE order_items
    ADD CONSTRAINT FK_ORDER_ITEMS_ON_ORDER_UUID FOREIGN KEY (order_uuid) REFERENCES orders (uuid);

-- changeset Vazant:1747885706575-22
ALTER TABLE order_items
    ADD CONSTRAINT FK_ORDER_ITEMS_ON_PRODUCT_UUID FOREIGN KEY (product_uuid) REFERENCES products (uuid);

-- changeset Vazant:1747885706575-23
ALTER TABLE product_prices
    ADD CONSTRAINT FK_PRODUCT_PRICES_ON_PRODUCT_UUID FOREIGN KEY (product_uuid) REFERENCES products (uuid);

-- changeset Vazant:1747885706575-24
ALTER TABLE users
    ADD CONSTRAINT FK_USERS_ON_ORGANIZATION_UUID FOREIGN KEY (organization_uuid) REFERENCES organizations (uuid);

-- changeset Vazant:1747885706575-25
ALTER TABLE users
    ADD CONSTRAINT FK_USERS_ON_PERSON_UUID FOREIGN KEY (person_uuid) REFERENCES persons (uuid);

