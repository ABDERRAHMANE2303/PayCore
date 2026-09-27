# PayCore 

PayCore is a REST API simulating a small payment-processing system where customers make payments to merchants using different payment methods.

---

# 1. Project Setup

## Stack

```text
Java 21+
Spring Boot
Spring Web
Spring Data JPA
Bean Validation
PostgreSQL
JUnit + Mockito
Maven
```

## Suggested Structure

```text
src/main/java/com/paycore/
│
├── controller/
├── service/
├── repository/
├── entity/
├── dto/
├── exception/
├── processor/
├── config/
└── PayCoreApplication.java
```

## Main Domain

```text
Customer
Merchant
Payment
Transaction
```

Relationships:

```text
Customer 1 ───── * Payment * ───── 1 Merchant

                    │
                    │ 1
                    │
                    *
               Transaction
```

---

# 2. Customers

## Model

```text
Customer

id          UUID
name        String
status      CustomerStatus
createdAt   LocalDateTime
```

```java
enum CustomerStatus {
    ACTIVE,
    INACTIVE
}
```

New customers start as:

```text
ACTIVE
```

---

## Create Customer

```http
POST /api/v1/customers
```

Request:

```json
{
  "name": "Alice Martin"
}
```

Validate:

```text
name != null
name != blank
```

Expected:

```text
201 Created
```

Response:

```json
{
  "id": "...",
  "name": "Alice Martin",
  "status": "ACTIVE",
  "createdAt": "..."
}
```

Do **not** accept `id`, `status`, or `createdAt` from the client.

---

## Get Customer

```http
GET /api/v1/customers/{id}
```

Expected:

```text
200 -> customer exists
404 -> customer doesn't exist
```

---

## List Customers

```http
GET /api/v1/customers
```

Optional filter:

```http
GET /api/v1/customers?status=ACTIVE
```

Return customer response DTOs, not entities.

---

## Change Customer Status

```http
PATCH /api/v1/customers/{id}/status
```

Request:

```json
{
  "status": "INACTIVE"
}
```

Allowed:

```text
ACTIVE
INACTIVE
```

Expected:

```text
200 -> updated
404 -> customer missing
400 -> invalid status/input
```

An inactive customer **cannot create a new payment**.

---

# 3. Merchants

## Model

```text
Merchant

id          UUID
name        String
category    MerchantCategory
status      MerchantStatus
createdAt   LocalDateTime
```

```java
enum MerchantCategory {
    ECOMMERCE,
    RESTAURANT,
    SERVICES,
    TRANSPORT,
    OTHER
}
```

```java
enum MerchantStatus {
    ACTIVE,
    INACTIVE
}
```

---

## Create Merchant

```http
POST /api/v1/merchants
```

Request:

```json
{
  "name": "Atlas Store",
  "category": "ECOMMERCE"
}
```

Validate:

```text
name      -> not blank
category  -> not null
```

Server generates:

```text
id
status = ACTIVE
createdAt
```

Expected:

```text
201 Created
```

---

## Get Merchant

```http
GET /api/v1/merchants/{id}
```

```text
200 -> found
404 -> missing
```

---

## List Merchants

```http
GET /api/v1/merchants
```

Optional:

```http
GET /api/v1/merchants?status=ACTIVE
GET /api/v1/merchants?category=ECOMMERCE
```

---

## Change Merchant Status

```http
PATCH /api/v1/merchants/{id}/status
```

Request:

```json
{
  "status": "INACTIVE"
}
```

Inactive merchants cannot receive new payments.

---

# 4. Payments

## Model

```text
Payment

id              UUID
customer        Customer
merchant        Merchant
amount          BigDecimal
currency        Currency
method          PaymentMethod
status          PaymentStatus
createdAt       LocalDateTime
processedAt     LocalDateTime?
refundedAt      LocalDateTime?
```

Keep currencies deliberately small:

```java
enum Currency {
    MAD,
    EUR,
    USD
}
```

Payment methods:

```java
enum PaymentMethod {
    CARD,
    BANK_TRANSFER,
    WALLET
}
```

Statuses:

```java
enum PaymentStatus {
    PENDING,
    PROCESSING,
    SUCCESS,
    FAILED,
    REFUNDED
}
```

