--liquibase formatted sql
--changeset Andr33w:007-insert-user-roles
--logicalFilePath:2.0/insert_user_roles.sql

INSERT INTO user_roles VALUES
('5868d762-5a69-46cc-a94c-a16bb934a842', '2e721a23-86c1-440d-a9c3-8dd70da3ffdc'),
('f70907de-196d-483f-8faa-b04e9d988b0a', 'c574c212-a786-449f-aa03-6176f36ce2d3'),
('5868d762-5a69-46cc-a94c-a16bb934a842', 'c574c212-a786-449f-aa03-6176f36ce2d3');