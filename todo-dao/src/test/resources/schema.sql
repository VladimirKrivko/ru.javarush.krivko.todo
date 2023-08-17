CREATE SCHEMA todo;

CREATE TABLE todo.task (
    id              INT GENERATED BY DEFAULT AS IDENTITY,
    description     VARCHAR(100) NOT NULL,
    status          SMALLINT NOT NULL,
    PRIMARY KEY (id)
);