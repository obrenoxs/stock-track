CREATE TABLE users (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(150)   NOT NULL,
    re         VARCHAR(30)    NOT NULL,
    area       VARCHAR(100)   NOT NULL,
    password   VARCHAR(255)   NOT NULL,
    role       VARCHAR(20)    NOT NULL,
    created_at DATETIME       NOT NULL,
    updated_at DATETIME       NOT NULL,
    CONSTRAINT uk_users_re UNIQUE (re)
);