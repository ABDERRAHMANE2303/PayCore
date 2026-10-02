# Java Practical Cheat Sheet

## Navigation

- [1. Core Java Syntax](#1-core-java-syntax)
  - [1.1 Program Structure](#11-program-structure)
  - [1.2 Variables & Basic Types](#12-variables--basic-types)
  - [1.3 Operators](#13-operators)
  - [1.4 Conditions](#14-conditions)
  - [1.5 Switch](#15-switch)
  - [1.6 Loops](#16-loops)
  - [1.7 Methods](#17-methods)
  - [1.8 Type Conversion & Parsing](#18-type-conversion--parsing)
  - [1.9 Basic Input & Output](#19-basic-input--output)

- [2. Classes & Practical OOP](#2-classes--practical-oop)
  - [2.1 Classes & Objects](#21-classes--objects)
  - [2.2 Constructors & this](#22-constructors--this)
  - [2.3 Encapsulation](#23-encapsulation)
  - [2.4 Access Modifiers](#24-access-modifiers)
  - [2.5 static & final](#25-static--final)
  - [2.6 Inheritance](#26-inheritance)
  - [2.7 Overriding & Overloading](#27-overriding--overloading)
  - [2.8 Interfaces](#28-interfaces)
  - [2.9 Abstract Classes](#29-abstract-classes)
  - [2.10 Polymorphism](#210-polymorphism)
  - [2.11 Composition](#211-composition)
  - [2.12 instanceof & Casting](#212-instanceof--casting)
  - [2.13 Records](#213-records)

- [3. Core Data Structures](#3-core-data-structures)
  - [3.1 Arrays](#31-arrays)
  - [3.2 List & ArrayList](#32-list--arraylist)
  - [3.3 Set & HashSet](#33-set--hashset)
  - [3.4 Map & HashMap](#34-map--hashmap)
  - [3.5 Enums](#35-enums)
  - [3.6 Sorting & Comparator](#36-sorting--comparator)

- [4. Common Java Types & Utilities](#4-common-java-types--utilities)
  - [4.1 String](#41-string)
  - [4.2 StringBuilder](#42-stringbuilder)
  - [4.3 Math](#43-math)
  - [4.4 Dates & Time](#44-dates--time)
  - [4.5 UUID](#45-uuid)
  - [4.6 Objects](#46-objects)

- [5. Object Equality & Null Handling](#5-object-equality--null-handling)
  - [5.1 == vs equals](#51--vs-equals)
  - [5.2 equals & hashCode](#52-equals--hashcode)
  - [5.3 Null Handling](#53-null-handling)
  - [5.4 Optional](#54-optional)

- [6. Exceptions](#6-exceptions)
  - [6.1 try / catch / finally](#61-try--catch--finally)
  - [6.2 throw vs throws](#62-throw-vs-throws)
  - [6.3 Checked vs Unchecked](#63-checked-vs-unchecked)
  - [6.4 Custom Exceptions](#64-custom-exceptions)
  - [6.5 Try-With-Resources](#65-try-with-resources)

- [7. Lambdas & Streams](#7-lambdas--streams)
  - [7.1 Lambdas](#71-lambdas)
  - [7.2 Stream Basics](#72-stream-basics)
  - [7.3 Filtering & Mapping](#73-filtering--mapping)
  - [7.4 Searching & Matching](#74-searching--matching)
  - [7.5 Sorting](#75-sorting)
  - [7.6 Aggregation & Grouping](#76-aggregation--grouping)

- [8. Practical Backend Patterns](#8-practical-backend-patterns)
- [9. Unit Testing](#9-unit-testing)

---

# 1. Core Java Syntax

## 1.1 Program Structure

Java code lives inside classes.

```java
public class Main {

    public static void main(String[] args) {
        System.out.println("Hello Java");
    }
}
```

`main()` is the standard application entry point.

```java
public static void main(String[] args)
```

- `public` → JVM can access it.
- `static` → no `Main` object needs to be created.
- `void` → returns nothing.
- `String[] args` → command-line arguments.

Classes normally live in their own files:

```text
Account.java
Transaction.java
PaymentService.java
Main.java
```

A public class normally has the same name as its file.

---

## 1.2 Variables & Basic Types

Java is statically typed.

```java
String name = "Ali";
int age = 23;
double balance = 1500.50;
boolean active = true;
char grade = 'A';
```

### Primitive Types

Most commonly used:

```java
int number = 10;
long largeNumber = 5_000_000_000L;

double amount = 99.99;
float ratio = 1.5F;

boolean active = true;
char letter = 'A';

byte smallNumber = 10;
short mediumNumber = 1000;
```

For normal backend code, the ones encountered constantly are:

```text
int
long
double
boolean
```

### Primitive vs Reference Types

Primitive:

```java
int age = 25;
boolean active = true;
```

Reference:

```java
String name = "Ali";
Account account = new Account();
List<String> names = new ArrayList<>();
```

A reference variable points to an object.

```java
Account a = new Account();
Account b = a;

// a and b reference the SAME object
```

### Wrapper Classes

Each primitive has an object equivalent.

```text
int      -> Integer
long     -> Long
double   -> Double
boolean  -> Boolean
char     -> Character
```

Collections require reference types:

```java
List<Integer> numbers = new ArrayList<>();

// NOT:
List<int> numbers; // ERROR
```

### Autoboxing / Unboxing

Java automatically converts between primitives and wrappers when possible.

```java
Integer number = 10;  // int -> Integer
int value = number;   // Integer -> int
```

### var

Java can infer local variable types:

```java
var name = "Ali";          // String
var age = 25;              // int
var account = new Account();
```

The type is still fixed at compile time.

```java
var age = 25;

// age = "Ali"; // ERROR
```

`var` only works for local variables where Java can infer the type.

---

## 1.3 Operators

### Arithmetic

```java
a + b
a - b
a * b
a / b
a % b
```

Integer division:

```java
int result = 5 / 2;       // 2

double result = 5.0 / 2;  // 2.5
```

Increment / decrement:

```java
count++;
count--;

count += 5;
count -= 5;
count *= 2;
count /= 2;
```

### Comparison

```java
a == b
a != b

a > b
a < b
a >= b
a <= b
```

IMPORTANT:

For objects, `==` compares references.

```java
string1.equals(string2);   // logical/value comparison
```

More on this later.

### Logical Operators

```java
&&      // AND
||      // OR
!       // NOT
```

Example:

```java
if (amount > 0 && account.isActive()) {
    // ...
}
```

### Ternary Operator

```java
String status = active ? "ACTIVE" : "INACTIVE";
```

Equivalent to:

```java
String status;

if (active) {
    status = "ACTIVE";
} else {
    status = "INACTIVE";
}
```

---

## 1.4 Conditions

```java
if (condition) {
    // ...
} else if (otherCondition) {
    // ...
} else {
    // ...
}
```

Example:

```java
if (balance >= amount) {
    System.out.println("Payment allowed");
} else {
    System.out.println("Insufficient balance");
}
```

Multiple conditions:

```java
if (account.isActive() && balance >= amount) {
    // ...
}
```

Java requires boolean conditions.

```java
int number = 5;

// if (number) {}       // ERROR
if (number != 0) {}     // valid
```

Unlike Python, values such as `0`, `""`, and empty collections are not automatically falsy.

---

## 1.5 Switch

Classic switch:

```java
switch (status) {

    case "PENDING":
        System.out.println("Waiting");
        break;

    case "SUCCESS":
        System.out.println("Completed");
        break;

    default:
        System.out.println("Unknown");
}
```

Modern switch expression:

```java
String message = switch (status) {
    case "PENDING" -> "Waiting";
    case "SUCCESS" -> "Completed";
    case "FAILED" -> "Failed";
    default -> "Unknown";
};
```

Very useful with enums:

```java
PaymentStatus status = PaymentStatus.PENDING;

String message = switch (status) {
    case PENDING -> "Waiting";
    case COMPLETED -> "Payment completed";
    case FAILED -> "Payment failed";
};
```

---

## 1.6 Loops

### Standard For Loop

```java
for (int i = 0; i < 10; i++) {
    System.out.println(i);
}
```

Access index + value:

```java
for (int i = 0; i < accounts.size(); i++) {
    Account account = accounts.get(i);

    System.out.println(i);
    System.out.println(account);
}
```

### Enhanced For Loop

Preferred when the index is unnecessary.

```java
for (Account account : accounts) {
    System.out.println(account);
}
```

Equivalent conceptually to Python:

```text
for account in accounts
```

### While

```java
while (condition) {
    // ...
}
```

Example:

```java
while (balance > 0) {
    balance -= 100;
}
```

### break / continue

```java
for (Account account : accounts) {

    if (!account.isActive()) {
        continue;
    }

    if (account.getId().equals(targetId)) {
        break;
    }
}
```

- `continue` → skip current iteration.
- `break` → exit loop.

---

## 1.7 Methods

Basic method:

```java
public int add(int a, int b) {
    return a + b;
}
```

No return value:

```java
public void printAccount(Account account) {
    System.out.println(account);
}
```

### Parameters

```java
public void transfer(Account source, Account destination, double amount) {
    // ...
}
```

### Return Objects

```java
public Account findAccount(long id) {
    // ...
}
```

### Java Is Pass-by-Value

Java always passes arguments by value.

For primitives, the value itself is copied:

```java
void change(int number) {
    number = 100;
}

int x = 10;
change(x);

// x is still 10
```

For objects, the copied value is the reference:

```java
void deactivate(Account account) {
    account.deactivate();
}
```

The method receives a copy of the reference, but both references point to the same object.

### Method Overloading

Same method name, different parameter list:

```java
public void pay(double amount) {
    // ...
}

public void pay(double amount, String currency) {
    // ...
}
```

Java decides which method to call from the arguments.

---

## 1.8 Type Conversion & Parsing

### Primitive Conversion

Implicit widening:

```java
int number = 10;
double value = number;
```

Explicit narrowing:

```java
double value = 10.8;
int number = (int) value;

// 10
```

Decimal part is discarded.

### String -> Number

```java
int number = Integer.parseInt("42");

long id = Long.parseLong("100");

double amount = Double.parseDouble("99.50");
```

### Number -> String

```java
String text = String.valueOf(42);

String amount = Double.toString(99.5);
```

### Enum Parsing

```java
PaymentStatus status =
        PaymentStatus.valueOf("PENDING");
```

Often normalize external input:

```java
PaymentStatus status =
        PaymentStatus.valueOf(input.toUpperCase());
```

---

## 1.9 Basic Input & Output

Output:

```java
System.out.println("Hello");
System.out.print("Hello");
```

Formatted output:

```java
String name = "Ali";
double balance = 1000;

System.out.printf(
    "Account %s has %.2f MAD%n",
    name,
    balance
);
```

Input:

```java
import java.util.Scanner;

Scanner scanner = new Scanner(System.in);

System.out.print("Name: ");
String name = scanner.nextLine();

System.out.print("Age: ");
int age = scanner.nextInt();

System.out.print("Amount: ");
double amount = scanner.nextDouble();
```

Be careful when mixing `nextInt()` / `nextDouble()` with `nextLine()` because the newline can remain in the input buffer.

For small CLI applications, a simple approach is often:

```java
String input = scanner.nextLine();

int age = Integer.parseInt(input);
```

---

# 2. Classes & Practical OOP

## 2.1 Classes & Objects

A class defines state + behavior.

```java
public class Account {

    private String id;
    private String owner;
    private double balance;

    public void deposit(double amount) {
        balance += amount;
    }
}
```

Create an object:

```java
Account account = new Account();
```

Each object has its own state.

```java
Account account1 = new Account();
Account account2 = new Account();
```

---

## 2.2 Constructors & this

Constructors initialize objects.

```java
public class Account {

    private String id;
    private String owner;
    private double balance;

    public Account(String id, String owner, double balance) {
        this.id = id;
        this.owner = owner;
        this.balance = balance;
    }
}
```

Create:

```java
Account account =
        new Account("ACC-001", "Ali", 1000);
```

`this` refers to the current object.

```java
this.balance = balance;
```

Left:

```java
this.balance
```

means the object's field.

Right:

```java
balance
```

means the constructor parameter.

### Constructor Overloading

```java
public Account(String id, String owner) {
    this(id, owner, 0);
}

public Account(String id, String owner, double balance) {
    this.id = id;
    this.owner = owner;
    this.balance = balance;
}
```

`this(...)` calls another constructor of the same class.

---

## 2.3 Encapsulation

Keep state private and expose controlled behavior.

BAD:

```java
public class Account {
    public double balance;
}
```

Anyone can now do:

```java
account.balance = -500000;
```

Better:

```java
public class Account {

    private double balance;

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {

        if (amount <= 0) {
            throw new IllegalArgumentException(
                "Amount must be positive"
            );
        }

        balance += amount;
    }
}
```

And instead of blindly exposing:

```java
setBalance(...)
```

prefer domain behavior:

```java
deposit(...)
withdraw(...)
freeze(...)
activate(...)
```

The object protects its own valid state.

---

## 2.4 Access Modifiers

### public

Accessible from anywhere.

```java
public void deposit(double amount) {}
```

Used for behavior intentionally exposed to other classes.

### private

Accessible only inside the class.

```java
private double balance;
```

Typical choice for fields.

### protected

Accessible from:

- same package
- subclasses

```java
protected void validate() {}
```

Commonly useful for extension points in inheritance / abstract classes.

### Package-Private

No modifier:

```java
void internalOperation() {}
```

Accessible inside the same package.

### Practical Default

For domain objects:

```java
private fields
public business methods
```

Expose only what other classes actually need.

---

## 2.5 static & final

### final Variable

Can only be assigned once.

```java
final String id = "ACC-001";
```

Common with fields:

```java
private final String id;
```

Initialize through constructor:

```java
public Account(String id) {
    this.id = id;
}
```

IMPORTANT:

`final` freezes the reference, not necessarily the object.

```java
final List<String> names = new ArrayList<>();

names.add("Ali");        // VALID

// names = new ArrayList<>(); // ERROR
```

### static

Belongs to the class rather than an individual object.

```java
public class Payment {

    public static int paymentCount = 0;
}
```

Access:

```java
Payment.paymentCount;
```

Static method:

```java
public static boolean isValidAmount(double amount) {
    return amount > 0;
}
```

Call:

```java
Payment.isValidAmount(100);
```

### Constants

Usually:

```java
public static final int MAX_RETRIES = 3;

public static final String DEFAULT_CURRENCY = "MAD";
```

Naming convention:

```text
UPPER_SNAKE_CASE
```

---

## 2.6 Inheritance

Inheritance represents an **is-a** relationship.

```java
public class Payment {
    // common payment behavior
}
```

```java
public class CardPayment extends Payment {
    // card-specific behavior
}
```

A `CardPayment` **is a** `Payment`.

Subclass inherits accessible behavior from the parent.

```java
public class Payment {

    protected double amount;

    public void printAmount() {
        System.out.println(amount);
    }
}
```

```java
public class CardPayment extends Payment {

    public void authorize() {
        // amount is accessible because it is protected
    }
}
```

### super

Access parent constructor:

```java
public CardPayment(double amount, String cardNumber) {
    super(amount);

    this.cardNumber = cardNumber;
}
```

Access parent behavior:

```java
super.validate();
```

Prefer inheritance when there is a genuine **is-a** relationship.

---

## 2.7 Overriding & Overloading

### Overriding

Subclass provides a different implementation of inherited behavior.

```java
public class Payment {

    public void process() {
        System.out.println("Processing payment");
    }
}
```

```java
public class CardPayment extends Payment {

    @Override
    public void process() {
        System.out.println("Processing card payment");
    }
}
```

Use `@Override`.

It lets the compiler verify that you're actually overriding an existing method.

### Overloading

Same method name, different parameters:

```java
public void transfer(Account account, double amount) {}

public void transfer(
        Account account,
        double amount,
        String description
) {}
```

Summary:

```text
Overloading -> same class/name, different parameters
Overriding  -> subclass replaces inherited implementation
```

---

## 2.8 Interfaces

Interfaces define behavior/contracts.

```java
public interface PaymentProcessor {

    void process(Payment payment);
}
```

Implement it:

```java
public class CardPaymentProcessor
        implements PaymentProcessor {

    @Override
    public void process(Payment payment) {
        System.out.println("Processing card payment");
    }
}
```

Another implementation:

```java
public class BankTransferProcessor
        implements PaymentProcessor {

    @Override
    public void process(Payment payment) {
        System.out.println("Processing bank transfer");
    }
}
```

Interfaces are especially useful when multiple implementations provide the same capability.

```java
PaymentProcessor processor =
        new CardPaymentProcessor();
```

Code can depend on:

```java
PaymentProcessor
```

instead of:

```java
CardPaymentProcessor
```

This reduces coupling.

---

## 2.9 Abstract Classes

Abstract classes are useful when related subclasses share state or part of an algorithm.

```java
public abstract class PaymentProcessor {

    public final void process(Payment payment) {

        validate(payment);

        execute(payment);

        log(payment);
    }

    protected void validate(Payment payment) {
        // shared validation
    }

    protected abstract void execute(Payment payment);

    protected void log(Payment payment) {
        // shared logging
    }
}
```

Subclass:

```java
public class CardPaymentProcessor
        extends PaymentProcessor {

    @Override
    protected void execute(Payment payment) {
        // card-specific processing
    }
}
```

Cannot instantiate:

```java
// new PaymentProcessor(); // ERROR
```

Practical distinction:

```text
Interface
-> defines a capability / contract
-> implementations may be unrelated

Abstract class
-> related subclasses share state or behavior
-> provides partial implementation
```

---

## 2.10 Polymorphism

Different implementations can be treated as the same type.

```java
PaymentProcessor processor =
        new CardPaymentProcessor();

processor.process(payment);
```

Later:

```java
processor =
        new BankTransferProcessor();

processor.process(payment);
```

The calling code works with:

```java
PaymentProcessor
```

while runtime behavior depends on the actual implementation.

Collections can also contain different implementations:

```java
List<PaymentProcessor> processors = List.of(
    new CardPaymentProcessor(),
    new BankTransferProcessor()
);

for (PaymentProcessor processor : processors) {
    processor.process(payment);
}
```

---

## 2.11 Composition

Composition represents a **has-a** relationship.

```java
public class PaymentService {

    private final PaymentProcessor processor;

    public PaymentService(PaymentProcessor processor) {
        this.processor = processor;
    }

    public void pay(Payment payment) {
        processor.process(payment);
    }
}
```

`PaymentService` **has a** `PaymentProcessor`.

Create:

```java
PaymentProcessor processor =
        new CardPaymentProcessor();

PaymentService service =
        new PaymentService(processor);
```

Switch implementation:

```java
PaymentProcessor processor =
        new BankTransferProcessor();

PaymentService service =
        new PaymentService(processor);
```

This is constructor-based dependency injection even without Spring.

General rule:

```text
Inheritance -> IS A
Composition -> HAS A
```

Prefer composition when you simply need another object's behavior rather than actually being that type.

---

## 2.12 instanceof & Casting

Check runtime type:

```java
if (payment instanceof CardPayment) {
    // ...
}
```

Modern pattern matching:

```java
if (payment instanceof CardPayment cardPayment) {
    cardPayment.authorize();
}
```

Older equivalent:

```java
if (payment instanceof CardPayment) {

    CardPayment cardPayment =
            (CardPayment) payment;

    cardPayment.authorize();
}
```

Avoid excessive `instanceof` chains when polymorphism can express the behavior more cleanly.

Instead of:

```java
if (payment instanceof CardPayment) {
    // card processing
} else if (payment instanceof BankTransfer) {
    // transfer processing
}
```

often prefer:

```java
payment.process();
```

with each subtype implementing its own behavior.

---

## 2.13 Records

Records are concise immutable data carriers.

```java
public record PaymentRequest(
    String accountId,
    double amount,
    String currency
) {}
```

Java automatically provides:

- constructor
- accessors
- `equals()`
- `hashCode()`
- `toString()`

Use:

```java
PaymentRequest request =
        new PaymentRequest(
            "ACC-001",
            500,
            "MAD"
        );
```

Access:

```java
request.accountId();
request.amount();
request.currency();
```

Not:

```java
request.getAmount();
```

Useful when an object mainly carries data rather than mutable domain behavior.

---

# 3. Core Data Structures

## 3.1 Arrays

Fixed-size collection.

```java
int[] numbers = {1, 2, 3};

String[] names = {
    "Ali",
    "Sara",
    "Omar"
};
```

Create with size:

```java
int[] numbers = new int[5];
```

Access:

```java
numbers[0];

numbers[0] = 10;
```

Length:

```java
numbers.length;
```

Loop:

```java
for (int number : numbers) {
    System.out.println(number);
}
```

Arrays cannot grow.

For most application-level dynamic collections, prefer `List`.

---

## 3.2 List & ArrayList

`List` represents an ordered collection.

`ArrayList` is the most common implementation.

```java
import java.util.ArrayList;
import java.util.List;

List<String> names = new ArrayList<>();
```

Prefer declaring using the interface:

```java
List<String> names = new ArrayList<>();
```

rather than:

```java
ArrayList<String> names = new ArrayList<>();
```

### Add

```java
names.add("Ali");

names.add("Sara");
```

Insert at index:

```java
names.add(1, "Omar");
```

### Access

```java
names.get(0);
```

### Replace

```java
names.set(0, "Ahmed");
```

### Remove

```java
names.remove("Ali");

names.remove(0);
```

Be careful with `List<Integer>`:

```java
List<Integer> numbers =
        new ArrayList<>(List.of(10, 20, 30));

numbers.remove(1);
```

removes index `1`, not the value `1`.

Remove a specific Integer:

```java
numbers.remove(Integer.valueOf(20));
```

### Size

```java
names.size();
```

### Membership

```java
names.contains("Ali");
```

### Empty Check

```java
names.isEmpty();
```

### Clear

```java
names.clear();
```

### Iterate

```java
for (String name : names) {
    System.out.println(name);
}
```

### Immutable List Creation

```java
List<String> currencies =
        List.of("MAD", "EUR", "USD");
```

Do not modify it:

```java
// currencies.add("GBP"); // ERROR
```

### Mutable Copy

```java
List<String> currencies =
        new ArrayList<>(
            List.of("MAD", "EUR", "USD")
        );
```

---

## 3.3 Set & HashSet

A `Set` stores unique elements.

```java
import java.util.HashSet;
import java.util.Set;

Set<String> currencies = new HashSet<>();
```

Add:

```java
currencies.add("MAD");
currencies.add("EUR");

currencies.add("MAD"); // duplicate ignored
```

Check membership:

```java
currencies.contains("MAD");
```

Remove:

```java
currencies.remove("MAD");
```

Size:

```java
currencies.size();
```

Loop:

```java
for (String currency : currencies) {
    System.out.println(currency);
}
```

Typical average complexity for `HashSet`:

```text
add       O(1)
contains  O(1)
remove    O(1)
```

Hash-based collections depend on correct `equals()` and `hashCode()` implementations.

---

## 3.4 Map & HashMap

A `Map` stores:

```text
key -> value
```

```java
import java.util.HashMap;
import java.util.Map;

Map<String, Account> accounts =
        new HashMap<>();
```

### Add / Replace

```java
accounts.put(
    account.getId(),
    account
);
```

If the key already exists, the value is replaced.

### Access

```java
Account account =
        accounts.get("ACC-001");
```

Missing key:

```java
accounts.get("UNKNOWN"); // null
```

### Check Key

```java
accounts.containsKey("ACC-001");
```

### Check Value

```java
accounts.containsValue(account);
```

### Default Value

```java
int count =
        counts.getOrDefault(status, 0);
```

Very common counting pattern:

```java
counts.put(
    status,
    counts.getOrDefault(status, 0) + 1
);
```

### Remove

```java
accounts.remove("ACC-001");
```

### Iterate Keys

```java
for (String id : accounts.keySet()) {
    System.out.println(id);
}
```

### Iterate Values

```java
for (Account account : accounts.values()) {
    System.out.println(account);
}
```

### Iterate Key + Value

```java
for (Map.Entry<String, Account> entry
        : accounts.entrySet()) {

    String id = entry.getKey();
    Account account = entry.getValue();
}
```

Typical average complexity for `HashMap`:

```text
put          O(1)
get          O(1)
containsKey  O(1)
remove       O(1)
```

---

## 3.5 Enums

Enums represent a fixed set of valid values.

```java
public enum PaymentStatus {
    PENDING,
    COMPLETED,
    FAILED,
    CANCELLED
}
```

Use:

```java
PaymentStatus status =
        PaymentStatus.PENDING;
```

Compare enums with `==`:

```java
if (status == PaymentStatus.PENDING) {
    // ...
}
```

All values:

```java
PaymentStatus.values();
```

Parse:

```java
PaymentStatus.valueOf("PENDING");
```

Enums can contain state and behavior too:

```java
public enum Currency {

    MAD("Moroccan Dirham"),
    EUR("Euro"),
    USD("US Dollar");

    private final String label;

    Currency(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
```

Use:

```java
Currency.MAD.getLabel();
```

---

## 3.6 Sorting & Comparator

Natural sorting:

```java
List<Integer> numbers =
        new ArrayList<>(List.of(5, 2, 8));

numbers.sort(null);
```

Strings:

```java
names.sort(null);
```

Reverse:

```java
names.sort(
    Comparator.reverseOrder()
);
```

### Sort Objects by Field

```java
transactions.sort(
    Comparator.comparing(
        Transaction::getAmount
    )
);
```

Equivalent lambda:

```java
transactions.sort(
    Comparator.comparing(
        transaction -> transaction.getAmount()
    )
);
```

Descending:

```java
transactions.sort(
    Comparator.comparing(
        Transaction::getAmount
    ).reversed()
);
```

Multiple criteria:

```java
transactions.sort(
    Comparator
        .comparing(Transaction::getStatus)
        .thenComparing(Transaction::getCreatedAt)
);
```

---

# 4. Common Java Types & Utilities

## 4.1 String

Strings are immutable.

```java
String text = "PayCore";
```

Length:

```java
text.length();
```

Character:

```java
text.charAt(0);
```

Substring:

```java
text.substring(0, 3);
```

Comparison:

```java
text.equals("PayCore");

text.equalsIgnoreCase("paycore");
```

NEVER use `==` for normal String value comparison:

```java
text == "PayCore"; // reference comparison
```

### Search

```java
text.contains("Core");

text.startsWith("Pay");

text.endsWith("Core");

text.indexOf("Core");
```

### Case

```java
text.toLowerCase();

text.toUpperCase();
```

### Cleaning

```java
text.trim();
```

Modern whitespace-aware version:

```java
text.strip();
```

### Empty / Blank

```java
text.isEmpty();   // length == 0

text.isBlank();   // empty OR whitespace only
```

### Replace

```java
text.replace("Pay", "Fast");
```

Returns a new String.

### Split

```java
String input = "MAD,EUR,USD";

String[] currencies =
        input.split(",");
```

### Join

```java
String result =
        String.join(
            ", ",
            "MAD",
            "EUR",
            "USD"
        );
```

### Formatting

```java
String message =
        "Payment %s: %.2f MAD"
            .formatted(id, amount);
```

---

## 4.2 StringBuilder

Strings are immutable.

Repeated concatenation creates new String objects.

For many modifications:

```java
StringBuilder builder =
        new StringBuilder();

builder.append("Payment ");
builder.append(id);
builder.append(" completed");

String result =
        builder.toString();
```

Other operations:

```java
builder.insert(index, value);

builder.delete(start, end);

builder.reverse();

builder.length();
```

---

## 4.3 Math

```java
Math.max(a, b);

Math.min(a, b);

Math.abs(number);

Math.round(number);

Math.floor(number);

Math.ceil(number);

Math.pow(a, b);

Math.sqrt(number);
```

Random double:

```java
Math.random();
```

returns:

```text
0.0 <= value < 1.0
```

---

## 4.4 Dates & Time

Prefer the modern `java.time` API.

### LocalDate

Date without time.

```java
import java.time.LocalDate;

LocalDate today =
        LocalDate.now();

LocalDate date =
        LocalDate.of(
            2026,
            9,
            27
        );
```

Operations:

```java
today.plusDays(5);

today.minusDays(5);

today.plusMonths(1);
```

Compare:

```java
date.isBefore(today);

date.isAfter(today);

date.isEqual(today);
```

### LocalDateTime

Date + time.

```java
import java.time.LocalDateTime;

LocalDateTime now =
        LocalDateTime.now();
```

Operations:

```java
now.plusHours(2);

now.minusMinutes(30);
```

### Duration

Difference between times.

```java
import java.time.Duration;

Duration duration =
        Duration.between(start, end);

long minutes =
        duration.toMinutes();
```

---

## 4.5 UUID

Useful for generating identifiers.

```java
import java.util.UUID;

UUID id =
        UUID.randomUUID();
```

Convert to String:

```java
String id =
        UUID.randomUUID().toString();
```

Parse:

```java
UUID id =
        UUID.fromString(text);
```

---

## 4.6 Objects

Useful null-safe object utilities.

```java
import java.util.Objects;
```

Null-safe equality:

```java
Objects.equals(a, b);
```

Works even if one is null.

Require non-null:

```java
this.id =
    Objects.requireNonNull(id);
```

Custom message:

```java
this.id =
    Objects.requireNonNull(
        id,
        "Account ID cannot be null"
    );
```

Generate hash:

```java
Objects.hash(id, owner);
```

---

# 5. Object Equality & Null Handling

## 5.1 == vs equals

For primitives:

```java
int a = 10;
int b = 10;

a == b; // value comparison
```

For objects:

```java
account1 == account2;
```

checks whether both references point to the same object.

Logical equality:

```java
account1.equals(account2);
```

Example:

```java
Account a =
        new Account("ACC-001");

Account b =
        new Account("ACC-001");

a == b;       // false

a.equals(b);  // depends on equals implementation
```

---

## 5.2 equals & hashCode

By default, custom objects effectively use identity-based equality.

If business equality is based on an ID:

```java
@Override
public boolean equals(Object object) {

    if (this == object) {
        return true;
    }

    if (!(object instanceof Account account)) {
        return false;
    }

    return Objects.equals(
        id,
        account.id
    );
}
```

And:

```java
@Override
public int hashCode() {
    return Objects.hash(id);
}
```

Contract:

```text
If a.equals(b) == true

then

a.hashCode() == b.hashCode()
```

This matters especially with:

```java
HashSet
HashMap
```

Example:

```java
Set<Account> accounts =
        new HashSet<>();

accounts.add(
    new Account("ACC-001")
);

accounts.contains(
    new Account("ACC-001")
);
```

For this to behave according to ID equality, `equals()` and `hashCode()` must agree.

---

## 5.3 Null Handling

A reference can contain:

```java
null
```

Example:

```java
Account account = null;
```

This fails:

```java
account.getBalance();

// NullPointerException
```

Check:

```java
if (account != null) {
    account.getBalance();
}
```

Safer equality:

```java
Objects.equals(a, b);
```

Validate required values early:

```java
Objects.requireNonNull(account);
```

Avoid spreading `null` unnecessarily through business logic.

---

## 5.4 Optional

`Optional<T>` represents:

```text
value exists
OR
value is absent
```

Example:

```java
Optional<Account> account =
        findAccount(id);
```

Create:

```java
Optional.of(account);
```

Value may be null:

```java
Optional.ofNullable(account);
```

Empty:

```java
Optional.empty();
```

### Check

```java
account.isPresent();

account.isEmpty();
```

### Get Fallback

```java
Account result =
        account.orElse(defaultAccount);
```

Lazy fallback:

```java
Account result =
        account.orElseGet(
            () -> createDefaultAccount()
        );
```

### Throw If Missing

Very common:

```java
Account account =
    findAccount(id)
        .orElseThrow(
            () -> new AccountNotFoundException(id)
        );
```

### Transform

```java
Optional<String> owner =
    account.map(Account::getOwner);
```

Avoid blindly doing:

```java
account.get();
```

because it defeats much of the purpose of `Optional`.

Most commonly, use `Optional` for return values where absence is legitimate.

---

# 6. Exceptions

## 6.1 try / catch / finally

```java
try {

    int number =
        Integer.parseInt(input);

} catch (NumberFormatException e) {

    System.out.println(
        "Invalid number"
    );

} finally {

    System.out.println(
        "Finished"
    );
}
```

`finally` executes whether an exception occurs or not.

Multiple catches:

```java
try {
    // ...
} catch (NumberFormatException e) {
    // ...
} catch (IllegalArgumentException e) {
    // ...
}
```

---

## 6.2 throw vs throws

### throw

Actually throws an exception.

```java
if (amount <= 0) {
    throw new IllegalArgumentException(
        "Amount must be positive"
    );
}
```

### throws

Declares that a method may propagate an exception.

```java
public void readFile()
        throws IOException {

    // ...
}
```

Think:

```text
throw  -> DO it

throws -> DECLARE it
```

---

## 6.3 Checked vs Unchecked

### Checked Exceptions

Compiler forces handling or declaration.

Example:

```java
IOException
SQLException
```

Must:

```java
try {
    // ...
} catch (IOException e) {
    // ...
}
```

or:

```java
public void method()
        throws IOException {
}
```

### Unchecked Exceptions

Extend `RuntimeException`.

Examples:

```java
IllegalArgumentException
NullPointerException
IllegalStateException
IndexOutOfBoundsException
```

Compiler does not force handling.

Business/domain failures are commonly represented using custom runtime exceptions.

---

## 6.4 Custom Exceptions

```java
public class InsufficientBalanceException
        extends RuntimeException {

    public InsufficientBalanceException(
            double balance,
            double requestedAmount
    ) {

        super(
            "Balance " + balance +
            " is insufficient for " +
            requestedAmount
        );
    }
}
```

Use:

```java
if (amount > balance) {

    throw new InsufficientBalanceException(
        balance,
        amount
    );
}
```

Another example:

```java
public class AccountNotFoundException
        extends RuntimeException {

    public AccountNotFoundException(String id) {
        super(
            "Account not found: " + id
        );
    }
}
```

---

## 6.5 Try-With-Resources

Resources such as files and streams should be closed.

Instead of manually closing:

```java
BufferedReader reader = ...;

try {
    // use reader
} finally {
    reader.close();
}
```

use:

```java
try (
    BufferedReader reader =
        Files.newBufferedReader(path)
) {

    String line =
        reader.readLine();
}
```

Java automatically closes the resource.

Works with objects implementing:

```java
AutoCloseable
```

---

# 7. Lambdas & Streams

## 7.1 Lambdas

Lambda:

```java
(parameters) -> expression
```

Example:

```java
name -> name.toUpperCase()
```

Multiple parameters:

```java
(a, b) -> a + b
```

Multiple statements:

```java
account -> {

    System.out.println(
        account.getId()
    );

    return account.isActive();
}
```

Lambdas implement functional interfaces.

A functional interface has one abstract method.

```java
@FunctionalInterface
public interface PaymentValidator {

    boolean validate(Payment payment);
}
```

Lambda implementation:

```java
PaymentValidator validator =
        payment ->
            payment.getAmount() > 0;
```

Common standard functional interfaces:

```text
Predicate<T>      T -> boolean

Function<T, R>    T -> R

Consumer<T>       T -> void

Supplier<T>       () -> T
```

Examples:

```java
Predicate<Account> active =
        account -> account.isActive();

Function<Account, String> owner =
        account -> account.getOwner();

Consumer<Account> printer =
        account -> System.out.println(account);

Supplier<UUID> idGenerator =
        () -> UUID.randomUUID();
```

---

## 7.2 Stream Basics

Streams process collections declaratively.

```java
List<Account> accounts = ...;

accounts.stream();
```

A stream pipeline normally contains:

```text
source
-> intermediate operations
-> terminal operation
```

Example:

```java
List<Account> activeAccounts =
    accounts.stream()
        .filter(Account::isActive)
        .toList();
```

`filter()` is intermediate.

`toList()` is terminal.

Streams are lazy:

```java
accounts.stream()
    .filter(account -> {
        System.out.println(account);
        return account.isActive();
    });
```

Nothing executes yet.

Add terminal operation:

```java
accounts.stream()
    .filter(account -> {
        System.out.println(account);
        return account.isActive();
    })
    .toList();
```

Now the pipeline executes.

---

## 7.3 Filtering & Mapping

### filter

Keep elements satisfying a condition.

```java
List<Transaction> successful =
    transactions.stream()
        .filter(
            transaction ->
                transaction.getStatus()
                    == PaymentStatus.COMPLETED
        )
        .toList();
```

### map

Transform each element.

```java
List<String> ids =
    transactions.stream()
        .map(Transaction::getId)
        .toList();
```

Conceptually:

```text
Transaction -> String
```

### distinct

Remove duplicates:

```java
List<String> currencies =
    transactions.stream()
        .map(Transaction::getCurrency)
        .distinct()
        .toList();
```

### limit

```java
List<Transaction> firstFive =
    transactions.stream()
        .limit(5)
        .toList();
```

### skip

```java
transactions.stream()
    .skip(10)
    .limit(10)
    .toList();
```

---

## 7.4 Searching & Matching

### findFirst

```java
Optional<Account> account =
    accounts.stream()
        .filter(
            a -> a.getId().equals(id)
        )
        .findFirst();
```

Very common:

```java
Account account =
    accounts.stream()
        .filter(
            a -> a.getId().equals(id)
        )
        .findFirst()
        .orElseThrow(
            () -> new AccountNotFoundException(id)
        );
```

### anyMatch

```java
boolean exists =
    accounts.stream()
        .anyMatch(
            a -> a.getId().equals(id)
        );
```

### allMatch

```java
boolean allActive =
    accounts.stream()
        .allMatch(Account::isActive);
```

### noneMatch

```java
boolean noneBlocked =
    accounts.stream()
        .noneMatch(Account::isBlocked);
```

---

## 7.5 Sorting

Ascending:

```java
List<Transaction> sorted =
    transactions.stream()
        .sorted(
            Comparator.comparing(
                Transaction::getAmount
            )
        )
        .toList();
```

Descending:

```java
List<Transaction> sorted =
    transactions.stream()
        .sorted(
            Comparator
                .comparing(
                    Transaction::getAmount
                )
                .reversed()
        )
        .toList();
```

Sort by date:

```java
transactions.stream()
    .sorted(
        Comparator.comparing(
            Transaction::getCreatedAt
        )
    )
    .toList();
```

---

## 7.6 Aggregation & Grouping

### Count

```java
long count =
    transactions.stream()
        .filter(
            transaction ->
                transaction.getStatus()
                    == PaymentStatus.COMPLETED
        )
        .count();
```

### Sum

```java
double total =
    transactions.stream()
        .mapToDouble(
            Transaction::getAmount
        )
        .sum();
```

Average:

```java
double average =
    transactions.stream()
        .mapToDouble(
            Transaction::getAmount
        )
        .average()
        .orElse(0);
```

### Grouping

```java
import java.util.stream.Collectors;
```

Group transactions by status:

```java
Map<PaymentStatus, List<Transaction>>
    byStatus =

    transactions.stream()
        .collect(
            Collectors.groupingBy(
                Transaction::getStatus
            )
        );
```

Count by status:

```java
Map<PaymentStatus, Long> countByStatus =
    transactions.stream()
        .collect(
            Collectors.groupingBy(
                Transaction::getStatus,
                Collectors.counting()
            )
        );
```

### Loop vs Stream

Don't force streams everywhere.

Simple mutation:

```java
for (Account account : accounts) {
    account.deactivate();
}
```

is often clearer than trying to make everything a stream.

Streams are particularly useful for:

```text
filter
transform
search
sort
aggregate
group
```

Normal loops remain perfectly valid when imperative logic is clearer.

---

# 8. Practical Backend Patterns

## Find Object by ID

Loop:

```java
public Account findById(String id) {

    for (Account account : accounts) {

        if (account.getId().equals(id)) {
            return account;
        }
    }

    throw new AccountNotFoundException(id);
}
```

Stream:

```java
public Account findById(String id) {

    return accounts.stream()
        .filter(
            account ->
                account.getId().equals(id)
        )
        .findFirst()
        .orElseThrow(
            () -> new AccountNotFoundException(id)
        );
}
```

---

## Check Whether Something Exists

```java
boolean exists =
    accounts.stream()
        .anyMatch(
            account ->
                account.getId().equals(id)
        );
```

With Map:

```java
accountsById.containsKey(id);
```

If lookup by ID is extremely common, a `Map<ID, Object>` may be a better representation than repeatedly scanning a `List`.

---

## Filter Transactions

```java
List<Transaction> failed =
    transactions.stream()
        .filter(
            transaction ->
                transaction.getStatus()
                    == PaymentStatus.FAILED
        )
        .toList();
```

Multiple conditions:

```java
List<Transaction> result =
    transactions.stream()
        .filter(
            transaction ->
                transaction.getAmount() >= 100
        )
        .filter(
            transaction ->
                transaction.getStatus()
                    == PaymentStatus.COMPLETED
        )
        .toList();
```

---

## Sort Transactions

```java
List<Transaction> newest =
    transactions.stream()
        .sorted(
            Comparator
                .comparing(
                    Transaction::getCreatedAt
                )
                .reversed()
        )
        .toList();
```

---

## Calculate Total Amount

```java
double total =
    transactions.stream()
        .mapToDouble(
            Transaction::getAmount
        )
        .sum();
```

Filtered:

```java
double completedTotal =
    transactions.stream()
        .filter(
            transaction ->
                transaction.getStatus()
                    == PaymentStatus.COMPLETED
        )
        .mapToDouble(
            Transaction::getAmount
        )
        .sum();
```

---

## Group by Status

```java
Map<PaymentStatus, List<Transaction>>
    transactionsByStatus =

    transactions.stream()
        .collect(
            Collectors.groupingBy(
                Transaction::getStatus
            )
        );
```

---

## Protect Domain State

Avoid:

```java
account.setBalance(
    account.getBalance() - amount
);
```

Prefer:

```java
account.withdraw(amount);
```

Inside:

```java
public void withdraw(double amount) {

    if (amount <= 0) {
        throw new IllegalArgumentException(
            "Amount must be positive"
        );
    }

    if (amount > balance) {
        throw new InsufficientBalanceException(
            balance,
            amount
        );
    }

    balance -= amount;
}
```

The object maintains its own invariants.

---

## Protect Internal Collections

Potentially dangerous:

```java
public List<Transaction> getTransactions() {
    return transactions;
}
```

Caller can now modify your internal collection:

```java
account
    .getTransactions()
    .clear();
```

Return an unmodifiable view:

```java
public List<Transaction> getTransactions() {
    return Collections.unmodifiableList(
        transactions
    );
}
```

Or an immutable copy:

```java
public List<Transaction> getTransactions() {
    return List.copyOf(transactions);
}
```

Then expose controlled behavior:

```java
public void addTransaction(
        Transaction transaction
) {

    transactions.add(transaction);
}
```

---

## Interface -> Implementation

Contract:

```java
public interface PaymentProcessor {

    void process(Payment payment);
}
```

Implementation:

```java
public class CardPaymentProcessor
        implements PaymentProcessor {

    @Override
    public void process(Payment payment) {
        // implementation
    }
}
```

Consumer:

```java
public class PaymentService {

    private final PaymentProcessor processor;

    public PaymentService(
            PaymentProcessor processor
    ) {

        this.processor = processor;
    }

    public void pay(Payment payment) {
        processor.process(payment);
    }
}
```

Composition + abstraction + runtime polymorphism:

```text
PaymentService
      |
      v
PaymentProcessor
      |
      +---- CardPaymentProcessor
      |
      +---- BankTransferProcessor
```

---

## Validate Early

Instead of letting invalid data propagate:

```java
public void transfer(
        Account source,
        Account destination,
        double amount
) {

    Objects.requireNonNull(source);
    Objects.requireNonNull(destination);

    if (amount <= 0) {
        throw new IllegalArgumentException(
            "Amount must be positive"
        );
    }

    source.withdraw(amount);
    destination.deposit(amount);
}
```

Fail as close as possible to the violated rule.

---

## Common Collection Decision

```text
Need ordered dynamic elements?
-> List / ArrayList

Need unique elements?
-> Set / HashSet

Need key -> value lookup?
-> Map / HashMap

Need fixed constants/states?
-> enum

Need fixed-size low-level collection?
-> array
```

---

# 9. Unit Testing

## What a Unit Test Does

A unit test runs one small piece of code (often one method or class) and checks that its behavior matches expectations. It runs automatically and fails the build when an assertion is false.

JUnit 5 is the common Java test framework. A test is a normal method marked with `@Test`:

```java
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AmountRulesTest {

    @Test
    void rejectsZeroAmount() {
        IllegalArgumentException error = assertThrows(
            IllegalArgumentException.class,
            () -> validateAmount(0)
        );

        assertEquals("Amount must be positive", error.getMessage());
    }
}
```

If the expected exception is not thrown, or an assertion fails, the test fails. Tests do not usually print results or catch failures themselves; the test runner reports them.

## Arrange, Act, Assert

Structure each test into three steps:

```text
Arrange: prepare inputs and dependencies
Act:     call the behavior under test
Assert:  check the result or side effect
```

Test observable behavior, including important edge cases. Give tests descriptive names such as `rejectsZeroAmount` or `returnsCustomerWhenFound`. Keep each test focused on one behavior.

## Test Doubles

A mock is a fake dependency controlled by the test. Use one when you want to test a class without involving a database, network, or another service. A mock is not automatically a unit test; the test's scope depends on what real code it runs.

Use real objects for simple values and domain logic where practical. Avoid mocking the class whose behavior the test is meant to verify.
