--liquibase formatted sql
--changeset sql:init-users-table splitStatements:true endDelimiter:;

INSERT INTO users (email, password, first_name, last_name, `role`)
VALUES ("admin@gmail.com", "$2a$10$smO/.dAjWAilC8Es2jxVd.TaDjIRFQxu2J6h8QqMZplhLX5ZRlbwK", "FN-Admin", "LN-Admin", "MANAGER");
INSERT INTO users (email, password, first_name, last_name, `role`)
VALUES ("user@gmail.com", "$2a$10$1ZJQhZuix6ZHJTAh1762vudjD184iKF5kTorrfSh9aRIYXX8a9BBq", "FN-User", "LN-User", "CUSTOMER");
