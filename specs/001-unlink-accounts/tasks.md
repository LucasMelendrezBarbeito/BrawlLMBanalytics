# Tasks: Desvinculación de Cuentas

**Input**: Design documents from `specs/001-unlink-accounts/`

**Prerequisites**: plan.md, spec.md, research.md, data-model.md, contracts/delete-cuenta.md

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (US1, US2)

## Path Conventions

- **Backend**: `src/main/java/com/brawllmbanalytics/`
- **Frontend**: `src/main/resources/static/`

---

## Phase 1: Setup

No setup needed — proyecto ya existente con Spring Boot, MySQL, y frontend estático.

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Verificar que la infraestructura existente soporta la feature.

No se requieren tareas fundacionales adicionales. El endpoint de vinculación (`POST /cuentas/vincular`) ya existe y sigue el patrón correcto (JWT auth, inyección por constructor, repository). La feature de eliminación reutiliza la misma infraestructura.

---

## Phase 3: User Story 1 — Eliminar cuenta vinculada (Priority: P1) 🎯 MVP

**Goal**: Usuario autenticado puede eliminar una cuenta de Brawl Stars vinculada a su perfil.

**Independent Test**: Iniciar sesión → Ver cuentas vinculadas → Pulsar "Eliminar" → Confirmar → Cuenta desaparece de la lista.

### Implementation for User Story 1

- [ ] T001 [US1] Añadir método `eliminarCuenta(Long cuentaId, Integer usuarioId)` en `src/main/java/com/brawllmbanalytics/services/CuentaBrawlService.java` — busca la cuenta, valida pertenencia (anti-IDOR), elimina con `repository.deleteById()`
- [ ] T002 [US1] Añadir endpoint `DELETE /cuentas/{id}` en `src/main/java/com/brawllmbanalytics/controllers/CuentaBrawlController.java` — obtiene usuario del token, delega al service, retorna 204/403/404
- [ ] T003 [US1] Añadir función `eliminarCuenta(id)` en `src/main/resources/static/js/auth.js` — muestra `confirm()`, envía `DELETE` con `Authorization: Bearer <token>`, recarga lista con `cargarCuentasVinculadas()`
- [ ] T004 [US1] Añadir botón "Eliminar" en la tarjeta de cuenta dentro de `cargarCuentasVinculadas()` en `src/main/resources/static/js/auth.js` — botón con clase `btn btn-sm btn-danger`, llama `eliminarCuenta(c.id)`

**Checkpoint**: El usuario puede eliminar cuentas vinculadas desde la UI. La eliminación es verificada por el backend (anti-IDOR).

---

## Phase 4: User Story 2 — Feedback visual tras eliminación (Priority: P2)

**Goal**: El usuario recibe confirmación visual tras eliminar una cuenta y la lista se actualiza automáticamente.

**Independent Test**: Eliminar una cuenta → Ver mensaje de éxito → La cuenta desaparece sin recarga manual. Si falla → Ver mensaje de error.

### Implementation for User Story 2

- [ ] T005 [US2] Añadir feedback de éxito/error en `eliminarCuenta()` en `src/main/resources/static/js/auth.js` — si `res.ok`: mostrar alert de éxito y recargar lista; si no: mostrar mensaje de error con `res.status`
- [ ] T006 [US2] Manejar caso de cuenta ya eliminada (404) en `src/main/resources/static/js/auth.js` — si 404: mostrar "La cuenta ya no existe" y recargar lista

**Checkpoint**: El usuario recibe feedback claro tras cada acción. Los errores se manejan gracefully.

---

## Phase 5: Polish & Cross-Cutting Concerns

**Purpose**: Verificación final y validación.

- [ ] T007 Ejecutar validación de `quickstart.md` — probar los 5 escenarios documentados
- [ ] T008 Verificar que no hay violaciones a la constitution (anti-IDOR, DTOs, arquitectura en capas)

---

## Dependencies & Execution Order

### Phase Dependencies

- **Phase 1 (Setup)**: No aplica — proyecto existente
- **Phase 2 (Foundational)**: No aplica — infraestructura existente
- **Phase 3 (US1)**: Puede empezar inmediatamente
- **Phase 4 (US2)**: Depende de T003 y T004 (necesita la función de eliminación para añadir feedback)
- **Phase 5 (Polish)**: Depende de US1 y US2 completados

### Within Each User Story

- Service antes que controller (T001 → T002)
- JS function antes que botón (T003 → T004)
- Feedback después de función de eliminación (T005 → T006)

### Parallel Opportunities

- T001 (service) y T003 (JS function) pueden hacerse en paralelo (backend vs frontend)
- T005 y T006 pueden hacerse en paralelo (diferentes ramas del if/else)

---

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Completar T001-T004
2. **VALIDAR**: Probar eliminación de cuenta
3. Deploy si está listo

### Incremental Delivery

1. US1 (T001-T004) → Test → Deploy (MVP: eliminar funciona)
2. US2 (T005-T006) → Test → Deploy (mejora UX con feedback)
3. Polish (T007-T008) → Validación final

---

## Notes

- Feature simple: 8 tareas totales, 2 fases de implementación
- No se crean tests unitarios (no solicitados en spec)
- Anti-IDOR es el requisito de seguridad más crítico (T001, T002)
- Reutiliza infraestructura existente (JWT auth, repository, auth.js)
