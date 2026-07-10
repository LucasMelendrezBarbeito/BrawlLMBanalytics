# AGENTS.md — BrawlLMBanalytics

## Quick commands

```bash
# Build & run (use the Maven wrapper, not system mvn)
mvnw spring-boot:run

# Run the single context-load test (needs MySQL running)
mvnw test

# Run a specific test class
mvnw test -Dtest=BackendApplicationTests
```

## Environment prerequisites

- **Java 17** and a running **MySQL** instance on `127.0.0.1:3306`.
- Database must exist named `brawllmbanalytics`. Tables are **not** auto-created — `ddl-auto=none`. You need a pre-existing schema or manual DDL.
- The app needs a valid **Supercell API token**. It is hardcoded in two places (`application.properties` key `supercell.token` and `BrawlStarsService.java` field `API_TOKEN`). If calls fail, check both.

## Architecture

This is a **single-module Spring Boot 3.3 backend** that also serves the frontend as static files from `src/main/resources/static/`. There is no separate Node/React build.

- **Entry point**: `BackendApplication.java` (`@SpringBootApplication`)
- **Frontend**: plain HTML/CSS/JS in `src/main/resources/static/`. Served directly by Spring Boot at `http://localhost:8080/`
- **API routes** follow Spanish naming (`/auth`, `/brawl`, `/mapas`, `/tierlists`, `/cuentas`, `/estadisticas`, `/admin/mapas`)
- **DTOs**: Java `record` types under `dto/`. No Lombok (explicitly removed, per README).
- **Entities**: JPA entities with Spanish table names (`usuarios`, `mapas`, `brawlers`, `tierlists`, etc.)

## External APIs

Two external APIs are consumed:
- **Supercell official API** (`api.brawlstars.com/v1`): used via `java.net.http.HttpClient` in `BrawlStarsService`; Bearer-token auth.
- **BrawlAPI community API** (`api.brawlapi.com/v1`): used via `RestTemplate` (Apache HttpClient 5) in `MapasService.importarMapasDesdeBrawlAPI()`; unauthenticated.

## Security

- JWT-based, stateless (Spring Security session disabled, CSRF disabled, CORS disabled).
- Passwords hashed with BCrypt. Tokens signed with HS256 using `jwt.secret` from properties.
- `JwtAuthenticationFilter` skips auth for static resources (`/css/**`, `/js/**`, `/images/**`, `*.html`), login/register, and public GET endpoints.
- Public routes: GET brawl data, GET maps, GET tierlists. Protected: POST tierlist creation, account linking, user-specific GETs.

## Testing

- Only one test exists: `BackendApplicationTests` (`@SpringBootTest`). It loads the full Spring context, so **MySQL must be running** or it fails.
- No unit tests or mocked-service tests exist.

## Project quirks

- `TestCurlController.java` spawns `powershell` subprocesses — it is Windows-only diagnostic code.
- The `.github/java-upgrade/` directory is gitignored (contains `**/*` inside its own `.gitignore`).
- `Brawler`, `Gadget`, and `StarPower` entities use **manual** IDs (no `@GeneratedValue`). All other entities use `IDENTITY`.
- Two HTTP client stacks coexist: `java.net.http.HttpClient` (JEP 321) and Apache `HttpClient 5` via `RestTemplate`. Be consistent when adding new external calls — prefer the pattern already used by the neighboring service.
- The `BrawlController` maps to `/brawl` and `BrawlerController` also maps to `/brawl` (the Brawler endpoints resolve as sub-paths: `/brawl/brawlers`).
