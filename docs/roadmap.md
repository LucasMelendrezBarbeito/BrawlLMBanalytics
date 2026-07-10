# Roadmap — BrawlLMBanalytics

Seguimiento del plan de trabajo (seguridad, correcciones y features).
Estado a fecha 2026-07-10. Los commits referenciados están en la rama `feature-claude`.

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
| 2.5 | Rate limiting en login/register (anti fuerza bruta) | ⬜ | — |
| 2.6 | Cabeceras de seguridad (CSP, HSTS, X-Frame-Options, nosniff, Referrer-Policy) | ✅ | `16f4c57` |
| 2.7 | Configurar CORS explícitamente (whitelist en vez de `@CrossOrigin` abierto) | ⬜ | — |
| 2.8 | `@ControllerAdvice` global (sin stack traces al cliente) | ✅ | `c225ef1` |
| 2.9 | Migrar JWT de `localStorage` a cookies httpOnly | ⬜ | — |
| 2.10 | Debug logs (`show-sql`, `logging...=DEBUG`) solo en perfil dev | ⬜ | — |

## Fase 3 — Correcciones técnicas (efectos de quitar Lombok)

| # | Tarea | Estado | Commit |
|---|-------|--------|--------|
| 3.1 | Reimplementar `getIconUrl()` en `Brawler` | ✅ | `6e9aa6b` |
| 3.2 | Auditar getters/setters faltantes en entidades | ✅ | `6e9aa6b` |
| 3.3 | Verificar serialización circular `Brawler.gadgets` / `starPowers` | 🔸 | sin ciclo observado; no auditado a fondo |

## Fase 4 — Features pendientes

| # | Tarea | Estado |
|---|-------|--------|
| 4.1 | Dockerizar MySQL (`docker-compose.yml`) | ⬜ |
| 4.2 | Sistema de roles (enum `USER`/`ADMIN`, endpoints admin, dashboard) | ⬜ |
| 4.3 | Estadísticas de Rankeds (liga, rangos, últimas partidas vía API Supercell) | ⬜ |

## Fase 5 — Mejoras futuras

| # | Tarea | Estado |
|---|-------|--------|
| 5.1 | Refresh token + logout con blacklist | ⬜ |
| 5.2 | Perfiles Spring (`application-dev` / `application-prod`) | ⬜ |
| 5.3 | Actualizar dependencias (Spring Boot 3.3.0→3.3.x, jjwt, httpclient5) | ⬜ |
| 5.4 | HTTPS/TLS (`server.ssl.*`) | ⬜ |
| 5.5 | Check de username/email duplicado en el registro | ⬜ |
| 5.6 | `@OneToMany` EAGER → LAZY + paginación en `GET /tierlists` | ⬜ |
| 5.7 | `@Column(unique=true, nullable=false)` en `Usuario.username`/`email` | ⬜ |

---

## Notas

- **Excepciones tipadas (refactor recomendado)**: los `RuntimeException` de negocio
  ("... no encontrado", "cuenta ya vinculada") caen al 500 genérico del handler global.
  Introducir excepciones tipadas (`NoEncontradoException` → 404, etc.) daría status y
  mensajes correctos. Relacionado con 2.8.
- **Historial de git**: los secrets antiguos siguen en commits previos. El `jwt.secret`
  se rotó, así que el viejo ya no sirve para firmar; no se reescribió el historial.
