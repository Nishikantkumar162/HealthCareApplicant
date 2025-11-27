## Local setup checklist

1. Install Java 17, Maven, Node 20+, Docker Desktop.
2. Start MySQL via Docker compose (recommended) or configure manually and update `SPRING_DATASOURCE_URL`.
3. Backend:
   - `cd backend`
   - `mvn clean spring-boot:run`
4. Frontend:
   - `cd frontend`
   - `cp env.example .env`
   - `npm install && npm run dev`
5. Visit `http://localhost:5173` and sign in with seeded credentials.

### Running tests

```
mvn test        # backend
npm run build   # frontend (ensures lint-free production build)
```

### Docker Hub images

CI pushes:

- `healthcare-backend`
- `healthcare-frontend`

Tag = short commit SHA. Update `docker-compose.yml` to use remote images when deploying elsewhere.

