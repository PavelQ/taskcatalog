# Task Catalog Service

Бэкенд-сервис для управления каталогом задач. Разработан в качестве тестового задания с использованием реактивного стека
Spring WebFlux и прямого доступа к БД через `JdbcClient`.

## Особенности проекта

- **Реактивная обработка**: Использование Project Reactor (`Mono`, `Flux`) для обеспечения асинхронности и высокой
  производительности.
- **Чистый SQL**: Полный отказ от ORM/JPA в пользу нативных SQL-запросов через `JdbcClient` для максимизации контроля и
  производительности.
- **Слоистая архитектура**: Четкое разделение на Controller, Service и Repository слои.
- **Валидация и обработка ошибок**: Централизованная обработка исключений через `@RestControllerAdvice` и строгая
  валидация входящих DTO.
- **Тестирование**: Высокое покрытие Unit-тестами (Service, Controller) и наличие интеграционных тестов с использованием
  Testcontainers.

## Технологический стек

- **Язык**: Kotlin
- **Фреймворк**: Spring Boot 4.0.5
- **Веб-движок**: Spring WebFlux (Netty)
- **База данных**: PostgreSQL 17 (через JdbcClient)
- **Миграции**: Flyway
- **Тестирование**: JUnit 5, MockK, AssertJ, StepVerifier, Testcontainers

## Требования

- Docker и Docker Compose (для запуска БД)
- JDK 25

## Запуск приложения

1. Запустите базу данных в Docker:
   ```bash
   docker-compose up -d
   ```
2. Запустите приложение через Gradle:
   ```bash
   ./gradlew bootRun
   ```

## API Эндпоинты

### Задачи

- `POST /api/tasks` — Создать новую задачу.
- `GET /api/tasks?page=0&size=10&status=NEW` — Получить список задач с пагинацией и фильтрацией (параметры `page` и
  `size` обязательны).
- `GET /api/tasks/{id}` — Получить задачу по ID.
- `PATCH /api/tasks/{id}/status` — Обновить только статус задачи.
- `PATCH /api/tasks/{id}` — Обновить название и описание задачи (CRUD).
- `DELETE /api/tasks/{id}` — Удалить задачу.

## Тестирование

Запуск всех тестов:

```bash
./gradlew test
```

- **Unit тесты**: Проверка бизнес-логики сервисов и валидации контроллеров.
- **Интеграционные тесты**: Проверка корректности SQL-запросов и работы с реальной БД через Testcontainers.

## Структура проекта

```text
src/main/kotlin/ru/pavelq/taskcatalog/
├── controller   # REST контроллеры
├── service      # Бизнес-логика
├── repository   # Доступ к данным (JdbcClient)
├── model        # Доменные сущности
├── dto          # Data Transfer Objects
└── exception    # Ошибки и GlobalExceptionHandler
```
