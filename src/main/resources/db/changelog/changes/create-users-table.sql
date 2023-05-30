CREATE TABLE IF NOT EXISTS `users`
(
    `id`         BIGINT NOT NULL,
    `email`      VARCHAR(256) NULL,
    `first_name` VARCHAR(100) NULL,
    `last_name`  VARCHAR(100) NULL,
    `password`   VARCHAR(100) NULL,
    `role`       VARCHAR(45) NULL,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE
);
