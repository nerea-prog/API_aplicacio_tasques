DROP TABLE IF EXISTS tasks;
CREATE TABLE tasks (
    id SERIAL,
    name VARCHAR(255),
    category VARCHAR(255),
    dataCreated TIMESTAMP,
    dataUpdated TIMESTAMP,
);
