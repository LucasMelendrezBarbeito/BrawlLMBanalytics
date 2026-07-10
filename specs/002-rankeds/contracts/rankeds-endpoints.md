# API Contract: Rankeds Endpoints

## 1. GET /rankeds/global

Obtiene el Top 200 global de jugadores ranked.

### Authentication

No requerida (endpoint público).

### Response Codes

| Code | Description |
|------|-------------|
| 200 | Lista de jugadores |
| 500 | Error al contactar la API de Supercell |

### Response Body

```json
{
  "items": [
    {
      "tag": "#LGVY0QGP9",
      "name": "SLC|Elox",
      "iconId": 28000123,
      "trophies": 300202,
      "rank": 1,
      "clubName": "Slice"
    }
  ]
}
```

---

## 2. GET /rankeds/local/{countryCode}

Obtiene el Top 200 de un país específico.

### Authentication

No requerida (endpoint público).

### Path Parameters

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| countryCode | String | Yes | Código de país ISO (ej: "es", "us", "br") |

### Response Codes

| Code | Description |
|------|-------------|
| 200 | Lista de jugadores |
| 400 | Código de país inválido |
| 500 | Error al contactar la API de Supercell |

### Response Body

Igual que `/rankeds/global`.

---

## 3. GET /rankeds/cuenta/{cuentaId}

Obtiene el detalle ranked de una cuenta vinculada (temporadas + partidas).

### Authentication

Requerida. JWT token via `Authorization: Bearer <token>` o cookie `jwt`.

### Path Parameters

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| cuentaId | Integer | Yes | ID de la cuenta vinculada |

### Response Codes

| Code | Description |
|------|-------------|
| 200 | Detalle ranked |
| 401 | No autenticado |
| 403 | La cuenta no pertenece al usuario |
| 404 | Cuenta no encontrada |

### Response Body

```json
{
  "cuentaId": 42,
  "tag": "#P98J02C",
  "nombre": "BoredGriggsR",
  "ranked": {
    "currentRank": "Diamond",
    "currentTrophies": 1258
  },
  "temporadas": [
    {
      "seasonNumber": 5,
      "rank": "Gold",
      "trophies": 800
    }
  ],
  "ultimasPartidas": [
    {
      "battleTime": "2026-07-10T21:32:27Z",
      "mode": "duoShowdown",
      "rank": 4,
      "trophyChange": -8,
      "brawlerName": "TARA",
      "brawlerId": 16000017
    }
  ]
}
```

---

## 4. GET /rankeds/cuenta/{cuentaId}/sync

Sincroniza los datos ranked de una cuenta con la API de Supercell.

### Authentication

Requerida. JWT token.

### Response Codes

| Code | Description |
|------|-------------|
| 200 | Sincronización exitosa |
| 401 | No autenticado |
| 403 | La cuenta no pertenece al usuario |
| 404 | Cuenta no encontrada |
| 500 | Error al contactar la API de Supercell |

### Response Body

```json
{
  "message": "Datos ranked sincronizados correctamente",
  "temporadasActualizadas": 1,
  "partidasActualizadas": 10
}
```
