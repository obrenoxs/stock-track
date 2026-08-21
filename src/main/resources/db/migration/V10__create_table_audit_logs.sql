CREATE TABLE audit_logs (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    action_type VARCHAR(30)  NOT NULL,
    reason      VARCHAR(255) NULL,
    tool_id     BIGINT       NOT NULL,
    user_id     BIGINT       NOT NULL,
    created_at  DATETIME     NOT NULL,
    CONSTRAINT fk_audit_logs_tool FOREIGN KEY (tool_id) REFERENCES tools(id),
    CONSTRAINT fk_audit_logs_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE INDEX idx_audit_logs_tool_id ON audit_logs (tool_id);
CREATE INDEX idx_audit_logs_user_id ON audit_logs (user_id);