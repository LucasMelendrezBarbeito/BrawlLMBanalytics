# Roadmap — BrawlLMBanalytics

Seguimiento del plan de trabajo (seguridad, correcciones y features).
Estado a fecha 2026-07-11. Los commits referenciados están en la rama `master` salvo indicación contraria.

Leyenda: ✅ hecho · 🔸 parcial · ⬜ pendiente

---

## Fase 0 — Compilación

| # | Tarea | Estado | Commit |
|---|-------|--------|--------|
| 0.1 | Arreglar compilación rota tras quitar Lombok (`getIconUrl`, accessors de records) | ✅ | `6e9aa6b` |

## Fase 1 — Seguridad urgente

| # | Tarea | Estado | Commit |
|---|-------|--------|--------|
| 1.1 | Secrets fuera del repo → variables de entorno (`.env.example`) + rotar `jwt.secret` | ✅ | `6861629` |
| 1.2 | Eliminar `TestCurlController` (ejecutaba `Runtime.exec`/powershell + token embebido) | ✅ | `5006fe0` |
| 1.3 | Proteger `/admin/mapas/importar` (`permitAll` → `hasRole("ADMIN")`) | ✅ | `00c51bd` |
| 1.4 | Proteger `/tierlists/*/review` + derivar `usuarioId` del token | ✅ | `00c51bd` |
| 1.5 | Ocultar `password` y `email` en respuestas JSON (`@JsonIgnore`) | ✅ | `83bc7a2` |
| 1.6 | Sanitizar XSS en el render de reseñas (`escapeHtml` en `tierlist_ver.html`) | ✅ | `4284e09` |

## Fase 2 — Hardening de seguridad

| # | Tarea | Estado | Commit |
|---|-------|--------|--------|
| 2.1 | Resto de IDOR (tierlist crear, cuentas vincular/mias, estadísticas ownership) | ✅ | `b77d0d4` |
| 2.2 | Validación de DTOs (`@Valid` + Bean Validation) | ✅ | `928fc5f` |
| 2.3 | Mensajes de error genéricos en login (anti-enumeración) | ✅ | `c225ef1` |
| 2.4 | Manejo de excepciones JWT en el filtro (token expirado/inválido → 401) | ✅ | `a6a6c87` |
| 2.5 | Rate limiting en login/register (anti fuerza bruta) | ✅ | `pending` |
| 2.6 | Cabeceras de seguridad (CSP, HSTS, X-Frame-Options, nosniff, Referrer-Policy) | ✅ | `16f4c57` |
| 2.7 | Configurar CORS explícitamente (whitelist en vez de `@CrossOrigin` abierto) | ✅ | `pending` |
| 2.8 | `@ControllerAdvice` global (sin stack traces al cliente) | ✅ | `c225ef1` |
| 2.9 | Migrar JWT de `localStorage` a cookies httpOnly | ✅ | `pending` |
| 2.10 | Debug logs (`show-sql`, `logging...=DEBUG`) solo en perfil dev | ✅ | `12d93df` |

## Fase 3 — Correcciones técnicas (efectos de quitar Lombok)

| # | Tarea | Estado | Commit |
|---|-------|--------|--------|
| 3.1 | Reimplementar `getIconUrl()` en `Brawler` | ✅ | `6e9aa6b` |
| 3.2 | Auditar getters/setters faltantes en entidades | ✅ | `6e9aa6b` |
| 3.3 | Verificar serialización circular `Brawler.gadgets` / `starPowers` | 🔸 | sin ciclo observado; no auditado a fondo |

## Fase 4 — Rediseño Frontend

