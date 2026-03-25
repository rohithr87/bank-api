# Bank Account Management REST API

A RESTful Banking API built with Java, Spring Boot, and MariaDB. Supports account management, money operations (deposit, withdraw, transfer), and transaction history.

## Tech Stack

- **Java 17**
- **Spring Boot 3.5.12**
- **Spring Data JPA**
- **MariaDB**
- **JUnit 5**
- **Maven**

## Architecture

```
Controller Layer → Service Layer → Repository Layer → Database
```

- **Model Layer** — Account, Transaction entities (OOP: Encapsulation)
- **Repository Layer** — JpaRepository interfaces (Database access)
- **Service Layer** — Interface + Implementation (OOP: Abstraction, Polymorphism)
- **Controller Layer** — REST API endpoints
- **Exception Layer** — Custom exceptions + Global handler (OOP: Inheritance)

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/accounts | Create new account |
| GET | /api/accounts | Get all accounts |
| GET | /api/accounts/{id} | Get account by ID |
| PUT | /api/accounts/{id} | Update account |
| DELETE | /api/accounts/{id} | Delete account |
| POST | /api/accounts/{id}/deposit | Deposit money |
| POST | /api/accounts/{id}/withdraw | Withdraw money |
| POST | /api/accounts/transfer | Transfer between accounts |
| GET | /api/accounts/{id}/transactions | View transaction history |

## Sample Requests

### Create Account
```bash
curl -X POST http://localhost:8080/api/accounts \
  -H "Content-Type: application/json" \
  -d '{"accountHolderName": "Rohith", "accountType": "SAVINGS", "balance": 10000}'
```

### Deposit
```bash
curl -X POST http://localhost:8080/api/accounts/1/deposit \
  -H "Content-Type: application/json" \
  -d '{"amount": 5000}'
```

### Transfer
```bash
curl -X POST http://localhost:8080/api/accounts/transfer \
  -H "Content-Type: application/json" \
  -d '{"fromId": 1, "toId": 2, "amount": 3000}'
```

## Error Handling

- **404** — Account not found
- **400** — Insufficient balance
- **500** — Internal server error

## How to Run

1. Clone the repository
2. Create MariaDB database: `CREATE DATABASE bank_db;`
3. Update `application.properties` with your DB credentials
4. Run: `mvn spring-boot:run`
5. API available at: `http://localhost:8080/api/accounts`

## Run Tests

```bash
mvn test
```

## OOP Concepts Used

- **Encapsulation** — Private fields with getters/setters in entities
- **Abstraction** — AccountService interface
- **Inheritance** — Custom exceptions extend RuntimeException
- **Polymorphism** — AccountServiceImpl implements AccountService interface
