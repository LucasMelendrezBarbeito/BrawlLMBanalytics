# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project

BrawlLMBanalytics is a data-analysis web platform for the mobile game **Brawl Stars**. It lets users link their game accounts to view personal/brawler stats, browse the map catalog with per-map winrate rankings, track the live event rotation, and build interactive drag-and-drop tierlists with a star-rating/comments review system. It integrates the official Supercell Brawl Stars API and the community Brawlify (BrawlAPI) API.

## Workflow

- **Commit after each verified change.** Once something is implemented AND tested to work (it compiles, the app boots, and the affected behavior is exercised), commit it — no need to ask each time; this is standing authorization for this repo. Keep commits small and scoped to a single verified unit.
- **Never commit secrets** (tokens, `jwt.secret`, DB credentials) — see the constitution, Principle I.
- This mirrors the constitution's "Flujo de Desarrollo" section (`.specify/memory/constitution.md`).

## Commands

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
- The app needs a valid **Supercell API token**, generated and IP-registered at the [Supercell Developer Portal](https://developer.brawlstars.com/). It is provided via the **`SUPERCELL_TOKEN`** environment variable (single source: `application.properties` reads `${SUPERCELL_TOKEN}`, and `BrawlStarsService` injects `${supercell.token}`). If calls fail, check the env var and that it matches your current public IP.
- **Secrets are read from environment variables**, never hardcoded (see `.env.example` for the full list): `JWT_SECRET`, `SUPERCELL_TOKEN`, and optionally `DB_USERNAME`/`DB_PASSWORD` (default `root`/empty). The app will not start if `JWT_SECRET` or `SUPERCELL_TOKEN` is missing.

## Architecture

This is a **single-module Spring Boot 3.3 backend** that also serves the frontend as static files from `src/main/resources/static/`. There is no separate Node/React build — frontend is plain HTML5/CSS3/ES6 using the Fetch API and the native Drag & Drop API for the tierlist builder.

- **Entry point**: `BackendApplication.java` (`@SpringBootApplication`)
- **Frontend**: served directly by Spring Boot at `http://localhost:8080/`
- **Package layout** (`src/main/java/com/brawllmbanalytics/`): `controllers/`, `services/`, `repositories/`, `security/`, `dto/`, `entities/`, `config/`
- **API routes** follow Spanish naming (`/auth`, `/brawl`, `/mapas`, `/tierlists`, `/cuentas`, `/estadisticas`, `/admin/mapas`)
- **DTOs**: Java `record` types under `dto/`. No Lombok (explicitly removed — do not reintroduce it).
- **Entities**: JPA entities with Spanish table names (`usuarios`, `mapas`, `brawlers`, `tierlists`, etc.)

## External APIs

Two external APIs are consumed, via two different HTTP client stacks — when adding a new external call, follow the pattern already used by the neighboring service rather than mixing stacks arbitrarily:
- **Supercell official API** (`api.brawlstars.com/v1`): consumed via `java.net.http.HttpClient` (JEP 321) in `BrawlStarsService`; Bearer-token auth.
- **BrawlAPI community API** (`api.brawlapi.com/v1`): consumed via `RestTemplate` (backed by Apache HttpClient 5) in `MapasService.importarMapasDesdeBrawlAPI()`; unauthenticated.

## Security

- JWT-based, stateless (Spring Security session disabled, CSRF disabled, CORS disabled).
- Passwords hashed with BCrypt. Tokens signed with HS256 using `jwt.secret` from properties.
- `JwtAuthenticationFilter` skips auth for static resources (`/css/**`, `/js/**`, `/images/**`, `*.html`), login/register, and public GET endpoints.
- Public routes: GET brawl data, GET maps, GET tierlists. Protected: POST tierlist creation, account linking, user-specific GETs.

## Testing

- Tests run against **H2 in-memory** via the `test` profile (`src/test/resources/application-test.properties`) — **no MySQL or env vars needed**. Run with `mvnw test`.
- `BackendApplicationTests` (`@SpringBootTest`, context load) and `SeguridadIntegrationTest` (`@AutoConfigureMockMvc`, exercises the security behaviors of Fases 1–2: public vs protected endpoints, invalid token → 401, validation → 400, generic login errors, no password/email leak).

## Project quirks

- `TestCurlController.java` spawns `powershell` subprocesses — it is Windows-only diagnostic code.
- The `.github/java-upgrade/` directory is gitignored (contains `**/*` inside its own `.gitignore`).
- `Brawler`, `Gadget`, and `StarPower` entities use **manual** IDs (no `@GeneratedValue`). All other entities use `IDENTITY`.
- The `BrawlController` maps to `/brawl` and `BrawlerController` also maps to `/brawl` (the Brawler endpoints resolve as sub-paths: `/brawl/brawlers`).
- Database provisioning is currently manual; a Docker-based setup is planned but not yet implemented (see README's "Futuras Mejoras").
