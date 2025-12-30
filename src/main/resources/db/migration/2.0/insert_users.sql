--liquibase formatted sql
--changeset Andr33w:006-insert-users
--logicalFilePath:2.0/insert_users.sql

INSERT INTO users VALUES
(
    'f70907de-196d-483f-8faa-b04e9d988b0a',
    'Юзер',
    0,
    'Юзерович',
    'user@mail.ru',
    'us3r',
    '$2a$10$lkz908HPtoMQQz8UolN1EecXqI8WOhkHKNDo0eHFi7WaJ2Tw3xNUq',
    '2000-10-25',
    'true',
    'false',
    '2025-12-30 19:25:06.701208+00',
    '2025-12-30 19:25:06.701208+00'
),
(
    '5868d762-5a69-46cc-a94c-a16bb934a842',
    'Администратор',
    0,
    'Администраторович',
    'admin@mail.ru',
    '4dm1n',
    '$2a$10$lkz908HPtoMQQz8UolN1EecXqI8WOhkHKNDo0eHFi7WaJ2Tw3xNUq',
    '2000-10-25',
    'true',
    'false',
    '2025-12-30 19:25:08.701208+00',
    '2025-12-30 19:25:08.701208+00'
);