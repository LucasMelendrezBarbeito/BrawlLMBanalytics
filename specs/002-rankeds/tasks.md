# Tasks: Rankeds

**Input**: Design documents from `specs/002-rankeds/`

**Prerequisites**: plan.md, spec.md, research.md, data-model.md, contracts/rankeds-endpoints.md

## Format: `[ID] [P?] [Story] Description`

- **[P]**: Can run in parallel (different files, no dependencies)
- **[Story]**: Which user story this task belongs to (US1, US2, US3, US4)

## Path Conventions

- **Backend**: `src/main/java/com/brawllmbanalytics/`
- **Frontend**: `src/main/resources/static/`

---

## Phase 1: Setup (Shared Infrastructure)

**Purpose**: Crear entidades y repositorios compartidos por todas las historias.

- [ ] T001 Crear entidad `RankedSeason` en `src/main/java/com/brawllmbanalytics/entities/RankedSeason.java` — campos: id, cuentaBrawl (FK), seasonNumber, rank, trophies, createdAt
- [ ] T002 Crear entidad `RankedMatch` en `src/main/java/com/brawllmbanalytics/entities/RankedMatch.java` — campos: id, cuentaBrawl (FK), battleTime, mode, rank, trophyChange, brawlerName, brawlerId, result
- [ ] T003 [P] Crear `RankedSeasonRepository` en `src/main/java/com/brawllmbanalytics/repositories/RankedSeasonRepository.java` — métodos: findByCuentaBrawlIdOrderBySeasonNumberDesc, findByCuentaBrawlIdAndSeasonNumber
- [ ] T004 [P] Crear `RankedMatchRepository` en `src/main/java/com/brawllmbanalytics/repositories/RankedMatchRepository.java` — métodos: findTop10ByCuentaBrawlIdOrderByBattleTimeDesc

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: DTOs y service base que todas las historias necesitan.

- [ ] T005 [P] Crear `RankedPlayerDTO` en `src/main/java/com/brawllmbanalytics/dto/RankedPlayerDTO.java` — record con: tag, name, iconId, trophies, rank, clubName
- [ ] T006 [P] Crear `RankedDetailDTO` en `src/main/java/com/brawllmbanalytics/dto/RankedDetailDTO.java` — record con: cuentaId, tag, nombre, ranked (currentRank, currentTrophies), temporadas (lista), ultimasPartidas (lista)
- [ ] T007 [P] Crear `RankedMatchDTO` en `src/main/java/com/brawllmbanalytics/dto/RankedMatchDTO.java` — record con: battleTime, mode, rank, trophyChange, brawlerName, brawlerId, result
- [ ] T008 Crear `RankedService` en `src/main/java/com/brawllmbanalytics/services/RankedService.java` — métodos: getGlobalRankings(), getLocalRankings(countryCode), getCuentaRankedDetail(cuentaId, usuarioId), syncCuentaRanked(cuentaId, usuarioId)

---

## Phase 3: User Story 1 — Ver clasificación global de rankeds (Priority: P1) 🎯 MVP

**Goal**: Usuario puede ver el Top 200 global de jugadores ranked.

**Independent Test**: Navegar a `/rankeds.html` → Ver Top 200 global con icono, puntos y nombre.

### Implementation for User Story 1

- [ ] T009 [US1] Implementar `getGlobalRankings()` en `RankedService` — llama a `GET /rankings/global/players?limit=200` de la API de Supercell, mapea respuesta a lista de `RankedPlayerDTO`
- [ ] T010 [US1] Crear endpoint `GET /rankeds/global` en `src/main/java/com/brawllmbanalytics/controllers/RankedController.java` — delega a `rankedService.getGlobalRankings()`, retorna lista de `RankedPlayerDTO`
- [ ] T011 [US1] Crear vista `rankeds.html` en `src/main/resources/static/rankeds.html` — estructura HTML con nav, botones "Top Global" / "Local", contenedor de clasificación
- [ ] T012 [US1] Implementar función `cargarRankings(tipo)` en `src/main/resources/static/js/auth.js` — fetch a `/rankeds/global` o `/rankeds/local/{code}`, renderiza lista con icono de liga, puntos y nombre

