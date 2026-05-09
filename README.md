# LMS Backend

A Spring Boot REST API for a Learning Management System. It exposes endpoints for managing users, courses, lessons, quizzes, enrollments, and student progress, with JWT-based authentication.

## Tech Stack

- **Java 21** (Temurin)
- **Spring Boot 4.0.5** — Web, Data JPA, Security, Validation, Actuator
- **PostgreSQL** as the primary datastore
- **JJWT 0.13** for JWT issuance/validation
- **Gradle** (wrapper included) for builds
- **Docker** for containerized deployment (Render-ready)

## Project Structure

```
src/main/java/org/com/lms_be
├── config/         # Spring configuration (security, JWT, etc.)
├── exception/      # Global exception handling
├── feature/        # Feature-sliced modules
│   ├── auth/
│   ├── user/
│   ├── category/
│   ├── course/
│   ├── lesson/
│   ├── lesson_progress/
│   ├── enrollment/
│   ├── quiz/
│   ├── quiz_attempt/
│   ├── question/
│   └── answer/
└── util/
src/main/resources
├── application.properties        # Shared config
├── application-dev.properties    # Local dev (default profile)
└── application-prod.properties   # Production
```

## Prerequisites

- JDK 21
- PostgreSQL 14+ running locally (or any reachable instance)
- Docker (optional, for container builds)

## Configuration

The active profile defaults to `dev` (see `application.properties`). Override any value via environment variables.

### Dev profile (`application-dev.properties`)

| Variable      | Default                                     | Description              |
| ------------- | ------------------------------------------- | ------------------------ |
| `DB_URL`      | `jdbc:postgresql://localhost:5432/lms_dev_db` | JDBC connection URL    |
| `DB_USERNAME` | `postgres`                                  | DB user                  |
| `DB_PASSWORD` | *(set in properties)*                       | DB password              |

### Shared (`application.properties`)

| Variable            | Default               | Description                    |
| ------------------- | --------------------- | ------------------------------ |
| `JWT_SECRET`        | *(dev fallback)*      | HMAC secret for signing tokens |
| `JWT_EXPIRATION_MS` | `86400000` (24h)      | Token lifetime in ms           |

### Prod profile (`application-prod.properties`)

Requires: `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USERNAME`, `DB_PASSWORD`, `JWT_SECRET`, and optionally `PORT` (defaults to `8080`).

> JPA is configured with `ddl-auto=update`, so tables are created/updated automatically on startup.

## Running Locally

### 1. Start PostgreSQL

Create a database that matches your dev config:

```bash
createdb lms_dev_db
```

Or with Docker:

```bash
docker run --name lms-postgres \
  -e POSTGRES_DB=lms_dev_db \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -p 5432:5432 -d postgres:16
```

### 2. Run the app

Using the Gradle wrapper:

```bash
./gradlew bootRun
```

Or build a jar and run it:

```bash
./gradlew bootJar
java -jar build/libs/lms-be-0.0.1-SNAPSHOT.jar
```

The app listens on `http://localhost:8080`.

### 3. Override config via env vars (optional)

```bash
DB_URL=jdbc:postgresql://localhost:5432/lms_dev_db \
DB_USERNAME=postgres \
DB_PASSWORD=secret \
JWT_SECRET=replace-me \
./gradlew bootRun
```

### 4. Switch profiles

```bash
SPRING_PROFILES_ACTIVE=prod ./gradlew bootRun
```

## Running with Docker

Build the image:

```bash
docker build -t lms-be .
```

Run it (pointing at a reachable Postgres):

```bash
docker run --rm -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e DB_HOST=host.docker.internal \
  -e DB_PORT=5432 \
  -e DB_NAME=lms_dev_db \
  -e DB_USERNAME=postgres \
  -e DB_PASSWORD=postgres \
  -e JWT_SECRET=replace-me \
  lms-be
```

## Tests

```bash
./gradlew test
```

## API Overview

Base URL: `http://localhost:8080`

| Resource                    | Path               |
| --------------------------- | ------------------ |
| Auth (login)                | `/auth/login`      |
| Users                       | `/users`           |
| Categories                  | `/category`        |
| Courses                     | `/course`          |
| Lessons (incl. quiz lessons)| `/lesson`          |
| Lesson progress             | `/lesson-progress` |
| Enrollments                 | `/enrollment`      |
| Questions (on quiz lessons) | `/question`        |
| Answers                     | `/answer`          |
| Quiz attempts               | `/quiz-attempt`    |
| Health                      | `/actuator/health` |

> **Quizzes**: a quiz is just a `LessonEntity` with `contentType=QUIZ`. There is no separate quiz resource and no passing score. Submitting a `quiz-attempt` records the attempt, returns `correctCount` / `totalQuestions` for immediate feedback, and marks the lesson as completed.

### Authentication

1. `POST /auth/login` with credentials returns a JWT.
2. Send the token on subsequent requests:
   ```
   Authorization: Bearer <token>
   ```
3. Endpoints expect the user identity from the auth header — do not pass `userId` in the request body.

## Deployment

`render.yaml` describes a Render Blueprint that provisions a Postgres database and a Docker web service, wiring DB credentials and a generated `JWT_SECRET` automatically. Push to a connected repo and use **New → Blueprint** on Render to deploy.
