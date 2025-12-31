## README

# Система управления банковскими картами
Для запуска сервиса необходимо выполнить выполнить следующие шаги:
### 1) Установить `OpenSSL`
например:
```bash
winget install ShiningLight.OpenSSL.Light
```

### 2) Сгенерировать ключи для подписи токенов:
Можно использовать уже имеющиеся, но для безопасности лучше сгенерировать новые. <br>
Необходимо зайти в каталог `src/main/resources/keys` и выполнить в нём команды:

Для Access Token:
```bash
openssl genrsa -out access_private.pem 2048
```
```bash
openssl rsa -in access_private.pem -pubout -out access_public.pem
```
Для Refresh Token:
```bash
openssl genrsa -out refresh_private.pem 2048
```
```bash
openssl rsa -in refresh_private.pem -pubout -out refresh_public.pem
```

### 3) Удалить префиксы и постфиксы из ключей:
Из каждого ключа удалить префиксы и постфиксы (-----BEGIN PRIVATE KEY----- / -----END PRIVATE KEY-----) <br>
Как для приватных, так и для публичных ключей

### 4) Установить Docker:
Если он не установлен, то можно установить через, например, `winget`:
```bash
winget install Docker.DockerDesktop
```

### 5) Запустить в докере базу данных Docker:
В каталоге проекта выполнить команду:
```bash
docker-compose up bank-db

# Или с флагом -d для запуска в фоне
docker-compose up -d bank-db
```
### 6) Выполнить компиляцию и сборку файлов проекта:
```bash
mvn clean build
```

### 7) Запустить сервис в Docker:
```bash
docker-compose up bank-rest-service
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
<hr/>

### Дефолтные пользователи:
По умолчанию в сервисе уже имеются 2 пользователя:
1) Пользователь с ролью `USER`: <br>
   login: `us3r` <br>
   password: `qwErty!23@`

2) Пользователь с ролями `ADMIN` и `USER`: <br>
   login: `4dm1n` <br>
   password: `qwErty!23@`