**Checkpoint**: El usuario puede ver el Top 200 global en la vista de rankeds.

---

## Phase 4: User Story 2 — Ver clasificación local (Priority: P2)

**Goal**: Usuario puede ver el Top 200 de su país alternando con "Local".

**Independent Test**: Pulsa "Local" → Se muestra Top 200 del país del usuario.

### Implementation for User Story 2

- [ ] T013 [US2] Implementar `getLocalRankings(countryCode)` en `RankedService` — llama a `GET /rankings/{countryCode}/players?limit=200`, mapea a `RankedPlayerDTO`
- [ ] T014 [US2] Crear endpoint `GET /rankeds/local/{countryCode}` en `RankedController.java` — delega a `rankedService.getLocalRankings(countryCode)`
- [ ] T015 [US2] Implementar lógica de detección de país en `cargarRankings()` en `auth.js` — usar `countryCode` del jugador vinculado o fallback a "global"
- [ ] T016 [US2] Implementar toggle "Top Global" / "Local" en `rankeds.html` — botones que llaman a `cargarRankings("global")` o `cargarRankings("local")`

**Checkpoint**: El usuario puede alternar entre clasificación global y local.

---

## Phase 5: User Story 3 — Ver cuentas vinculadas en rankeds (Priority: P3)

**Goal**: Usuario autenticado ve sus cuentas vinculadas en la página de rankeds.

**Independent Test**: Iniciar sesión → Ver sección "Tus cuentas" con cuentas vinculadas.

### Implementation for User Story 3

- [ ] T017 [US3] Implementar sección "Tus cuentas" en `rankeds.html` — contenedor debajo de la clasificación, visible solo si hay token
- [ ] T018 [US3] Implementar función `cargarCuentasRankeds()` en `auth.js` — fetch a `/cuentas/mias`, renderiza tarjetas de cuenta con botón "Ver detalle"
- [ ] T019 [US3] Implementar navegación a vista de detalle al seleccionar cuenta en `auth.js` — redirige a `rankeds_detalle.html?cuentaId={id}`

**Checkpoint**: El usuario ve sus cuentas vinculadas y puede seleccionar una.

---

## Phase 6: User Story 4 — Ver detalle ranked de una cuenta (Priority: P4)

**Goal**: Al seleccionar una cuenta, ver rango actual, historial de 5 temporadas y últimas 10 partidas ranked.

**Independent Test**: Seleccionar cuenta → Ver rango, temporadas y partidas.

### Implementation for User Story 4

- [ ] T020 [US4] Implementar `getCuentaRankedDetail(cuentaId, usuarioId)` en `RankedService` — busca cuenta, valida pertenencia (anti-IDOR), obtiene datos ranked de Supercell, consulta BD para temporadas y partidas
- [ ] T021 [US4] Implementar `syncCuentaRanked(cuentaId, usuarioId)` en `RankedService` — sincroniza datos ranked con la API de Supercell, guarda/actualiza `RankedSeason` y `RankedMatch` en BD
- [ ] T022 [US4] Crear endpoint `GET /rankeds/cuenta/{cuentaId}` en `RankedController.java` — delega a `rankedService.getCuentaRankedDetail()`, retorna `RankedDetailDTO`
- [ ] T023 [US4] Crear endpoint `POST /rankeds/cuenta/{cuentaId}/sync` en `RankedController.java` — delega a `rankedService.syncCuentaRanked()`
- [ ] T024 [US4] Crear vista `rankeds_detalle.html` en `src/main/resources/static/rankeds_detalle.html` — estructura HTML con resumen visual de rango, historial de temporadas y lista de partidas
- [ ] T025 [US4] Implementar función `cargarDetalleRanked(cuentaId)` en `auth.js` — fetch a `/rankeds/cuenta/{cuentaId}`, renderiza rango actual, temporadas y partidas
- [ ] T026 [US4] Implementar función `sincronizarRanked(cuentaId)` en `auth.js` — fetch POST a `/rankeds/cuenta/{cuentaId}/sync`, recarga detalle tras sincronización

