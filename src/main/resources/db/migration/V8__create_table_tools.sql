CREATE TABLE tools (
    id                     BIGINT AUTO_INCREMENT PRIMARY KEY,
    serial_number          VARCHAR(100)  NOT NULL,
    status                 VARCHAR(20)   NOT NULL,
    last_calibration_date  DATE          NULL,
    next_calibration_date  DATE          NULL,
    tool_type_id           BIGINT        NOT NULL,
    location_id            BIGINT        NULL,
    created_at             DATETIME      NOT NULL,
    updated_at             DATETIME      NOT NULL,
    CONSTRAINT uk_tools_serial_number UNIQUE (serial_number),
    CONSTRAINT fk_tools_tool_type FOREIGN KEY (tool_type_id) REFERENCES tool_types(id),
    CONSTRAINT fk_tools_location FOREIGN KEY (location_id) REFERENCES locations(id)
);

CREATE INDEX idx_tools_status ON tools (status);
CREATE INDEX idx_tools_tool_type_id ON tools (tool_type_id);
CREATE INDEX idx_tools_location_id ON tools (location_id);