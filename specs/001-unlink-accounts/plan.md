# Implementation Plan: Desvinculación de Cuentas

**Branch**: `feature/unlink-accounts` | **Date**: 2026-07-11 | **Spec**: [spec.md](./spec.md)

**Input**: Feature specification from `specs/001-unlink-accounts/spec.md`

## Summary

Eliminar cuenta de Brawl Stars vinculada a un usuario. Backend: endpoint `DELETE /cuentas/{id}` con autorización anti-IDOR. Frontend: botón "Eliminar" en la vista de cuentas con confirmación nativa y recarga automática de la lista.

## Technical Context

**Language/Version**: Java 17

**Primary Dependencies**: Spring Boot 3.3, Spring Security, Spring Data JPA

**Storage**: MySQL 8, tabla `cuentas_brawl`

**Testing**: `@WebMvcTest` + `MockMvc` para controller, `@DataJpaTest` para repository

**Target Platform**: Linux server / Windows dev (Spring Boot embedded Tomcat)

**Project Type**: Web application (backend + frontend estático)

**Performance Goals**: Respuesta < 200ms para DELETE

**Constraints**: Anti-IDOR (Principio I), DTOs como contrato (Principio II), arquitectura en capas (Principio III)

**Scale/Scope**: Cambios en 1 controller, 1 service, 1 archivo JS

## Constitution Check

*GATE: Must pass before Phase 0 research. Re-check after Phase 1 design.*

| Principle | Status | Notes |
|-----------|--------|-------|
| I. Seguridad primero | ✅ PASS | Autorización del token JWT, no de parámetros. Anti-IDOR. |
| II. DTOs como contrato | ✅ PASS | DELETE retorna 204 sin body. No se exponen entidades. |
| III. Arquitectura en capas | ✅ PASS | Controller → Service → Repository. Inyección por constructor. |
| IV. Convenciones del proyecto | ✅ PASS | Nomenclatura en español, stack fijo (Spring Boot 3.3 / MySQL). |
| V. Calidad verificable | ✅ PASS | Tests unitarios planeados para controller y service. |

**No violations detected.**

## Project Structure

### Documentation (this feature)

```text
specs/001-unlink-accounts/
├── plan.md              # Este archivo
├── research.md          # Phase 0 output
├── data-model.md        # Phase 1 output
├── quickstart.md        # Phase 1 output
├── contracts/           # Phase 1 output
│   └── delete-cuenta.md
└── tasks.md             # Phase 2 output (/speckit.tasks)
```

### Source Code (repository root)

```text
src/main/java/com/brawllmbanalytics/
├── controllers/
│   └── CuentaBrawlController.java    # Añadir DELETE /cuentas/{id}
├── services/
│   └── CuentaBrawlService.java       # Añadir método eliminar()
└── repositories/
    └── CuentaBrawlRepository.java    # Ya existe, usar deleteById()

src/main/resources/static/
├── js/
│   └── auth.js                       # Añadir función eliminarCuenta()
└── css/
    └── main.css                      # Estilos para botón eliminar (si necesario)
```

## Complexity Tracking

> No violations — table empty.

| Violation | Why Needed | Simpler Alternative Rejected Because |
|-----------|------------|-------------------------------------|
| (none) | — | — |
