
# Devices API

This is a simple Spring Boot API for managing devices.

## Architecture

The project follows a classic layered architecture pattern for a monolithic Spring Boot application that connects to a PostgreSQL database.

The main packages are:

-   `controller`: This layer is responsible for exposing the API endpoints. It receives HTTP requests, validates them, and calls the appropriate service methods.
-   `service`: This layer contains the core business logic of the application. It orchestrates calls to the repository layer and implements the main functionalities.
-   `repository`: This is the data access layer, which uses Spring Data JPA to interact with the PostgreSQL database.
-   `entity`: Contains the JPA entities that map to the database tables.
-   `dto`: Data Transfer Objects are used to define the shape of the data for the API requests and responses. This helps to decouple the API from the internal database structure.
-   `mapper`: Contains interfaces (likely using MapStruct) to map data between `entity` objects and `dto` objects.
-   `config`: Holds application configuration classes.
-   `handler`: Provides centralized exception handling for the application.
-  `validator`: Implements custom validation logic.

## DTO Strategy

The `dto` package is structured to provide clear and maintainable data contracts for the API. Instead of a single DTO, we have three distinct objects:

*   **`DeviceDto`**: This is the general-purpose DTO used for API responses (e.g., in `GET` requests). It represents the full state of a device as it should be exposed to the client.
*   **`CreateDeviceDto`**: This DTO is used specifically for the `POST` endpoint to create a new device. It contains only the fields that a client is allowed to provide when creating a new resource. This allows for specific validation rules that should only apply during creation.
*   **`UpdateDeviceDto`**: Used for the `PUT` endpoint, this DTO defines the fields that can be modified on an existing device. This is crucial for applying different validation logic, such as preventing certain fields from being updated if a device is in use, which is handled by our custom validators.

This separation gives us fine-grained control over validation logic for each specific use case, making the API more robust and secure.

## Custom Validators

The application includes custom validators to enforce specific business rules:

-   `@DeviceExists`: Validates if a device with the given ID actually exists in the database.
-   `@NotDeleteIfInUse`: Prevents a device from being deleted if it is currently marked as 'in use'.
-   `@NameAndBrandNotUpdatableIfInUse`: Prevents the `name` and `brand` of a device from being updated if the device is currently 'in use'.

## How to run

To run the application, you will need to have Java and Maven installed. You will also need to have a PostgreSQL database running.

1.  Clone the repository
2.  Create a database named `devices`
3.  Run the application with the following command:

```bash
mvn spring-boot:run
```

## How to run with docker-compose

To run the application with docker-compose, you will need to have Docker and Docker Compose installed.

1.  Clone the repository
2.  Run the following command:

```bash
docker-compose up
```

## Swagger

Once the application is running, you can access the Swagger UI at:

[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## Future Improvements and Considerations

This implementation provides a solid foundation, but several areas could be improved for a production-ready application:

*   **Authentication & Authorization**: The API is currently unsecured. Future versions should implement a security layer (e.g., using Spring Security with JWT or OAuth2) to protect the endpoints.
*   **Testing**: The project lacks a dedicated test suite. It is crucial to add unit tests for the service layer and integration tests for the controllers and repository to ensure code quality and prevent regressions.
*   **CI/CD Pipeline**: To automate the build, testing, and deployment processes, a CI/CD pipeline (e.g., using GitHub Actions or Jenkins) should be implemented.
*   **Configuration Management**: Database credentials and other sensitive information should be externalized from the `application.yml` file and managed securely through environment variables or a configuration server (like Spring Cloud Config), especially for production environments.
*   **Logging and Monitoring**: While Spring Boot provides default logging, a more robust solution with structured logging (e.g., JSON format) and integration with a monitoring stack (like Prometheus and Grafana) would be beneficial for observing the application's health and performance in real-time.
*   **Business Logic for 'in use'**: The concept of a device being "in use" is currently a simple boolean flag. This could be expanded to include more complex logic, such as tracking which user has the device, checkout/check-in dates, and usage history.
