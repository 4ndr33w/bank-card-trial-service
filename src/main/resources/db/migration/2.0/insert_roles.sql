--liquibase formatted sql
--changeset Andr33w:005-insert-roles
--logicalFilePath:2.0/insert_roles.sql

INSERT INTO roles (role) VALUES
('ADMIN'),
('USER');