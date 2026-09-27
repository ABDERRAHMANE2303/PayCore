# RESTful API Practical Cheat Sheet

## Navigation

- [1. HTTP Methods](#1-http-methods)
- [2. URLs & Request Data](#2-urls--request-data)
- [3. Status Codes](#3-status-codes)
- [4. Responses & Errors](#4-responses--errors)
- [5. Pagination, Filtering & Sorting](#5-pagination-filtering--sorting)
- [6. Core Rules](#6-core-rules)
- [7. Quick Example](#7-quick-example)

---

# 1. HTTP Methods

Idempotent :` doing the same operation multiple times has the same intended effect on the server state as doing it once.

| Method | Use | Idempotent | Example | Typical Success |
|---|---|---:|---|---|
| `GET` | Read | ✅ | `GET /payments/123` | `200` |
| `POST` | Create | ❌ | `POST /payments` | `201` |
| `PUT` | Full replace | ✅ | `PUT /payments/123` | `200 / 204` |
| `PATCH` | Partial update | Not guaranteed | `PATCH /payments/123` | `200 / 204` |
| `DELETE` | Delete | ✅ | `DELETE /payments/123` | `204` |

### PUT vs PATCH

```text
PUT   -> replace the resource
PATCH -> modify selected fields
```

### POST for Async Operations

If processing continues after the request:

```http
POST /reports
-> 202 Accepted
```

---

# 2. URLs & Request Data

## URL Rules

```text
Use resources/nouns       -> /users, /payments
Use plural collections    -> /users/{id}
Use lowercase
Avoid verbs               -> /getUsers ❌
Version when needed       -> /api/v1/payments
```

Nested resource:

```http
GET /accounts/123/transactions
```

Don't over-nest resources.

---

## Where Does Data Go?

| Data | Location | Example |
|---|---|---|
| Resource ID | Path | `/payments/123` |
| Filters/search | Query | `/payments?status=FAILED` |
| Pagination/sorting | Query | `?page=0&size=20&sort=date,desc` |
| Create/update data | Body | JSON |
| Auth / metadata | Headers | `Authorization`, `Content-Type` |

### Common Headers

```http
Content-Type: application/json
Accept: application/json
Authorization: Bearer <token>
```

---

# 3. Status Codes

## Most Used

| Code | Meaning | Use |
|---:|---|---|
| `200` | OK | Successful request with body |
| `201` | Created | Resource created |
| `202` | Accepted | Async processing started |
| `204` | No Content | Success without body |
| `400` | Bad Request | Invalid/malformed request |
| `401` | Unauthorized | Missing/invalid authentication |
| `403` | Forbidden | Authenticated but not allowed |
| `404` | Not Found | Resource doesn't exist |
| `409` | Conflict | Duplicate/state conflict |
| `422` | Unprocessable Content | Semantic/validation error |
| `500` | Internal Server Error | Unexpected server failure |

```text
401 -> not authenticated
403 -> authenticated, not authorized
```

Less common infrastructure errors:

```text
502 -> bad response from upstream
503 -> service unavailable
504 -> upstream timeout
```

---

# 4. Responses & Errors

## Resource Response

```json
{
  "id": "PAY-001",
  "amount": 500,
  "currency": "MAD",
  "status": "COMPLETED"
}
```

## Consistent Error Structure

```json
{
  "status": 404,
  "error": "Not Found",
  "message": "Payment PAY-001 does not exist"
}
```

Validation can include field errors:

```json
{
  "status": 400,
  "errors": {
    "amount": "must be positive"
  }
}
```

For `201 Created`, optionally return the created resource and/or:

```http
Location: /api/v1/payments/PAY-001
```

---

# 5. Pagination, Filtering & Sorting

Use query parameters:

```http
GET /payments?status=COMPLETED
GET /payments?page=0&size=20
GET /payments?sort=createdAt,desc
```

Combined:

```http
GET /payments?status=COMPLETED&page=0&size=20&sort=createdAt,desc
```

Typical paginated response:

```json
{
  "content": [],
  "page": 0,
  "size": 20,
  "totalElements": 100,
  "totalPages": 5
}
```

---

# 6. Core Rules

| Rule | Example / Meaning |
|---|---|
| Resources, not actions | `/payments` instead of `/getPayments` |
| Path identifies | `/payments/123` |
| Query filters/searches | `/payments?status=FAILED` |
| Use correct HTTP method | `DELETE /payments/123` |
| Use meaningful status codes | Don't return `200` for everything |
| Stateless requests | Request contains what server needs |
| Consistent errors | Same error structure across API |
| Paginate large collections | `?page=0&size=20` |
| Don't expose sensitive data | Passwords, secrets, internal fields |

### Idempotency

Repeating an idempotent request has the same intended effect:

```text
GET     -> idempotent
PUT     -> idempotent
DELETE  -> idempotent
POST    -> normally NOT
PATCH   -> depends on operation
```

For sensitive `POST` operations such as payments, an idempotency key can prevent duplicate processing:

```http
Idempotency-Key: <unique-key>
```

---

# 7. Quick Example

### Create

```http
POST /api/v1/payments
Content-Type: application/json
```

```json
{
  "accountId": "ACC-001",
  "amount": 500,
  "currency": "MAD"
}
```

```http
201 Created
```

### Read

```http
GET /api/v1/payments/PAY-001
-> 200 OK
```

### Filter

```http
GET /api/v1/payments?status=COMPLETED
-> 200 OK
```

### Update

```http
PATCH /api/v1/payments/PAY-001
-> 200 OK
```

### Delete

```http
DELETE /api/v1/payments/PAY-001
-> 204 No Content
```

---

## Mental Model

```text
Resource       -> URL
Action         -> HTTP Method
Identification -> Path
Filter/Search  -> Query
Input Data     -> Body
Metadata/Auth  -> Headers
Result         -> Status Code
Data           -> JSON
```