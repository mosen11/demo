# Architecture Documentation

This document provides a brief overview of the architecture and technology stack used in the Recipe Management application.

## Architectural Layers

The application follows a standard layered architecture to ensure separation of concerns and maintainability.

### 1. Controller Layer (`com.demo.recipe.controller`)
- **Responsibility**: Handles incoming HTTP requests and returns appropriate HTTP responses.
- **Key Component**: `RecipeController`.
- **Details**: It uses Spring MVC annotations (`@RestController`, `@GetMapping`, etc.) to define API endpoints. It validates input using `@Valid` and delegates business operations to the Service layer.

### 2. Service Layer (`com.demo.recipe.service`)
- **Responsibility**: Contains the core business logic of the application.
- **Key Component**: `RecipeService`.
- **Details**: This layer orchestrates data flow between the controllers and repositories. It handles complex business rules, such as advanced filtering for recipes using JPA Specifications, and manages transactions using `@Transactional`.

### 3. Repository Layer (`com.demo.recipe.repository`)
- **Responsibility**: Manages data persistence and retrieval from the database.
- **Key Component**: `RecipeRepository`.
- **Details**: It extends `JpaRepository` and `JpaSpecificationExecutor`, leveraging Spring Data JPA to provide standard CRUD operations and dynamic query execution without boilerplate code.

### 4. Model Layer (`com.demo.recipe.model`)
- **Responsibility**: Represents the data structures used throughout the application.
- **Key Component**: `Recipe`.
- **Details**: The `Recipe` class is a JPA entity mapped to the database. It uses Jakarta Validation annotations to ensure data integrity.

---

## Spring Boot Libraries & Dependencies

The project utilizes several key Spring Boot and third-party libraries:

- **Spring Boot Starter Web**: Used for building RESTful web services. It includes Spring MVC and an embedded Tomcat server.
- **Spring Boot Starter Data JPA**: Provides a seamless way to interact with relational databases using Hibernate (JPA implementation) and Spring Data.
- **Spring Boot Starter Validation**: Integrates Hibernate Validator to enforce constraints on data models (e.g., `@NotBlank`, `@Min`).
- **H2 Database**: An in-memory database used for development and testing. It allows for fast iterations without the need for an external database server.
- **Lombok**: A library that reduces boilerplate code (like getters, setters, and builders) using annotations, making the code cleaner and more readable.
- **SpringDoc OpenAPI (Swagger UI)**: Automatically generates API documentation based on the application's controllers and models, accessible via a web interface for easy API testing and exploration.
- **Spring Boot Starter Test**: A comprehensive testing starter that includes JUnit, Mockito, and AssertJ for unit and integration testing.

