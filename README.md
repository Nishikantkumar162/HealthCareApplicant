## Healthcare Appointment System

End-to-end appointment platform covering patients, doctors and admins with JWT auth, role-based APIs, React frontend, Spring Boot backend, MySQL storage, Docker orchestration and CI/CD via GitHub Actions.

### Project layout

- `backend/` Spring Boot service (JWT security, Swagger, email notifications, reminders)
- `frontend/` React + Vite SPA (role dashboards, bookings, status flows)
- `db/` SQL schema & seed helpers
- `infra/github/` GitHub Actions workflow
- `docker-compose.yml` Local multi-service stack
- `docs/` Additional setup tips (see `docs/setup.md`)

### Backend

1. `cd backend`
2. `mvn spring-boot:run`

Environment (see `backend/src/main/resources/application.yml`):

```
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3307/healthcare
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=root
APP_JWT_SECRET=TXlTdXBlclNlY3JldEtleUhlYWx0aENhcmU=
```

Swagger UI: `http://localhost:8080/swagger-ui.html`

### Frontend

1. `cd frontend`
2. `cp env.example .env` (adjust API base URL if running remotely)
3. `npm install`
4. `npm run dev`

### Dockerized local stack

```
docker compose up --build
```

Services:

- MySQL on `localhost:3307`
- Backend on `localhost:8080`
- Frontend on `http://localhost:5173`

### GitHub Actions CI/CD

Workflow `ci-cd.yml` executes on pushes/PRs to `main`:

- Build & test backend (Maven)
- Build frontend (Vite)
- When branch is `main`, build + push Docker images to Docker Hub (`DOCKERHUB_USERNAME`, `DOCKERHUB_TOKEN` secrets)

### Default accounts

Loaded via `DataSeeder`:

- Admin: `admin@healthcare.com` / `Admin@123`
- Doctor: `doctor@healthcare.com` / `Doctor@123`
- Patient: `patient@healthcare.com` / `Patient@123`

### Testing the flow

1. Login as patient -> book appointment with approved doctor
2. Login as doctor -> confirm/reject appointment, manage availability
3. Login as admin -> approve doctors, monitor dashboard
4. Observe email logs in backend console for booking/reminder notifications

### MySQL schema

Base schema is captured in `db/schema.sql` and matches the JPA entities (users, roles, patients, doctors, availability, appointments). Use `db/data.sql` if you want to bootstrap manually outside of Spring Boot.

