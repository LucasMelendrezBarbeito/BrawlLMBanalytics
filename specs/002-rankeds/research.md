# Research: Rankeds

**Date**: 2026-07-11

## Decision 1: Fuente de datos de clasificación

**Decision**: Usar la API de Supercell directamente (`/rankings/global/players` y `/rankings/{countryCode}/players`).

**Rationale**: La API proporciona el Top 200 global y por país en tiempo real. No requiere almacenamiento propio de clasificaciones.

**Alternatives considered**:
- Almacenar clasificaciones en BD propia — Rechazado: datos públicos de Supercell, innecesario duplicar.
- Usar BrawlAPI/Brawlify — Rechazado: la API oficial de Supercell es la fuente de verdad.

## Decision 2: Datos ranked de cuentas vinculadas

**Decision**: Obtener datos ranked del endpoint `/players/{tag}` (campo `ranked`) y battlelog de `/players/{tag}/battlelog` filtrando por `type: "ranked"`.

**Rationale**: La API proporciona el rango actual y el historial de partidas. Para "últimas 5 temporadas", almacenamos snapshots periódicos en la BD (entidad `RankedSeason`).

**Alternatives considered**:
- Solo mostrar battlelog — Rechazado: no cubre "últimas 5 temporadas".
- Polling cada 24h para snapshots — Adoptado como estrategia para datos históricos.

## Decision 3: Detección de país del usuario

**Decision**: Usar el campo `countryCode` del jugador vinculado (de la API de Supercell) como proxy del país del usuario.

**Rationale**: El jugador ya tiene un país asignado en Brawl Stars. No requiere geolocalización del navegador.

**Alternatives considered**:
- Geolocalización del navegador — Rechazado: requiere permisos, impreciso.
- Campo manual en perfil de usuario — Posible pero innecesario para v1.

## Decision 4: Almacenamiento de datos ranked

**Decision**: Crear entidades `RankedSeason` y `RankedMatch` en la BD. `RankedMatch` se sincroniza con el battlelog. `RankedSeason` se crea como snapshot cuando se detecta una nueva temporada.

**Rationale**: Permite mostrar historial de 5 temporadas sin depender de que la API lo proporcione.

**Alternatives considered**:
- No almacenar, solo mostrar battlelog — Rechazado: no cubre el requisito de 5 temporadas.
- Almacenar todo en una sola tabla JSON — Rechazado: no es query-friendly.

## Decision 5: Endpoints REST

**Decision**:
- `GET /rankeds/global` — Top 200 global (proxy a Supercell)
- `GET /rankeds/local/{countryCode}` — Top 200 por país
- `GET /rankeds/cuenta/{cuentaId}` — Detalle ranked de una cuenta vinculada (temporadas + partidas)

**Rationale**: Patrón REST consistente con el resto del proyecto. Los endpoints de clasificación son públicos. El de detalle requiere autenticación.

**Alternatives considered**:
- Un solo endpoint con parámetros — Rechazado: menos semántico.
- Endpoints separados para temporadas y partidas — Posible pero innecesario para v1.

## Decision 6: Tests

**Decision**: Tests unitarios para el service de rankeds con mocks de la API de Supercell.

**Rationale**: Constitution V — Calidad verificable. Tests que cubran errores de API y lógica de negocio.

**Alternatives considered**:
- Solo tests de integración — Rechazado: lentos, dependen de API externa.
- Sin tests — Rechazado: viola Principio V.
