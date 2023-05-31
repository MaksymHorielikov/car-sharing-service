--liquibase formatted sql
--changeset sql:create-payments-table splitStatements:true endDelimiter:;

CREATE TABLE IF NOT EXISTS `payments`
(
    `id` BIGINT AUTO_INCREMENT NOT NULL PRIMARY KEY,
    `status` VARCHAR(255) NOT NULL,
    `type` VARCHAR(255) NOT NULL,
    `rental_id` BIGINT NOT NULL,
    `session_url` VARCHAR(255) NOT NULL,
    `session_id` VARCHAR(255) NOT NULL,
    `amount` DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (`rental_id`) REFERENCES rentals(id)
);

--rollback DROP TABLE payments;
