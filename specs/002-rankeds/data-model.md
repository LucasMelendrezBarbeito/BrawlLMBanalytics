# Data Model: Rankeds

**Date**: 2026-07-11

## Entity: RankedSeason

Representa un snapshot del rango ranked de una cuenta en una temporada específica.

| Campo | Tipo | Descripción |
|-------|------|-------------|
| id | Integer (PK, AUTO) | Identificador único |
| cuenta_brawl_id | Integer (FK → cuentas_brawl.id) | Cuenta vinculada |
| season_number | Integer | Número de temporada (ej: 1, 2, 3...) |
| rank | String | Rango alcanzado (ej: "Masters", "Diamond") |
| trophies | Integer | Trofeos al final de la temporada |
| created_at | Timestamp | Fecha del snapshot |

### Relationships

- `RankedSeason` N → 1 `CuentaBrawl` (via `cuenta_brawl_id`)
- Unique constraint: (`cuenta_brawl_id`, `season_number`)

### Validation Rules

- `cuenta_brawl_id` DEBE pertenecer al usuario autenticado
- `season_number` DEBE ser positivo
- Máximo 5 temporadas por cuenta (las más recientes)

## Entity: RankedMatch

Representa una partida ranked reciente de una cuenta.

| Campo | Tipo | Descripción |
|-------|------|-------------|
| id | Integer (PK, AUTO) | Identificador único |
| cuenta_brawl_id | Integer (FK → cuentas_brawl.id) | Cuenta vinculada |
| battle_time | DateTime | Fecha/hora de la partida |
| mode | String | Modo de juego (ej: "gemGrab", "brawlBall") |
| rank | Integer | Posición final (para showdown) o null |
| trophy_change | Integer | Cambio de trofeos (+/-) |
| brawler_name | String | Brawler usado |
| brawler_id | Integer | ID del brawler usado |
| result | String | Resultado ("victory", "defeat", "rank_N") |

### Relationships

- `RankedMatch` N → 1 `CuentaBrawl` (via `cuenta_brawl_id`)

### Validation Rules

- `cuenta_brawl_id` DEBE pertenecer al usuario autenticado
- Máximo 10 partidas por cuenta (las más recientes)
- Se sincroniza con el battlelog de Supercell

## Entity: RankedPlayer (Transient)

Representa un jugador en la clasificación. NO persiste en BD.

| Campo | Tipo | Descripción |
|-------|------|-------------|
| tag | String | Tag del jugador |
| name | String | Nombre del jugador |
| iconId | Integer | ID del icono de perfil |
| trophies | Integer | Trofeos actuales |
| rank | Integer | Posición en la clasificación |
| clubName | String | Nombre del club (opcional) |

## Impact on Existing Entities

- **CuentaBrawl**: No se modifica. Las entidades ranked se vinculan via FK.
- **Usuario**: No se modifica.
