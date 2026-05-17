-- NexusStore Database Schema – Milestone 4
-- Spring Boot runs this automatically on startup via spring.sql.init.mode=always
-- CREATE TABLE IF NOT EXISTS is idempotent; safe to re-run.

CREATE TABLE IF NOT EXISTS users (
    id            INT AUTO_INCREMENT PRIMARY KEY,
    first_name    VARCHAR(100)  NOT NULL,
    last_name     VARCHAR(100)  NOT NULL,
    email         VARCHAR(255)  NOT NULL,
    username      VARCHAR(20)   NOT NULL UNIQUE,
    password      VARCHAR(255)  NOT NULL,
    role          VARCHAR(20)   NOT NULL DEFAULT 'USER'
);

CREATE TABLE IF NOT EXISTS products (
    product_id     INT AUTO_INCREMENT PRIMARY KEY,
    name           VARCHAR(255)   NOT NULL,
    description    TEXT           NOT NULL,
    price          DECIMAL(10,2)  NOT NULL,
    stock_quantity INT            NOT NULL DEFAULT 0,
    category       VARCHAR(50)    NOT NULL,
    image_url      VARCHAR(500),
    date_added     DATE           NOT NULL
);
