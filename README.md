# Family League

A cricket prediction league app built with Spring Boot and PostgreSQL.

---

## Prerequisites

- Java 21+
- Maven 3.8+
- PostgreSQL 14+

---

## Setup

### 1. Clone the repo

```bash
git clone <your-repo-url>
cd java-tech-stack
```

### 2. Create the database

```bash
psql -U postgres -c "CREATE DATABASE family_league;"
```

### 3. Configure environment variables

Copy the example file and fill in your values:

```bash
cp .env.example .env
```

Edit `.env`:

```env
DB_URL=jdbc:postgresql://localhost:5432/family_league
DB_USERNAME=postgres
DB_PASSWORD=your_postgres_password

JWT_SECRET=any_random_string_minimum_32_characters_long

MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=your_email@gmail.com
MAIL_PASSWORD=your_app_password
MAIL_FROM=your_email@gmail.com
```

> Mail fields are required by Spring but emails are only sent when the scheduler triggers — the app runs fine without a working SMTP for basic testing.

### 4. Run the app

```bash
export $(grep -v '^#' .env | xargs)
mvn spring-boot:run
```

App starts on `http://localhost:8080`

---

## Database Migrations

Flyway runs automatically on startup:

| Version | Description        |
|---------|--------------------|
| V1      | Initial schema     |
| V2      | Seed data          |

V2 seeds the following test data (all passwords: `Test@123`):

| Username   | Role  |
|------------|-------|
| testadmin  | ADMIN |
| alice      | USER  |
| bob        | USER  |
| charlie    | USER  |
| diana      | USER  |
| eve        | USER  |

> The app also auto-creates an `admin` user on first startup (password: `Admin@123`).

---

## API

Swagger UI: `http://localhost:8080/swagger-ui.html`

### Authentication

All endpoints (except login/register) require a JWT token:

```
POST /api/auth/login
{
  "username": "alice",
  "password": "Test@123"
}
```

Use the returned token in the `Authorization` header:

```
Authorization: Bearer <token>
```

### Key Endpoints

| Method | URL | Description |
|--------|-----|-------------|
| POST | `/api/auth/login` | Login |
| POST | `/api/auth/register` | Register new user |
| GET | `/api/teams` | List all teams |
| GET | `/api/teams/{teamId}/players` | List players in a team |
| GET | `/api/leagues` | List leagues |
| GET | `/api/leagues/{leagueId}/seasons` | List seasons |
| GET | `/api/seasons/{seasonId}/matches` | List matches in a season |
| GET | `/api/matches/{matchId}` | Match details |
| GET | `/api/matches/{matchId}/result` | Match result |
| POST | `/api/matches/{matchId}/predictions` | Submit match prediction |
| GET | `/api/matches/{matchId}/predictions/me` | My match prediction |
| GET | `/api/matches/{matchId}/predictions` | All predictions (after lock) |
| POST | `/api/seasons/{seasonId}/predictions/league` | Submit league prediction |
| GET | `/api/seasons/{seasonId}/predictions/league/me` | My league prediction |
| GET | `/api/seasons/{seasonId}/leaderboard` | Season leaderboard |
