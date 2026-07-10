# API Contract: DELETE /cuentas/{id}

## Endpoint

```
DELETE /cuentas/{id}
```

## Authentication

Required. JWT token via `Authorization: Bearer <token>` header or `jwt` cookie.

## Path Parameters

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| id | Integer | Yes | ID de la cuenta de Brawl Stars a eliminar |

## Request

No body required.

## Response Codes

| Code | Description |
|------|-------------|
| 204 | Cuenta eliminada exitosamente (No Content) |
| 401 | No autenticado (token ausente o inválido) |
| 403 | La cuenta no pertenece al usuario autenticado |
| 404 | Cuenta no encontrada |

## Authorization Logic

1. Extraer `username` del token JWT
2. Obtener `usuario_id` del usuario autenticado
3. Buscar la cuenta por `id`
4. Si la cuenta no existe → 404
5. Si `cuenta.usuarioId != usuario_id` → 403
6. Eliminar la cuenta → 204

## Example Request

```http
DELETE /cuentas/42 HTTP/1.1
Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGci...
```

## Example Response (Success)

```http
HTTP/1.1 204 No Content
```

## Example Response (Forbidden)

```http
HTTP/1.1 403 Forbidden
Content-Type: application/json

{"message": "No tienes permiso para eliminar esta cuenta."}
```
