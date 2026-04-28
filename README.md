# Task Catalog Service

🇬🇧 English version below  
🇷🇺 Русская версия ниже  

---

## 🇬🇧 Overview
Backend service for managing tasks in a scalable system (similar to Jira or Trello).

The service is built using a reactive approach with Spring WebFlux and provides high-performance database access via direct SQL queries. It demonstrates production-ready backend patterns including layered architecture, validation, error handling, and comprehensive testing.

---

## 🚀 Features
- Reactive REST API (Spring WebFlux)
- Task management (CRUD operations)
- Pagination and filtering
- Partial updates (PATCH endpoints)
- Centralized error handling
- Input validation
- High test coverage (unit + integration)

---

## 🧱 Architecture
- Controller → Service → Repository layers
- Reactive processing (Mono / Flux)
- Direct database access via JdbcClient (no ORM)
- Database migrations with Flyway

---

## 🛠 Tech Stack
- Kotlin
- Spring Boot (WebFlux)
- PostgreSQL
- Docker

---

## ▶️ How to Run

### 1. Start database
```bash
docker-compose up -d
```

### 2. Run application
```bash
./gradlew bootRun
```

---

## 📡 API Examples

### Create task
```bash
curl -X POST http://localhost:8080/api/tasks \
-H "Content-Type: application/json" \
-d '{"title":"Test","description":"Test task"}'
```

### Get tasks
```bash
curl "http://localhost:8080/api/tasks?page=0&size=10&status=NEW"
```

---

## 🧪 Testing

Run all tests:
```bash
./gradlew test
```

- Unit tests: service logic and controller validation  
- Integration tests: database interaction using Testcontainers  

---

## 📁 Project Structure
```text
src/main/kotlin/ru/pavelq/taskcatalog/
├── controller
├── service
├── repository
├── model
├── dto
└── exception
```

---

## 🇷🇺 Описание
Бэкенд-сервис для управления задачами (аналог Jira / Trello).

Сервис реализован с использованием реактивного стека Spring WebFlux и прямого доступа к базе данных через SQL. Проект демонстрирует production-подходы: слоистую архитектуру, валидацию, обработку ошибок и полноценное тестирование.

---

## 🚀 Возможности
- Реактивный REST API (Spring WebFlux)
- CRUD-операции для задач
- Пагинация и фильтрация
- Частичные обновления (PATCH)
- Централизованная обработка ошибок
- Валидация входных данных
- Высокое покрытие тестами (unit + integration)

---

## 🧱 Архитектура
- Слои Controller → Service → Repository
- Реактивная обработка (Mono / Flux)
- Прямой доступ к БД через JdbcClient (без ORM)
- Миграции базы данных через Flyway

---

## 🛠 Технологии
- Kotlin
- Spring Boot (WebFlux)
- PostgreSQL
- Docker

---

## ▶️ Запуск

### 1. Запуск базы данных
```bash
docker-compose up -d
```

### 2. Запуск приложения
```bash
./gradlew bootRun
```

---

## 📡 Примеры API

### Создание задачи
```bash
curl -X POST http://localhost:8080/api/tasks \
-H "Content-Type: application/json" \
-d '{"title":"Test","description":"Test task"}'
```

### Получение списка задач
```bash
curl "http://localhost:8080/api/tasks?page=0&size=10&status=NEW"
```

---

## 🧪 Тестирование

Запуск всех тестов:
```bash
./gradlew test
```

- Unit-тесты: проверка бизнес-логики и контроллеров  
- Интеграционные тесты: работа с БД через Testcontainers  

---

## 📁 Структура проекта
```text
src/main/kotlin/ru/pavelq/taskcatalog/
├── controller
├── service
├── repository
├── model
├── dto
└── exception
```
