CREATE TABLE tool_types_categories (
    tool_type_id  BIGINT NOT NULL,
    category_id   BIGINT NOT NULL,
    PRIMARY KEY (tool_type_id, category_id),
    CONSTRAINT fk_ttc_tool_type FOREIGN KEY (tool_type_id) REFERENCES tool_types(id),
    CONSTRAINT fk_ttc_category FOREIGN KEY (category_id) REFERENCES categories(id)
);