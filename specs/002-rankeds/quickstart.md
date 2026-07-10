# Quickstart: Rankeds

**Date**: 2026-07-11

## Prerequisites

- Aplicación Spring Boot corriendo en `http://localhost:8080`
- MySQL activo
- Token de Supercell API configurado en variables de entorno

## Validation Scenarios

### Scenario 1: Ver Top 200 Global

1. Navegar a `http://localhost:8080/rankeds.html`
2. Verificar que se muestra el Top 200 global por defecto
3. **Expected**: Lista de 200 jugadores con icono de liga, puntos y nombre.

### Scenario 2: Alternar a clasificación Local

1. Pulsar el botón "Local"
2. **Expected**: Se muestra el Top 200 del país del usuario (ej: España).

### Scenario 3: Ver cuentas vinculadas

1. Iniciar sesión
2. Navegar a `http://localhost:8080/rankeds.html`
3. Verificar sección "Tus cuentas"
4. **Expected**: Se muestran las cuentas vinculadas del usuario.

### Scenario 4: Ver detalle ranked de una cuenta

1. Seleccionar una cuenta en "Tus cuentas"
2. **Expected**: Se muestra rango actual, historial de temporadas y últimas 10 partidas ranked.

### Scenario 5: Sin autenticación

1. No iniciar sesión
2. Navegar a `http://localhost:8080/rankeds.html`
3. **Expected**: Se ve la clasificación global/local pero NO la sección "Tus cuentas".

### Scenario 6: API de Supercell no disponible

1. Simular error de API (desconectar red o usar token inválido)
2. Intentar ver clasificación
3. **Expected**: Mensaje de error claro "No se pudo obtener la clasificación".

## Verification Checklist

- [ ] Top 200 global se carga correctamente
- [ ] Top 200 local se carga por país
- [ ] Botones "Top Global" y "Local" alternan la vista
- [ ] Cuentas vinculadas aparecen para usuarios autenticados
- [ ] Detalle ranked muestra temporadas y partidas
- [ ] Errores de API se manejan con mensajes claros
- [ ] Sin autenticación, no se muestra "Tus cuentas"
