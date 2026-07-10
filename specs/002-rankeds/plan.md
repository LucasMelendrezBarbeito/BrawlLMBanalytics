# Implementation Plan: Rankeds

**Branch**: `feature/rankeds` | **Date**: 2026-07-11 | **Spec**: [spec.md](./spec.md)

**Input**: Feature specification from `specs/002-rankeds/spec.md`

## Summary

Nueva sección de Rankeds con clasificación global/local Top 200, visualización de cuentas vinculadas y detalle ranked (temporadas + partidas). Backend: 4 endpoints REST que proxyan la API de Supercell y almacenan datos ranked. Frontend: vista de clasificación con toggle Global/Local + vista de detalle por cuenta.

## Technical Context

**Language/Version**: Java 17

**Primary Dependencies**: Spring Boot 3.3, Spring Security, Spring Data JPA, java.net.http.HttpClient

**Storage**: MySQL 8, nuevas tablas `ranked_seasons` y `ranked_matches`

**Testing**: `@WebMvcTest` + `MockMvc` para controllers, tests unitarios para service con mocks

**Target Platform**: Linux server / Windows dev (Spring Boot embedded Tomcat)

**Project Type**: Web application (backend + frontend estático)

**Performance Goals**: Respuesta < 2s para clasificaciones (depende de API Supercell), < 500ms para detalle ranked (BD local)

**Constraints**: Anti-IDOR (Principio I), DTOs como contrato (Principio II), arquitectura en capas (Principio III), usar HttpClient para Supercell (Principio IV)

**Scale/Scope**: ~4 endpoints nuevos, 2 entidades JPA, 1 vista HTML + JS

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

| Principle | Status | Notes |
|-----------|--------|-------|
| I. Seguridad primero | ✅ PASS | Autorización del token JWT para detalle ranked. Clasificaciones son públicas. Anti-IDOR en endpoints protegidos. |
| II. DTOs como contrato | ✅ PASS | Endpoints retornan DTOs (records), no entidades JPA. |
| III. Arquitectura en capas | ✅ PASS | Controller → Service → Repository. Inyección por constructor. |
| IV. Convenciones del proyecto | ✅ PASS | Nomenclatura en español, stack fijo, HttpClient para Supercell (consistente con BrawlStarsService). |
| V. Calidad verificable | ✅ PASS | Tests unitarios planeados para service. |

**No violations detected.**

## Project Structure

### Documentation (this feature)

```text
specs/002-rankeds/
├── plan.md              # Este archivo
├── research.md          # Phase 0 output
├── data-model.md        # Phase 1 output
├── quickstart.md        # Phase 1 output
├── contracts/           # Phase 1 output
│   └── rankeds-endpoints.md
└── tasks.md             # Phase 2 output (/speckit.tasks)
```

### Source Code (repository root)

```text
src/main/java/com/brawllmbanalytics/
├── entities/
│   ├── RankedSeason.java           # Nueva entidad
│   └── RankedMatch.java            # Nueva entidad
├── repositories/
│   ├── RankedSeasonRepository.java # Nuevo repository
│   └── RankedMatchRepository.java  # Nuevo repository
├── dto/
│   ├── RankedPlayerDTO.java        # DTO para clasificación
│   ├── RankedDetailDTO.java        # DTO para detalle ranked
│   └── RankedMatchDTO.java         # DTO para partida ranked
├── services/
│   └── RankedService.java          # Nuevo service
├── controllers/
│   └── RankedController.java       # Nuevo controller
└── (existente) BrawlStarsService.java  # Se reutiliza para llamadas a Supercell

src/main/resources/static/
├── rankeds.html                    # Nueva vista
└── js/
    └── auth.js                     # Se añaden funciones ranked
```

## Complexity Tracking

> No violations — table empty.

| Violation | Why Needed | Simpler Alternative Rejected Because |
|-----------|------------|-------------------------------------|
| (none) | — | — |
