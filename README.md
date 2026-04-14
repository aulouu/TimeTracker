# Task time tracker API

REST API для учета времени сотрудников по задачам.

## Реализованный функционал

- Создание задачи.
- Получение задачи по ID.
- Изменение статуса задачи (`NEW`, `IN_PROGRESS`, `DONE`).
- Создание записи о затраченном времени сотрудника на задачу.
- Получение информации о затраченном времени сотрудника на задачи за определённый
  период времени.
- Валидация входных DTO (Bean Validation).
- Централизованная обработка ошибок через `@RestControllerAdvice`.
- Документация API через Swagger/OpenAPI.
- Bearer authentication (JWT).

## Запуск приложения

### Требования

- Java 17+
- Maven Wrapper (в проекте уже есть `mvnw`)
- Docker

### Шаги

1. Поднять PostgreSQL:

```bash
docker compose up -d postgres
```

2. Собрать проект:

```bash
./mvnw clean package
```

3. Запустить приложение:

```bash
./mvnw spring-boot:run
```

Приложение стартует на `http://localhost:8080`.

По умолчанию используются параметры БД:
- `jdbc:postgresql://localhost:5432/timetracker`
- `postgres / postgres`

При необходимости можно переопределить через:
- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`

### Запуск приложения и БД полностью в Docker

```bash
docker compose up --build -d
```

Проверка:
- API: `http://localhost:8080`
- Swagger UI: `http://localhost:8080/swagger-ui.html`

Остановить и удалить контейнеры:

```bash
docker compose down
```

## Swagger / OpenAPI

- UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## Аутентификация

Сначала получите JWT:

`POST /api/auth/register`

Далее передавайте токен в заголовке:

`Authorization: Bearer <token>`

## Эндпойнты

- `POST /api/auth/register`
- `POST /api/auth/login`
- `POST /api/tasks`
- `GET /api/tasks/{id}`
- `PUT /api/tasks/{id}/status`
- `POST /api/time-records`
- `GET /api/time-records/period`

## Тестирование

Запуск всех тестов:

```bash
./mvnw test
```

Примечание по Testcontainers:
- Интеграционные тесты запускаются на PostgreSQL через Testcontainers.
- Для полного запуска тестов Docker должен быть доступен.

## Набор тестовых запросов

В проекте добавлена Postman-коллекция:

- `Task-time-tracker.postman_collection.json`

Импортируйте ее в Postman и выполняйте запросы по порядку:
1. Register
2. Create Task
3. Get Task By Id
4. Update Task Status
5. Create Time Record
6. Get Employee Time Report
