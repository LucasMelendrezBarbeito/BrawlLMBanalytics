# Quickstart: Desvinculación de Cuentas

**Date**: 2026-07-11

## Prerequisites

- Aplicación Spring Boot corriendo en `http://localhost:8080`
- MySQL activo con la tabla `cuentas_brawl` poblada
- Usuario autenticado con al menos una cuenta vinculada

## Validation Scenarios

### Scenario 1: Eliminar cuenta vinculada exitosamente

1. Iniciar sesión en `http://localhost:8080/login.html`
2. Navegar a `http://localhost:8080/cuentas.html`
3. Verificar que aparecen las cuentas vinculadas
4. Pulsar "Eliminar" en una cuenta
5. Confirmar la eliminación en el diálogo
6. **Expected**: La cuenta desaparece de la lista. Mensaje de éxito.

### Scenario 2: Cancelar eliminación

1. Pulsar "Eliminar" en una cuenta
2. Cancelar en el diálogo de confirmación
3. **Expected**: La cuenta permanece en la lista.

### Scenario 3: Intentar eliminar cuenta de otro usuario (vía API)

```bash
# Con token de usuario A, intentar eliminar cuenta de usuario B
curl -X DELETE http://localhost:8080/cuentas/{id_cuenta_usuario_B} \
  -H "Authorization: Bearer <token_usuario_A>"
```

**Expected**: HTTP 403 Forbidden

### Scenario 4: Cuenta inexistente

```bash
curl -X DELETE http://localhost:8080/cuentas/99999 \
  -H "Authorization: Bearer <token>"
```

**Expected**: HTTP 404 Not Found

### Scenario 5: Sin autenticación

```bash
curl -X DELETE http://localhost:8080/cuentas/42
```

**Expected**: HTTP 401 Unauthorized

## Verification Checklist

- [ ] Eliminación exitosa retorna 204
- [ ] La lista se recarga automáticamente tras eliminación
- [ ] Cuenta de otro usuario retorna 403
- [ ] Cuenta inexistente retorna 404
- [ ] Sin autenticación retorna 401
- [ ] El botón "Eliminar" solo aparece para cuentas del usuario autenticado
