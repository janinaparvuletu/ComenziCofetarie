# Bakery Orders Management System – Spring Boot Web Application

## 1. Introduction
This project represents a web-based order management system for a bakery, developed as an academic project using the **Spring Boot** framework.  
The application implements a layered **MVC architecture** and provides role-based access control for managing users and cake orders.

The system integrates backend logic, database persistence, security mechanisms, and a server-side rendered frontend using **Thymeleaf**.

---

## 2. Functional Requirements

### 2.1 User Roles
The application supports three distinct user roles:

- **CLIENT**
  - authentication using email and password
  - creation of customized cake orders
  - visualization of personal orders
  - cancellation of orders in *PROCESSING* state

- **EMPLOYEE**
  - visualization of all existing orders
  - update of order status (PROCESSING, FINISHED, DELIVERED, CANCELED)

- **MANAGER**
  - full employee privileges
  - creation and deletion of users
  - management of employee accounts

---

## 3. Application Architecture

The application follows a layered architecture with a clear separation of responsibilities:

### 3.1 Entity Layer
- Domain entities: `User`, `Order`
- Enumerations used for domain constraints:
  - `Rol`
  - `Blat`, `Crema`, `Glazura`
  - `Status`
- Enum values are persisted using `EnumType.STRING` to ensure database clarity and stability.

### 3.2 Repository Layer
- Implemented using **Spring Data JPA**
- Provides database access through method name–derived queries
- Examples:
  - `findByEmail`
  - `findByRol`
  - `findByUserUsernameAndUserNrTelefon`
  - `findByStatus`

### 3.3 Service Layer
- Contains all business logic and validation rules
- Responsibilities include:
  - data validation (CNP, email, deadlines)
  - password encryption using `BCryptPasswordEncoder`
  - enforcement of order lifecycle rules
- Acts as an intermediary between controllers and repositories

### 3.4 Controller Layer
- Handles HTTP requests and response navigation
- Communicates with the service layer
- Uses `Model` objects to transfer data to the view layer
- Ensures no business logic is implemented at controller level

### 3.5 Security Layer
- Implemented using **Spring Security**
- Features:
  - authentication based on email and password
  - password encryption with BCrypt
  - role-based authorization (CLIENT / EMPLOYEE / MANAGER)
- Custom `UserDetailsService` implementation used for authentication
- Access restrictions enforced at backend level

### 3.6 View Layer
- Implemented using **Thymeleaf**
- Server-side rendered HTML pages
- Uses:
  - `th:object` and `th:field` for form binding
  - `th:each` for dynamic lists
- Enum values are displayed using combo boxes populated from backend or directly from enum types

---

## 4. Database Persistence
- Relational database: **MySQL**
- ORM framework: **Hibernate (via JPA)**
- Entity relationships:
  - `Order` → `User` (Many-to-One)
- Unique constraints applied for email and CNP fields

---

## 5. Security and Validation
- Passwords are never stored in plain text
- Input validation is performed in the service layer
- Unauthorized access is prevented both:
  - at URL level
  - at business logic level
- Role-based access ensures proper separation of privileges

---

## 6. Testing
- Unit tests implemented for the service layer
- Testing tools:
  - **JUnit**
  - **Mockito**
- Tests cover:
  - order creation and cancellation
  - validation rules
  - business constraints

---

## 7. Technologies Used
- Java
- Spring Boot
- Spring Data JPA
- Spring Security
- Thymeleaf
- MySQL
- Maven
- JUnit & Mockito
- Git

---

## 8. Application Execution
1. Configure database connection in `application.properties`
2. Run the application from the main Spring Boot class:
   ```java
   SpringApplication.run(BakeryOrdersApplication.class, args);