| # | Tarea | Estado | Commit |
|---|-------|--------|--------|
| 4.1 | CSS unificado `main.css` — glow aesthetic, JetBrains Mono, responsive | ✅ | `4bbc533` |
| 4.2 | Logo SVG profesional con gradientes y glow | ✅ | `4bbc533` |
| 4.3 | Todas las páginas HTML remodeladas (index, login, register, dashboard, cuenta, cuentas, estadisticas, mapas, tierlist, tierlist_crear, tierlist_ver) | ✅ | `4bbc533` |
| 4.4 | Sistema de estrellas → rating-pip numérico profesional | ✅ | `4bbc533` |
| 4.5 | Menú hamburguesa responsive | ✅ | `4bbc533` |
| 4.6 | Fix: error 500 al registrarse (validación duplicados) | ✅ | `d6c609c` |
| 4.7 | Fix: error 500 al vincular cuenta (Authorization header) | ✅ | `d6c609c` |
| 4.8 | Fix: "debes iniciar sesión" en cuentas (guardar token en localStorage) | ✅ | `d6c609c` |
| 4.9 | Fix: brawler icons en tierlist (calcular iconUrl en controller) | ✅ | `d6c609c` |
| 4.10 | Fix: cards de mapa (ancho en carousel, altura de imagen) | ✅ | `d6c609c` |
| 4.11 | Mapas: layout apilado + carrusel 4 cartas + flechas consistentes | ✅ | `6fd3302` |
| 4.12 | Avatar: URL correcta `cdn.brawlify.com/profile-icons/regular/` | ✅ | `bca4fd3` |
| 4.13 | Carrusel stats: flechas muestran 4 cards | ✅ | `6fd3302` |
| 4.14 | Botón Salir: `event.preventDefault()` en todas las páginas | ✅ | `6fd3302` |
| 4.15 | Logout en tierlist_crear/ver (añadir función `logout()`) | ✅ | `548c08a` |
| 4.16 | Mapas rotación: comprobar imagen antes de pintar | ✅ | `548c08a` |
| 4.17 | Spacing entre botones de tierlist cards | ✅ | `649ef3f` |

## Fase 5 — Features pendientes

| # | Tarea | Estado |
|---|-------|--------|
| 5.1 | Dockerizar MySQL (`docker-compose.yml`) | ⬜ |
| 5.2 | Sistema de roles (enum `USER`/`ADMIN`, endpoints admin, dashboard) | ⬜ |
| 5.3 | Estadísticas de Rankeds (liga, rangos, últimas partidas vía API Supercell) | ⬜ |

## Fase 6 — Desvinculación de Cuentas (feature/unlink-accounts)

| # | Tarea | Estado | Commit |
|---|-------|--------|--------|
| 6.1 | Spec (`specs/001-unlink-accounts/spec.md`) | ✅ | `ef97bf8` |
| 6.2 | Plan + research + data-model + contracts + quickstart | ✅ | `6e31c57` |
| 6.3 | Tasks (`specs/001-unlink-accounts/tasks.md`) | ✅ | `2e7c228` |
| 6.4 | Service: `eliminarCuenta()` con anti-IDOR | ✅ | `08b01a2` |
| 6.5 | Controller: `DELETE /cuentas/{id}` | ✅ | `08b01a2` |
| 6.6 | Frontend: función `eliminarCuenta()` + botón + feedback | ✅ | `08b01a2` |

## Fase 7 — Mejoras futuras

| # | Tarea | Estado |
|---|-------|--------|
| 7.1 | Refresh token + logout con blacklist | ⬜ |
| 7.2 | Perfiles Spring (`application-dev` creado en 2.10; falta `application-prod`) | 🔸 |
| 7.3 | Actualizar dependencias (Spring Boot 3.3.0→3.3.x, jjwt, httpclient5) | ⬜ |
| 7.4 | HTTPS/TLS (`server.ssl.*`) | ⬜ |
| 7.5 | `@OneToMany` EAGER → LAZY + paginación en `GET /tierlists` | ⬜ |
| 7.6 | `@Column(unique=true, nullable=false)` en `Usuario.username`/`email` | ⬜ |

---

## Notas

- **Tests**: suite de integración de seguridad con H2 en memoria
  (`SeguridadIntegrationTest`, perfil `test`), sin dependencia de MySQL ni
  variables de entorno. `mvnw test` → 8 tests. Commit `d3fd5f1`.

- **Excepciones tipadas (refactor recomendado)**: los `RuntimeException` de negocio
  ("... no encontrado", "cuenta ya vinculada") caen al 500 genérico del handler global.
  Introducir excepciones tipadas (`NoEncontradoException` → 404, etc.) daría status y
  mensajes correctos. Relacionado con 2.8.
- **Historial de git**: los secrets antiguos siguen en commits previos. El `jwt.secret`
  se rotó, así que el viejo ya no sirve para firmar; no se reescribió el historial.

- **Spec-Driven Development**: se integró GitHub Spec Kit (`specify init . --integration opencode`)
  para habilitar flujos de trabajo basados en especificaciones. Los comandos `/speckit.*` están
  disponibles en opencode para crear especificaciones, planes y tareas de forma estructurada.
  Se configuró auto-creación de rama `feature/<short-name>` en `/speckit.specify`.
