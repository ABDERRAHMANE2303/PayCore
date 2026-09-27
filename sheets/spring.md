# Spring Boot Practical Cheat Sheet

## Navigation

- [1. Spring Core & Dependency Injection](#1-spring-core--dependency-injection)
- [2. Project Structure](#2-project-structure)
- [3. Controllers](#3-controllers)
- [4. DTOs & Validation](#4-dtos--validation)
- [5. JPA Entities](#5-jpa-entities)
- [6. Entity Relationships](#6-entity-relationships)
- [7. Spring Data Repositories](#7-spring-data-repositories)
- [8. Services & Transactions](#8-services--transactions)
- [9. Exception Handling](#9-exception-handling)
- [10. Configuration & Profiles](#10-configuration--profiles)
- [11. Testing](#11-testing)
- [12. Common Backend Patterns](#12-common-backend-patterns)
- [13. Quick Reference](#13-quick-reference)

---

# 1. Spring Core & Dependency Injection

Spring creates and manages application objects (**Beans**) and injects their dependencies.

```text
Controller → Service → Repository → Database
```

## Main Bean Annotations

| Annotation | Use |
|---|---|
| `@RestController` | REST controller |
| `@Service` | Business/service layer |
| `@Repository` | Persistence component |
| `@Component` | Generic Spring-managed class |
| `@Configuration` | Configuration class |
| `@Bean` | Manually register an object as a bean |

## Constructor Injection

Preferred dependency injection style:

```java
@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }
}
```

With one constructor, `@Autowired` is unnecessary.

Avoid field injection:

```java
@Autowired
private UserRepository repository;
```

---

## Interface + Implementation

```java
public interface NotificationService {
    void send(String message);
}
```

```java
@Service
public class EmailNotificationService
        implements NotificationService {

    @Override
    public void send(String message) {
        // ...
    }
}
```

Inject the abstraction:

```java
private final NotificationService notificationService;

public UserService(
        NotificationService notificationService) {

    this.notificationService = notificationService;
}
```

If multiple implementations exist:

```java
@Qualifier("emailNotificationService")
```

or mark the default:

```java
@Primary
@Service
public class EmailNotificationService
        implements NotificationService {
}
```

---

## @Configuration + @Bean

Useful when the class itself cannot/should not be annotated:

```java
@Configuration
public class AppConfig {

    @Bean
    public SomeClient someClient() {
        return new SomeClient();
    }
}
```

The returned object becomes injectable like any other bean.

---

# 2. Project Structure

Typical layered structure:

```text
src/main/java/com/example/app/
│
├── Application.java
├── controller/
├── service/
├── repository/
├── entity/
├── dto/
├── exception/
└── config/
```

| Layer | Responsibility |
|---|---|
| `controller` | HTTP input/output |
| `service` | Business logic / use cases |
| `repository` | Database access |
| `entity` | Persistent domain data |
| `dto` | API request/response models |
| `exception` | Custom errors + handlers |
| `config` | Application configuration |

Resources:

```text
src/main/resources/
├── application.yml
├── application-dev.yml
└── application-test.yml
```

Tests:

```text
src/test/java/com/example/app/
```

## Entry Point

```java
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(
            Application.class,
            args
        );
    }
}
```

Keep the application class in the root package so component scanning finds the subpackages.

---

# 3. Controllers

## Main Annotations

| Need | Annotation |
|---|---|
| REST controller | `@RestController` |
| Base path | `@RequestMapping` |
| GET | `@GetMapping` |
| POST | `@PostMapping` |
| PUT | `@PutMapping` |
| PATCH | `@PatchMapping` |
| DELETE | `@DeleteMapping` |
| JSON body | `@RequestBody` |
| Path value | `@PathVariable` |
| Query parameter | `@RequestParam` |
| Validate input | `@Valid` |

## Basic Controller

```java
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }
}
```

## GET

```java
@GetMapping("/{id}")
public UserResponse getById(
        @PathVariable UUID id) {

    return service.getById(id);
}
```

Query parameter:

```java
@GetMapping
public List<UserResponse> findAll(
        @RequestParam(required = false)
        String role) {

    return service.findAll(role);
}
```

## POST

```java
@PostMapping
public ResponseEntity<UserResponse> create(
        @Valid
        @RequestBody CreateUserRequest request) {

    UserResponse response =
        service.create(request);

    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(response);
}
```

## DELETE

```java
@DeleteMapping("/{id}")
public ResponseEntity<Void> delete(
        @PathVariable UUID id) {

    service.delete(id);

    return ResponseEntity.noContent().build();
}
```

## ResponseEntity

```java
ResponseEntity.ok(body);

ResponseEntity
    .status(HttpStatus.CREATED)
    .body(body);

ResponseEntity.noContent().build();

ResponseEntity.notFound().build();
```

Use it when explicit control over status/headers is useful. Otherwise, returning the response object directly is fine.

---

# 4. DTOs & Validation

Keep API models separate from persistence entities:

```text
HTTP ↔ DTO ↔ Service ↔ Entity ↔ Database
```

Records work well for simple DTOs.

## Request DTO

```java
public record CreateUserRequest(

    @NotBlank
    String name,

    @Email
    @NotBlank
    String email,

    @Positive
    int age

) {}
```

Trigger validation:

```java
@PostMapping
public UserResponse create(
        @Valid
        @RequestBody CreateUserRequest request) {

    return service.create(request);
}
```

## Common Validation

| Annotation | Use |
|---|---|
| `@NotNull` | Not null |
| `@NotBlank` | Non-empty String |
| `@Size(min, max)` | String/collection size |
| `@Positive` | Number > 0 |
| `@PositiveOrZero` | Number >= 0 |
| `@Email` | Email format |
| `@Valid` | Trigger / nested validation |

## Response DTO

```java
public record UserResponse(
    UUID id,
    String name,
    String email
) {}
```

Simple manual mapping:

```java
private UserResponse toResponse(User user) {

    return new UserResponse(
        user.getId(),
        user.getName(),
        user.getEmail()
    );
}
```

---

# 5. JPA Entities

## Basic Entity

```java
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(
        nullable = false,
        unique = true
    )
    private String email;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    protected User() {}

    public User(String name, String email) {
        this.name = name;
        this.email = email;
        this.status = UserStatus.ACTIVE;
    }

    // getters + domain methods
}
```

## Main Annotations

| Annotation | Use |
|---|---|
| `@Entity` | Persistent class |
| `@Table` | Table configuration |
| `@Id` | Primary key |
| `@GeneratedValue` | Generate ID |
| `@Column` | Column configuration |
| `@Enumerated(EnumType.STRING)` | Persist enum as text |
| `@Transient` | Don't persist field |

Prefer:

```java
@Enumerated(EnumType.STRING)
```

so enum values are stored as:

```text
ACTIVE
```

rather than fragile numeric ordinals.

---

## Timestamps

```java
@Column(nullable = false)
private LocalDateTime createdAt;

@PrePersist
void onCreate() {
    createdAt = LocalDateTime.now();
}
```

Update hook:

```java
@PreUpdate
void onUpdate() {
    updatedAt = LocalDateTime.now();
}
```

---

## Monetary Values

Prefer:

```java
BigDecimal amount;
```

over:

```java
double amount;
```

Operations:

```java
amount.add(other);
amount.subtract(other);

amount.compareTo(BigDecimal.ZERO) > 0;
```

---

# 6. Entity Relationships

## Quick Reference

| Relationship | Annotation |
|---|---|
| Many → One | `@ManyToOne` |
| One → Many | `@OneToMany` |
| One → One | `@OneToOne` |
| Many ↔ Many | `@ManyToMany` |

Most common parent-child model:

```text
User 1 ─────── * Order
```

## Many-to-One

```java
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(
    name = "user_id",
    nullable = false
)
private User user;
```

The table containing the foreign key normally owns the relationship.

## One-to-Many

Inverse side:

```java
@OneToMany(mappedBy = "user")
private List<Order> orders =
        new ArrayList<>();
```

`mappedBy = "user"` refers to the field:

```java
Order.user
```

Don't make relationships bidirectional unless you actually need navigation from both directions.

## Fetching

```text
LAZY  -> load relationship when needed
EAGER -> load immediately
```

Avoid eagerly loading large relationships without a reason.

---

# 7. Spring Data Repositories

## Repository

```java
public interface UserRepository
        extends JpaRepository<User, UUID> {
}
```

Spring generates the implementation.

## Built-in Methods

```java
save(entity);

findById(id);

findAll();

existsById(id);

delete(entity);

deleteById(id);

count();
```

`findById()` returns:

```java
Optional<User>
```

Typical lookup:

```java
User user =
    repository.findById(id)
        .orElseThrow(
            () -> new ResourceNotFoundException(id)
        );
```

---

## Derived Queries

Spring builds queries from method names:

```java
Optional<User> findByEmail(String email);

List<User> findByStatus(UserStatus status);

boolean existsByEmail(String email);

long countByStatus(UserStatus status);
```

Multiple conditions:

```java
List<User> findByStatusAndRole(
    UserStatus status,
    Role role
);
```

Common keywords:

```text
findBy...
existsBy...
countBy...

And
Or
Between
LessThan
GreaterThan
Containing
OrderBy
```

---

## @Query

Use when derived method names become awkward:

```java
@Query("""
    SELECT u
    FROM User u
    WHERE u.status = :status
      AND u.role = :role
""")
List<User> search(
    @Param("status") UserStatus status,
    @Param("role") Role role
);
```

JPQL uses:

```text
Entity names + Java fields
```

Native SQL:

```java
@Query(
    value = "SELECT * FROM users WHERE status = :status",
    nativeQuery = true
)
List<User> findNative(
    @Param("status") String status
);
```

Use native queries only when needed.

---

## Pagination & Sorting

```java
Page<User> findByStatus(
    UserStatus status,
    Pageable pageable
);
```

Common types:

```text
Pageable
Page<T>
PageRequest
Sort
```

Create manually:

```java
Pageable pageable =
    PageRequest.of(
        0,
        20,
        Sort.by("createdAt").descending()
    );
```

Map page contents:

```java
Page<UserResponse> result =
    repository
        .findAll(pageable)
        .map(this::toResponse);
```

---

# 8. Services & Transactions

Services coordinate business operations.

```java
@Service
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }
}
```

## Read

```java
public UserResponse getById(UUID id) {

    User user =
        repository.findById(id)
            .orElseThrow(
                () -> new ResourceNotFoundException(id)
            );

    return toResponse(user);
}
```

## Create

```java
@Transactional
public UserResponse create(
        CreateUserRequest request) {

    User user =
        new User(
            request.name(),
            request.email()
        );

    User saved =
        repository.save(user);

    return toResponse(saved);
}
```

---

## @Transactional

Use when an operation must succeed/fail as one unit:

```java
@Transactional
public void operation() {

    // database change 1
    // database change 2
    // database change 3
}
```

```text
Success   -> COMMIT
Exception -> ROLLBACK
```

A typical transaction boundary belongs in the service layer.

Read-only:

```java
@Transactional(readOnly = true)
public UserResponse getById(UUID id) {
    // ...
}
```

---

## Updating Managed Entities

```java
@Transactional
public UserResponse update(
        UUID id,
        UpdateUserRequest request) {

    User user =
        repository.findById(id)
            .orElseThrow(
                () -> new ResourceNotFoundException(id)
            );

    user.changeEmail(request.email());

    return toResponse(user);
}
```

Inside the transaction, JPA tracks the managed entity.

When it changes:

```java
user.changeEmail(...);
```

Hibernate can persist the change when the transaction commits (**dirty checking**).

An additional:

```java
repository.save(user);
```

is not required for an already-managed entity.

---

# 9. Exception Handling

Business/service code throws meaningful exceptions:

```java
throw new ResourceNotFoundException(id);
```

Centralize HTTP translation.

## Global Handler

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(
        ResourceNotFoundException.class
    )
    public ResponseEntity<ApiError> handleNotFound(
            ResourceNotFoundException ex) {

        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(
                new ApiError(
                    404,
                    ex.getMessage()
                )
            );
    }
}
```

Error DTO:

```java
public record ApiError(
    int status,
    String message
) {}
```

Typical mapping:

| Error | Status |
|---|---:|
| Resource missing | `404` |
| Invalid request | `400 / 422` |
| Duplicate/conflict | `409` |
| Unexpected failure | `500` |

Validation exceptions from `@Valid` can also be handled in the same advice.

---

# 10. Configuration & Profiles

## application.yml

```yaml
spring:
  datasource:
    url: ${DB_URL}
    username: ${DB_USERNAME}
    password: ${DB_PASSWORD}

  jpa:
    hibernate:
      ddl-auto: update
```

Environment variables:

```bash
export DB_URL=jdbc:postgresql://localhost:5432/app
export DB_USERNAME=postgres
export DB_PASSWORD=password
```

Don't hardcode production secrets.

---

## Profiles

```text
application.yml
application-dev.yml
application-test.yml
application-prod.yml
```

Activate:

```bash
SPRING_PROFILES_ACTIVE=dev
```

or:

```bash
java -jar app.jar --spring.profiles.active=dev
```

Profile-specific bean:

```java
@Profile("dev")
@Component
public class DevDataLoader {
}
```

---

## Custom Properties

Single property:

```java
@Value("${app.max-items}")
private int maxItems;
```

For grouped configuration:

```java
@ConfigurationProperties(prefix = "app")
public record AppProperties(
    int maxItems,
    String defaultLanguage
) {}
```

```yaml
app:
  max-items: 100
  default-language: en
```

---

# 11. Testing

## Service Unit Test — Mockito

```java
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserService service;
}
```

Stub dependency:

```java
when(repository.findById(id))
    .thenReturn(Optional.of(user));
```

Execute:

```java
UserResponse result =
    service.getById(id);
```

Verify:

```java
verify(repository)
    .findById(id);
```

Assertions:

```java
assertEquals(expected, actual);

assertTrue(condition);

assertThrows(
    ResourceNotFoundException.class,
    () -> service.getById(id)
);
```

## Spring Test Types

| Tool | Use |
|---|---|
| JUnit | Normal tests/assertions |
| Mockito | Mock dependencies |
| `@WebMvcTest` | Controller slice |
| MockMvc | HTTP/controller testing |
| `@DataJpaTest` | Repository/JPA slice |
| `@SpringBootTest` | Full Spring application context |

Use the smallest test scope needed.

---

# 12. Common Backend Patterns

## Find or Throw

```java
User user =
    repository.findById(id)
        .orElseThrow(
            () -> new ResourceNotFoundException(id)
        );
```

---

## Check Duplicate

```java
if (repository.existsByEmail(request.email())) {
    throw new ResourceAlreadyExistsException();
}
```

---

## Create

```text
Request DTO
   ↓
validate
   ↓
Entity
   ↓
repository.save()
   ↓
Response DTO
```

```java
User user =
    new User(
        request.name(),
        request.email()
    );

User saved =
    repository.save(user);

return toResponse(saved);
```

---

## Update

```text
find
 ↓
modify entity
 ↓
transaction commits
 ↓
response DTO
```

```java
@Transactional
public UserResponse update(
        UUID id,
        UpdateUserRequest request) {

    User user =
        repository.findById(id)
            .orElseThrow(
                () -> new ResourceNotFoundException(id)
            );

    user.changeName(request.name());

    return toResponse(user);
}
```

---

## Delete

```java
public void delete(UUID id) {

    if (!repository.existsById(id)) {
        throw new ResourceNotFoundException(id);
    }

    repository.deleteById(id);
}
```

---

## Filter

Repository:

```java
List<User> findByStatus(
    UserStatus status
);
```

Service:

```java
return repository
    .findByStatus(status)
    .stream()
    .map(this::toResponse)
    .toList();
```

---

## Paginate

```java
public Page<UserResponse> findAll(
        Pageable pageable) {

    return repository
        .findAll(pageable)
        .map(this::toResponse);
}
```

---

## Multi-Step Database Operation

```java
@Transactional
public void operation() {

    EntityA a = repositoryA
        .findById(...)
        .orElseThrow(...);

    EntityB b = repositoryB
        .findById(...)
        .orElseThrow(...);

    a.update(...);
    b.update(...);

    repositoryC.save(...);
}
```

If one step fails, the transaction can roll back the operation.

---

## Standard Request Flow

```text
HTTP Request
     ↓
@RestController
     ↓
@Valid Request DTO
     ↓
@Service
     ↓
Business / Domain Logic
     ↓
@Transactional
     ↓
JpaRepository
     ↓
@Entity
     ↓
Database
     ↓
Response DTO
     ↓
HTTP Response
```

---

# 13. Quick Reference

## Spring

| Annotation | Use |
|---|---|
| `@SpringBootApplication` | Application entry |
| `@Component` | Generic bean |
| `@Service` | Service bean |
| `@Repository` | Persistence component |
| `@Configuration` | Configuration |
| `@Bean` | Register bean manually |
| `@Qualifier` | Choose implementation |
| `@Primary` | Default implementation |
| `@Transactional` | Transaction boundary |

## Web

| Annotation | Use |
|---|---|
| `@RestController` | REST controller |
| `@RequestMapping` | Base path |
| `@GetMapping` | GET |
| `@PostMapping` | POST |
| `@PutMapping` | PUT |
| `@PatchMapping` | PATCH |
| `@DeleteMapping` | DELETE |
| `@RequestBody` | Body |
| `@PathVariable` | Path value |
| `@RequestParam` | Query value |
| `@Valid` | Validate |

## JPA

| Annotation | Use |
|---|---|
| `@Entity` | Persistent class |
| `@Table` | Table |
| `@Id` | Primary key |
| `@GeneratedValue` | Generated ID |
| `@Column` | Column options |
| `@Enumerated` | Enum persistence |
| `@ManyToOne` | Many → one |
| `@OneToMany` | One → many |
| `@JoinColumn` | Foreign key |

## Repository

```java
public interface UserRepository
        extends JpaRepository<User, UUID> {
}
```

```text
save
findById
findAll
existsById
delete
deleteById
count

findBy...
existsBy...
countBy...
```

## Mental Model

```text
Controller
    ↓
HTTP + DTOs

Service
    ↓
Use cases + transactions

Entity
    ↓
Domain/persistent state

Repository
    ↓
Database access
```