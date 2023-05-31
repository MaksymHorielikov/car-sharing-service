--liquibase formatted sql
--changeset sql:create-rentals_table splitStatements:true endDelimiter:;

CREATE TABLE IF NOT EXISTS rentals
(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    rental_date DATETIME NOT NULL,
    return_date DATETIME NOT NULL,
    actual_return_date DATETIME,
    car_id BIGINT,
    user_id BIGINT,
    deleted TINYINT(1) NOT NULL DEFAULT 0,
    FOREIGN KEY (car_id) REFERENCES cars(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

--rollback DROP TABLE rentals;