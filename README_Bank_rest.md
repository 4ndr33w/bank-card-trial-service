## README

# Система управления банковскими картами

1) Билдим сервис:
```bash
mvn clean build
```

2) апускаем сервис в Docker:
```bash
docker-compose up --build
```

### Адрес API:
`url`: http://localhost:700 <br>
`Swagger`: http://localhost:700/swagger-ui/index.html`
<hr/>

### Swagger:
Поддерживает 2 типа авторизации: `Basic` и `Bearer` <br>
#### Basic:
При вводе логина и пароля, будет доступен `endpoint` для получения `JWT` токена. <br>
адрес ресурса: http://localhost:700/api/v1/login <br>
(через Postman выбираем `Basic Auth`)
#### Bearer:
Остальные ресурсы (кроме ресурса создания пользователя) требуют `JWT` токена.

#### Ресурс создания пользователя:
`url`: http://localhost:700/api/v1/users метод `POST` <br>
<hr/>

### База данных:
проксируется на http://localhost:5555 из контейнера `Docker`: `bank-db:5432` <br>

Имя базы данных: `back_cards_db`