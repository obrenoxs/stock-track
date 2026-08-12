CREATE TABLE locations (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    corridor   VARCHAR(50)  NOT NULL,
    shelf      VARCHAR(50)  NOT NULL,
    drawer     VARCHAR(50)  NOT NULL,
    created_at DATETIME     NOT NULL,
    updated_at DATETIME     NOT NULL,
    CONSTRAINT uk_locations_corridor_shelf_drawer UNIQUE (corridor, shelf, drawer)
);