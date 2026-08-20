CREATE TABLE loans (
    id                     BIGINT AUTO_INCREMENT PRIMARY KEY,
    reason                 VARCHAR(255)  NOT NULL,
    observation            TEXT          NULL,
    loan_date              DATETIME      NOT NULL,
    expected_return_date   DATETIME      NULL,
    return_date            DATETIME      NULL,
    tool_id                BIGINT        NOT NULL,
    borrowed_by_user_id    BIGINT        NOT NULL,
    returned_by_user_id    BIGINT        NULL,
    created_at             DATETIME      NOT NULL,
    updated_at             DATETIME      NOT NULL,
    CONSTRAINT fk_loans_tool FOREIGN KEY (tool_id) REFERENCES tools(id),
    CONSTRAINT fk_loans_borrowed_by FOREIGN KEY (borrowed_by_user_id) REFERENCES users(id),
    CONSTRAINT fk_loans_returned_by FOREIGN KEY (returned_by_user_id) REFERENCES users(id)
);

CREATE INDEX idx_loans_tool_id ON loans (tool_id);
CREATE INDEX idx_loans_borrowed_by_user_id ON loans (borrowed_by_user_id);
CREATE INDEX idx_loans_return_date ON loans (return_date);