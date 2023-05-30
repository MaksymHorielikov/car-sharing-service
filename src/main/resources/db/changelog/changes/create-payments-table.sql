CREATE TABLE IF NOT EXISTS `payments`
(
    `id` BIGINT NOT NULL,
    `status` VARCHAR(45) NOT NULL,
    `type` VARCHAR(45) NOT NULL,
    `rental_id` BIGINT NOT NULL,
    `session_url` VARCHAR(256) NOT NULL,
    `session_id` VARCHAR(256) NOT NULL,
    `amount` DECIMAL NOT NULL,
    PRIMARY KEY (`id`)
);
