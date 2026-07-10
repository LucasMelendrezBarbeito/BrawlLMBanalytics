# Feature Specification: Rankeds

**Feature Branch**: `feature/rankeds`

**Created**: 2026-07-11

**Status**: Draft

**Input**: User description: "Rankeds: Clasificación global y local de rankeds, visualización de cuentas vinculadas con historial de temporadas y partidas ranked."

## User Scenarios & Testing *(mandatory)*

### User Story 1 — Ver clasificación global de rankeds (Priority: P1)

Como visitante (autenticado o no), quiero ver el Top 200 global de jugadores ranked para conocer a los mejores jugadores del mundo.

**Why this priority**: Es la vista principal de la feature. Sin ella, no existe la página de rankeds.

**Independent Test**: Navegar a la página de rankeds → Ver el Top 200 global con icono de liga, puntos y nombre.

**Acceptance Scenarios**:

1. **Given** un usuario en la página de rankeds, **When** carga la página, **Then** se muestra el Top 200 global con icono de liga, puntos de trofeos y nombre del jugador.
2. **Given** un usuario, **When** pulsa el botón "Top Global", **Then** se muestra la clasificación global.
3. **Given** la API de Supercell no responde, **When** el usuario intenta ver la clasificación, **Then** se muestra un mensaje de error claro.

---

### User Story 2 — Ver clasificación local (por país) (Priority: P2)

Como usuario, quiero ver el Top 200 de mi país para compararme con los mejores jugadores de mi región.

**Why this priority**: Complementa la vista global. El usuario quiere contexto local.

**Independent Test**: Pulsa "Local" → Se muestra el Top 200 del país del usuario.

**Acceptance Scenarios**:

1. **Given** un usuario en la página de rankeds, **When** pulsa "Local", **Then** se muestra el Top 200 de su país.
2. **Given** un usuario, **When** alterna entre "Top Global" y "Local", **Then** la vista cambia sin recargar la página completa.
3. **Given** un usuario desde España, **When** ve la clasificación local, **Then** se muestra el Top 200 de España (country code "es").

---

### User Story 3 — Ver cuentas vinculadas en rankeds (Priority: P3)

Como usuario autenticado, quiero ver mis cuentas vinculadas en la página de rankeds para seleccionar una y ver su historial.

**Why this priority**: Conecta la vista de rankeds con las cuentas del usuario.

**Independent Test**: Iniciar sesión → Ver sección "Tus cuentas" con las cuentas vinculadas.

**Acceptance Scenarios**:

1. **Given** un usuario autenticado con cuentas vinculadas, **When** está en la página de rankeds, **Then** ve una sección "Tus cuentas" con sus cuentas vinculadas.
2. **Given** un usuario sin cuentas vinculadas, **When** está en la página de rankeds, **Then** ve un mensaje indicando que no tiene cuentas vinculadas.
3. **Given** un usuario no autenticado, **When** está en la página de rankeds, **Then** no ve la sección "Tus cuentas".

---

### User Story 4 — Ver detalle ranked de una cuenta (Priority: P4)

Como usuario, al seleccionar una cuenta vinculada, quiero ver un resumen visual de su rendimiento ranked: rango actual, historial de las últimas 5 temporadas y las últimas 10 partidas ranked.

**Why this priority**: Es la vista de detalle que cierra el flujo de rankeds.

**Independent Test**: Seleccionar cuenta → Ver rango actual, historial de temporadas y últimas 10 partidas ranked.

**Acceptance Scenarios**:

1. **Given** un usuario selecciona una cuenta vinculada, **When** carga la vista de detalle, **Then** se muestra el rango ranked actual de la cuenta.
2. **Given** una cuenta con partidas ranked recientes, **When** carga la vista de detalle, **Then** se muestran las últimas 10 partidas ranked con resultado, trofeos y brawler usado.
3. **Given** una cuenta sin partidas ranked, **When** carga la vista de detalle, **Then** se muestra un mensaje indicando que no hay datos ranked disponibles.

---

### Edge Cases

- ¿Qué pasa si la API de Supercell no devuelve datos ranked para un jugador? → Mostrar mensaje "Sin datos ranked disponibles".
- ¿Qué pasa si el usuario tiene más de 5 cuentas vinculadas? → Mostrar todas, scroll si es necesario.
- ¿Qué pasa si la clasificación local no está disponible para un país? → Mostrar mensaje de error.

## Requirements *(mandatory)*

### Functional Requirements

- **FR-001**: El sistema DEBE mostrar el Top 200 global de jugadores ranked con icono de liga, puntos y nombre.
- **FR-002**: El sistema DEBE mostrar el Top 200 de un país específico (clasificación local).
- **FR-003**: El sistema DEBE detectar el país del usuario para mostrar la clasificación local por defecto.
- **FR-004**: La página DEBE tener dos botones ("Top Global" y "Local") para alternar entre clasificaciones.
- **FR-005**: El sistema DEBE mostrar las cuentas vinculadas del usuario autenticado en la página de rankeds.
- **FR-006**: Al seleccionar una cuenta vinculada, el sistema DEBE mostrar su rango ranked actual.
- **FR-007**: Al seleccionar una cuenta vinculada, el sistema DEBE mostrar las últimas 10 partidas ranked (resultado, trofeos, brawler).
- **FR-008**: Los datos ranked de cuentas vinculadas DEBEN almacenarse en el backend sin exponerlos en la vista de cuentas vinculadas.
- **FR-009**: La autorización DEBE derivarse del token JWT (anti-IDOR).
- **FR-010**: El sistema DEBE manejar errores de API con mensajes claros al usuario.

### Key Entities

- **RankedSeason**: Representa una temporada ranked de una cuenta. Atributos: id, cuentaBrawl (FK), seasonNumber, rank, trophies.
- **RankedMatch**: Representa una partida ranked reciente. Atributos: id, cuentaBrawl (FK), battleTime, mode, rank, trophyChange, brawlerUsed.
- **RankedPlayer**: Representa un jugador en la clasificación (no persiste en BD, viene de la API). Atributos: tag, name, iconId, trophies, rank, club.

## Success Criteria *(mandatory)*

### Measurable Outcomes

- **SC-001**: Los usuarios pueden ver el Top 200 global en menos de 3 segundos tras cargar la página.
- **SC-002**: Los usuarios pueden alternar entre Global y Local en menos de 1 segundo.
- **SC-003**: El 100% de las cuentas vinculadas con datos ranked muestran su historial correctamente.
- **SC-004**: Los datos ranked se almacenan en el backend sin exponerse en la vista de cuentas vinculadas.

## Assumptions

- La API de Supercell proporciona endpoints para clasificaciones globales y por país (`/rankings/global/players`, `/rankings/{countryCode}/players`).
- La API de Supercell proporciona el battlelog del jugador con partidas ranked (`/players/{tag}/battlelog`).
- El campo `ranked` del jugador puede ser null si no tiene datos ranked.
- El país del usuario se puede detectar mediante geolocalización del navegador o un campo en el perfil.
- Los datos de clasificación son públicos (no requieren autenticación).
- El historial de 5 temporadas se construye almacenando datos periódicamente (polling) o se muestra el rango actual + partidas recientes del battlelog.
