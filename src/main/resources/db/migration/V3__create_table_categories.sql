CREATE TABLE categories (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(100)   NOT NULL,
    created_at DATETIME       NOT NULL,
    updated_at DATETIME       NOT NULL,
    CONSTRAINT uk_categories_name UNIQUE (name)
);