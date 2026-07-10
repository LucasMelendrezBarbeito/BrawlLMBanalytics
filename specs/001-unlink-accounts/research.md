# Research: Desvinculación de Cuentas

**Date**: 2026-07-11

## Decision 1: Endpoint REST para eliminación

**Decision**: `DELETE /cuentas/{id}`

**Rationale**: Patrón REST estándar para eliminar recursos. El ID de la cuenta se pasa en la URL. La identidad del usuario se deriva del token JWT (anti-IDOR, Principio I).

**Alternatives considered**:
- `DELETE /cuentas/vincular?tag=XXX` — Rechazado: expone el tag como identificador, menos RESTful.
- `POST /cuentas/eliminar` — Rechazado: no es semántico REST.

## Decision 2: Autorización anti-IDOR

**Decision**: El controller obtiene el usuario autenticado del SecurityContext (establecido por JwtAuthenticationFilter). Compara `cuenta.usuarioId` con el ID del usuario autenticado. Si no coinciden, retorna 403.

**Rationale**: Principio I de la constitution — "La autorización se deriva SIEMPRE del token autenticado, jamás de un parámetro del request."

**Alternatives considered**:
- Recibir `usuarioId` en el body/query — Rechazado: viola Principio I (IDOR).
- Validar en el service con el ID del token — Adoptado como patrón estándar.

## Decision 3: DTO de respuesta

**Decision**: No se crea DTO específico. Se retorna `204 No Content` en caso de éxito.

**Rationale**: Para DELETE, 204 es el código estándar. No se necesita cuerpo de respuesta. Si la cuenta no existe, 404. Si no pertenece al usuario, 403.

**Alternatives considered**:
- Retornar la entidad eliminada — Rechazado: innecesario y viola Principio II (no exponer entidades).
- Retornar un mensaje de éxito — Rechazado: 204 es suficiente.

## Decision 4: Frontend — Confirmación nativa

**Decision**: Usar `window.confirm()` nativo del navegador para la confirmación antes de eliminar.

**Rationale**: Es simple, no requiere CSS adicional, y es accesible. El spec dice "muestra una confirmación" sin especificar tipo. `confirm()` cumple el requisito mínimo.

**Alternatives considered**:
- Modal custom con CSS — Posible pero innecesario para v1. Mejora futura.
- SweetAlert2 u otra librería — Rechazado: el proyecto no usa librerías JS externas.

## Decision 5: Recarga de lista tras eliminación

**Decision**: Tras recibir 204 del backend, llamar a `cargarCuentasVinculadas()` de nuevo para recargar la lista.

**Rationale**: Reutiliza la función existente en auth.js. Evita manipulación manual del DOM.

**Alternatives considered**:
- Eliminar el div del DOM directamente — Rechazado: menos robusto, puede desincronizar con el backend.