**Checkpoint**: El usuario puede ver el detalle ranked completo de una cuenta.

---

## Phase 7: Tests

**Purpose**: Tests unitarios para validar la lógica de negocio.

- [ ] T027 [P] Test unitario para `RankedService.getGlobalRankings()` en `src/test/java/com/brawllmbanalytics/services/RankedServiceTest.java` — mock de API de Supercell, verificar mapeo a DTOs
- [ ] T028 [P] Test unitario para `RankedService.getCuentaRankedDetail()` en `RankedServiceTest.java` — mock de repositorios, verificar anti-IDOR (403 si no pertenece)
- [ ] T029 [P] Test unitario para `RankedService.syncCuentaRanked()` en `RankedServiceTest.java` — mock de API, verificar persistencia en BD

---

## Phase 8: Polish & Cross-Cutting Concerns

**Purpose**: Verificación final, roadmap y validación.

- [ ] T030 Añadir enlace "Rankeds" a la nav bar en todas las páginas HTML
- [ ] T031 Ejecutar validación de `quickstart.md` — probar los 7 escenarios documentados
- [ ] T032 Actualizar `docs/roadmap.md` con la feature de Rankeds y commits

---

## Dependencies & Execution Order

### Phase Dependencies

- **Phase 1 (Setup)**: No dependencies — empezar inmediatamente
- **Phase 2 (Foundational)**: Depende de Phase 1 (entidades creadas)
- **Phase 3 (US1)**: Depende de Phase 2 (DTOs y service base)
- **Phase 4 (US2)**: Depende de T009, T010, T011, T012 (necesita la vista base de US1)
- **Phase 5 (US3)**: Depende de T011 (necesita la vista rankeds.html)
- **Phase 6 (US4)**: Depende de Phase 1 (entidades) y Phase 2 (service)
- **Phase 7 (Tests)**: Depende de Phase 2 (service implementado)
- **Phase 8 (Polish)**: Depende de todas las fases anteriores

### Within Each User Story

- Service antes que controller (T009 → T010)
- HTML antes que JS (T011 → T012)
- Backend antes que frontend

### Parallel Opportunities

- T003 y T004 (repositorios) en paralelo
- T005, T006, T007 (DTOs) en paralelo
- T027, T028, T029 (tests) en paralelo
- US1 y US2 pueden desarrollarse en paralelo tras Phase 2

---

## Implementation Strategy

### MVP First (User Story 1 Only)

1. Completar Phase 1 (Setup) — T001-T004
2. Completar Phase 2 (Foundational) — T005-T008
3. Completar Phase 3 (US1) — T009-T012
4. **VALIDAR**: Ver Top 200 global en `/rankeds.html`
5. Deploy si está listo

### Incremental Delivery

1. Setup + Foundational → Base lista
2. US1 (T009-T012) → Test → Deploy (MVP: clasificación global)
3. US2 (T013-T016) → Test → Deploy (toggle Global/Local)
4. US3 (T017-T019) → Test → Deploy (cuentas vinculadas)
5. US4 (T020-T026) → Test → Deploy (detalle ranked)
6. Tests (T027-T029) → Validación
7. Polish (T030-T032) → Finalización

---

## Notes

- Feature compleja: 32 tareas totales, 8 fases
- Tests unitarios incluidos (solicitados en spec)
- Anti-IDOR crítico en T020 (detalle ranked)
- API de Supercell: endpoints de clasificación son públicos, battlelog requiere token
- Detección de país via `countryCode` del jugador vinculado