---

# 5. Create Payment

```http
POST /api/v1/payments
```

Request:

```json
{
  "customerId": "...",
  "merchantId": "...",
  "amount": 750.00,
  "currency": "MAD",
  "method": "CARD"
}
```

Validate DTO:

```text
customerId -> not null
merchantId -> not null
amount     -> not null + positive
currency   -> not null
method     -> not null
```

Business validation:

```text
customer exists
merchant exists
customer is ACTIVE
merchant is ACTIVE
```

New payment:

```text
status = PENDING
```

Expected responses:

| Situation | Status |
|---|---:|
| Created | `201` |
| Customer missing | `404` |
| Merchant missing | `404` |
| Invalid input | `400` |
| Customer inactive | `409` |
| Merchant inactive | `409` |

Do not process the payment during creation.

Creation and processing are deliberately separate operations.

---

# 6. Payment Processing

Use an interface:

```java
public interface PaymentProcessor {

    ProcessingResult process(Payment payment);
}
```

Implement:

```text
CardPaymentProcessor
BankTransferPaymentProcessor
WalletPaymentProcessor
```

Spring should inject/manage these implementations.

The service chooses the appropriate processor based on:

```java
payment.getMethod()
```

The exact selection implementation is yours.

---

## Simulated Rules

Keep the processors deterministic and simple.

### CARD

```text
Maximum amount: 20,000
Fee: 2%
```

### BANK_TRANSFER

```text
Maximum amount: 100,000
Fee: 1%
```

### WALLET

```text
Maximum amount: 5,000
Fee: 0.5%
```

If the amount exceeds the method limit:

```text
processing -> FAILED
```

Otherwise:

```text
processing -> SUCCESS
```

No randomness.

No external API.

No actual movement of money.

The fee exists to give each implementation different behavior. Store it on the generated transaction.

---

# 7. Process Payment Endpoint

```http
POST /api/v1/payments/{id}/process
```

Only:

```text
PENDING -> PROCESSING -> SUCCESS
                       ↘ FAILED
```

is allowed.

Reject processing when status is:

```text
PROCESSING
SUCCESS
FAILED
REFUNDED
```

Processing should:

```text
1. Load payment
2. Validate current state
3. Set PROCESSING
4. Select PaymentProcessor
5. Process
6. Create Transaction
7. Set SUCCESS or FAILED
8. Set processedAt
9. Return result
```

Wrap the operation in a transaction.

Expected:

| Situation | Status |
|---|---:|
| Processed | `200` |
| Payment missing | `404` |
| Invalid payment state | `409` |

Response example:

```json
{
  "paymentId": "...",
  "status": "SUCCESS",
  "transactionId": "...",
  "fee": 15.00
}
```

---

# 8. Payment State Rules

Keep state logic inside the `Payment` entity rather than scattering it across controllers.

Useful domain methods could include:

```java
payment.startProcessing();

payment.markSuccessful();

payment.markFailed();

payment.markRefunded();
```

Each method should protect its own valid transition.

Example:

```text
markSuccessful()

PROCESSING -> SUCCESS    ✓
PENDING    -> SUCCESS    ✗
REFUNDED   -> SUCCESS    ✗
```

Invalid transitions should throw a domain exception.

---

# 9. Transactions

## Model

```text
Transaction

id          UUID
payment     Payment
type        TransactionType
amount      BigDecimal
fee         BigDecimal
status      TransactionStatus
createdAt   LocalDateTime
```

```java
enum TransactionType {
    PAYMENT,
    REFUND
}
```

```java
enum TransactionStatus {
    SUCCESS,
    FAILED
}
```

Every **processing attempt** creates one transaction.

Example:

```text
Payment
PAY-001

Transaction
TXN-001
type   = PAYMENT
amount = 750
fee    = 15
status = SUCCESS
```

Payment and Transaction are separate:

```text
Payment     -> business operation/current state
Transaction -> record of a processing/refund attempt
```

---

# 10. Refund Payment

```http
POST /api/v1/payments/{id}/refund
```

For now support **full refunds only**.

Rules:

```text
payment must exist
payment.status must be SUCCESS
refund amount = original payment amount
payment cannot be refunded twice
```

