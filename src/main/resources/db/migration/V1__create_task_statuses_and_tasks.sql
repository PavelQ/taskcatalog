CREATE TABLE task_statuses
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

INSERT INTO task_statuses (name)
VALUES ('NEW'),
       ('IN_PROGRESS'),
       ('DONE'),
       ('CANCELLED');

CREATE TABLE tasks
(
    id          BIGSERIAL PRIMARY KEY,
    title       VARCHAR(100) NOT NULL,
    description TEXT,
    status_id   INT          NOT NULL DEFAULT 1 REFERENCES task_statuses (id),
    created_at  TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMP    NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_tasks_status_id ON tasks (status_id);
CREATE INDEX idx_tasks_created_at ON tasks (created_at DESC);

