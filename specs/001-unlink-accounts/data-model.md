# Data Model: Desvinculación de Cuentas

**Date**: 2026-07-11

## Entity: CuentaBrawl

Ya existente en la base de datos. No se requieren cambios de esquema.

| Campo | Tipo | Descripción |
|-------|------|-------------|
| id | Integer (PK, AUTO) | Identificador único de la cuenta vinculada |
| tag | String | Tag de Brawl Stars (ej: #P98J02C) |
| nombre | String | Nombre del jugador en Brawl Stars |
| trofeos | Integer | Trofeos actuales |
| nivel | Integer | Nivel de experiencia |
| usuario_id | Integer (FK → usuarios.id) | Propietario de la cuenta |

### Relationships

- `CuentaBrawl` N → 1 `Usuario` (via `usuario_id`)
- Cascade: `ON DELETE CASCADE` implícito por JPA (si existe en el esquema)

### Validation Rules

- `usuario_id` DEBE coincidir con el usuario autenticado (anti-IDOR)
- `id` DEBE existir en la tabla `cuentas_brawl`

### State Transitions

- `EXIST` → `DELETED` (hard delete, no soft delete)

## Impact on Existing Entities

- **Usuario**: No se modifica. Las cuentas vinculadas se eliminan independientemente.
- **Tierlist/ResenaTierlist**: No se ven afectadas. Las tierlists y reseñas son entidades independientes que no referencian `CuentaBrawl`.
