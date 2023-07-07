--liquibase formatted sql
--changeset sql:create-users-table splitStatements:true endDelimiter:;

CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    `role` VARCHAR(255) NOT NULL,
    chat_id VARCHAR(255) NULL
);

--rollback DROP TABLE users;