Processing:

```text
1. Load payment
2. Validate SUCCESS
3. Create REFUND transaction
4. amount = original amount
5. fee = 0
6. transaction status = SUCCESS
7. payment -> REFUNDED
8. set refundedAt
```

Expected:

| Situation | Status |
|---|---:|
| Refunded | `200` |
| Payment missing | `404` |
| Payment not refundable | `409` |

Response:

```json
{
  "paymentId": "...",
  "status": "REFUNDED",
  "transactionId": "...",
  "refundedAmount": 750.00
}
```

No partial refunds in the initial implementation.

---

# 11. Get Payment

```http
GET /api/v1/payments/{id}
```

Response:

```json
{
  "id": "...",
  "customerId": "...",
  "merchantId": "...",
  "amount": 750.00,
  "currency": "MAD",
  "method": "CARD",
  "status": "SUCCESS",
  "createdAt": "...",
  "processedAt": "..."
}
```

```text
200 -> found
404 -> missing
```

Don't expose the complete nested JPA entities.

Return IDs / useful fields through a DTO.

---

# 12. List, Filter & Sort Payments

```http
GET /api/v1/payments
```

Support optional filters:

```text
customerId
merchantId
status
method
minAmount
maxAmount
```

Examples:

```http
GET /api/v1/payments?status=SUCCESS

GET /api/v1/payments?method=CARD

GET /api/v1/payments?customerId={id}

GET /api/v1/payments?minAmount=100&maxAmount=1000
```

Support pagination:

```http
GET /api/v1/payments?page=0&size=20
```

Support sorting:

```http
GET /api/v1/payments?sort=amount,desc

GET /api/v1/payments?sort=createdAt,asc
```

Combined example:

```http
GET /api/v1/payments?status=SUCCESS&method=CARD&page=0&size=20&sort=createdAt,desc
```

Filtering/sorting should happen through the persistence layer.

**Do not** deliberately:

```java
repository.findAll()
    .stream()
    .filter(...)
```

for database filtering.

Use Spring Data/JPA queries.

You don't need to build an advanced dynamic query framework. Implement the filters progressively using simple repository queries or a small query solution if needed.

---

# 13. List Payment Transactions

```http
GET /api/v1/payments/{id}/transactions
```

Returns all processing/refund transactions for the payment.

Example:

```json
[
  {
    "id": "...",
    "type": "PAYMENT",
    "amount": 750.00,
    "fee": 15.00,
    "status": "SUCCESS",
    "createdAt": "..."
  }
]
```

```text
200 -> payment exists
404 -> payment missing
```

---

# 14. Statistics

Keep this feature deliberately small.

```http
GET /api/v1/payments/statistics
```

Return:

```json
{
  "totalPayments": 42,
  "successfulPayments": 31,
  "failedPayments": 7,
  "pendingPayments": 4,
  "successfulVolume": 18450.00,
  "averageSuccessfulPayment": 595.16
}
```

Also calculate:

```text
count by status
volume by payment method
```

This feature can deliberately use Java Streams after loading the required data because its purpose is partly to refresh:

```text
filter
map
reduce
groupingBy
counting
averaging
```

Don't over-engineer analytics or optimize it for millions of records.

---

# 15. Validation

## DTO Validation

Use Bean Validation for structural input validation:

```text
@NotNull
@NotBlank
@Positive
@Size
@Valid
```

Example:

```java
public record CreatePaymentRequest(

    @NotNull UUID customerId,

    @NotNull UUID merchantId,

    @NotNull @Positive BigDecimal amount,

    @NotNull Currency currency,

    @NotNull PaymentMethod method

) {}
```

## Business Validation

Keep business rules in services/entities:

```text
customer exists
merchant exists
customer active
merchant active
valid payment transition
payment refundable
processor limit
```

Do not try to express business rules through Bean Validation annotations.

---

# 16. Exception Handling

Create useful exceptions such as:

```text
ResourceNotFoundException
InvalidPaymentStateException
InactiveCustomerException
InactiveMerchantException
```

Use:

```java
@RestControllerAdvice
```

with:

```java
@ExceptionHandler
```

Return a consistent error response:

