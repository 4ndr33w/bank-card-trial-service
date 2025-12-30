--liquibase formatted sql
--changeset Andr33w:005-insert-roles
--logicalFilePath:2.0/insert_roles.sql

INSERT INTO roles VALUES
('2e721a23-86c1-440d-a9c3-8dd70da3ffdc','ADMIN'),
('c574c212-a786-449f-aa03-6176f36ce2d3', 'USER');