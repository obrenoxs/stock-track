CREATE TABLE tool_types (
    id                    BIGINT AUTO_INCREMENT PRIMARY KEY,
    name                  VARCHAR(150)   NOT NULL,
    brand                 VARCHAR(100)   NOT NULL,
    model                 VARCHAR(100)   NOT NULL,
    description           TEXT           NULL,
    minimum_stock         INT            NOT NULL,
    requires_calibration  BOOLEAN        NOT NULL DEFAULT FALSE,
    created_at            DATETIME       NOT NULL,
    updated_at            DATETIME       NOT NULL
);