```json
{
  "status": 404,
  "error": "Not Found",
  "message": "Payment does not exist",
  "timestamp": "..."
}
```

Minimum mapping:

| Situation | HTTP |
|---|---:|
| Invalid DTO | `400` |
| Resource missing | `404` |
| Invalid state/business conflict | `409` |
| Unexpected failure | `500` |

---

# 17. Persistence

Use PostgreSQL.

Repositories:

```text
CustomerRepository
MerchantRepository
PaymentRepository
TransactionRepository
```

Each should extend:

```java
JpaRepository<Entity, UUID>
```

Main relationships:

```text
Payment.customer     -> @ManyToOne
Payment.merchant     -> @ManyToOne
Transaction.payment  -> @ManyToOne
```

Prefer:

```java
FetchType.LAZY
```

for these relationships.

Store enums using:

```java
@Enumerated(EnumType.STRING)
```

Use:

```java
BigDecimal
```

for amounts and fees.

Use:

```java
LocalDateTime
```

for timestamps.

---

# 18. Transaction Boundaries

Use `@Transactional` around operations involving multiple related database changes.

Most important:

```text
process payment
refund payment
```

Example:

```text
PROCESS PAYMENT TRANSACTION

load payment
     ↓
change payment state
     ↓
run processor
     ↓
create transaction record
     ↓
change final payment state
     ↓
COMMIT
```

If an unexpected exception occurs before completion:

```text
ROLLBACK
```

Simple read endpoints don't need unnecessary transaction handling.

---

# 19. Testing

Don't aim for exhaustive coverage.

## Service Tests

Use JUnit + Mockito for the important business behavior:

```text
create valid payment
reject inactive customer
reject inactive merchant

process valid PENDING payment
reject processing SUCCESS payment
processor succeeds under limit
processor fails over limit

refund SUCCESS payment
reject refund of PENDING payment
reject second refund
```

## Repository Tests

Use `@DataJpaTest` for a few custom queries/filtering cases.

## Controller Tests

Use `@WebMvcTest` + MockMvc for representative endpoints:

```text
valid POST -> expected success status
invalid DTO -> 400
missing resource -> 404
business conflict -> 409
```

No need to test every trivial getter or framework behavior.

---

# 20. API Summary

## Customers

| Method | Endpoint | Purpose |
|---|---|---|
| `POST` | `/api/v1/customers` | Create |
| `GET` | `/api/v1/customers/{id}` | Get |
| `GET` | `/api/v1/customers` | List/filter |
| `PATCH` | `/api/v1/customers/{id}/status` | Change status |

## Merchants

| Method | Endpoint | Purpose |
|---|---|---|
| `POST` | `/api/v1/merchants` | Create |
| `GET` | `/api/v1/merchants/{id}` | Get |
| `GET` | `/api/v1/merchants` | List/filter |
| `PATCH` | `/api/v1/merchants/{id}/status` | Change status |

## Payments

| Method | Endpoint | Purpose |
|---|---|---|
| `POST` | `/api/v1/payments` | Create payment |
| `GET` | `/api/v1/payments/{id}` | Get payment |
| `GET` | `/api/v1/payments` | List/filter/sort/page |
| `POST` | `/api/v1/payments/{id}/process` | Process |
| `POST` | `/api/v1/payments/{id}/refund` | Refund |
| `GET` | `/api/v1/payments/{id}/transactions` | Payment history |
| `GET` | `/api/v1/payments/statistics` | Statistics |

---


# Implementation Order

```text
1. Bootstrap project + PostgreSQL
2. Customer entity/repository/service/controller
3. Merchant entity/repository/service/controller
4. DTO validation + global exceptions
5. Payment entity + creation
6. PaymentProcessor interface + implementations
7. Payment processing + transactions
8. Transaction entity/history
9. Refunds
10. Filtering + sorting + pagination
11. Statistics
12. Tests
```

## Main Rule

The goal is **not to invent requirements while coding**.

When implementing a feature:

```text
Read requirement
      ↓
Identify Spring/Java syntax needed
      ↓
Implement it
      ↓
Test endpoint
      ↓
Move on
```

Focus on writing the code yourself and becoming comfortable again with Java, Spring Boot, JPA, DTOs, validation, transactions, and tests.