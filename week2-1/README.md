# User Management Microservice

A complete user management microservice built with Spring Boot and PostgreSQL, featuring JWT authentication, role-based access control, and comprehensive error handling.

## Features

- RESTful API with proper HTTP methods
- JWT-based authentication
- Role-based access control (RBAC)
- PostgreSQL database with connection pooling
- Input validation
- Comprehensive error handling
- Logging system
- Unit tests
- Docker support for local development

## Prerequisites

- Java 17 or higher
- Maven
- Docker and Docker Compose
- PostgreSQL (if running without Docker)

## Technology Stack

- Spring Boot 3.2.3
- Spring Security
- Spring Data JPA
- PostgreSQL
- JWT for authentication
- Lombok
- Maven
- Docker

## Project Structure

```
src/main/java/com/example/usermanagement/
├── UserManagementApplication.java
├── controller/
│   ├── AuthController.java
│   └── UserController.java
├── dto/
│   ├── JwtResponseDTO.java
│   ├── LoginRequestDTO.java
│   ├── UserDTO.java
│   └── UserRegistrationDTO.java
├── entity/
│   └── User.java
├── exception/
│   ├── GlobalExceptionHandler.java
│   └── ResourceNotFoundException.java
├── repository/
│   └── UserRepository.java
├── security/
│   ├── CustomUserDetailsService.java
│   ├── JwtAuthenticationFilter.java
│   ├── JwtTokenProvider.java
│   └── SecurityConfig.java
└── service/
    ├── AuthService.java
    ├── UserService.java
    └── impl/
        ├── AuthServiceImpl.java
        └── UserServiceImpl.java
```

## Setup and Running

### Using Docker (Recommended)

1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd user-management-service
   ```

2. Start the PostgreSQL database using Docker Compose:
   ```bash
   docker-compose up -d
   ```

3. Build and run the application:
   ```bash
   ./mvnw clean package
   java -jar target/user-management-service-1.0.0.jar
   ```

### Without Docker

1. Ensure PostgreSQL is installed and running
2. Create a database named `user_management`
3. Update `application.yml` with your database credentials
4. Build and run the application:
   ```bash
   ./mvnw clean package
   java -jar target/user-management-service-1.0.0.jar
   ```

## API Endpoints

### Authentication

- `POST /api/v1/auth/register` - Register a new user
- `POST /api/v1/auth/login` - Login and get JWT token

### User Management

- `GET /api/v1/users` - Get all users (Admin only)
- `GET /api/v1/users/{id}` - Get user by ID
- `GET /api/v1/users/email/{email}` - Get user by email
- `PUT /api/v1/users/{id}` - Update user
- `DELETE /api/v1/users/{id}` - Delete user (Admin only)
- `PATCH /api/v1/users/{id}/status` - Change user status (Admin only)

## Security

- JWT-based authentication
- Password encryption using BCrypt
- Role-based access control
- Secure password storage
- Protection against common security vulnerabilities

## Testing

Run the tests using Maven:
```bash
./mvnw test
```

## Environment Variables

- `JWT_SECRET` - Secret key for JWT token generation (default: provided in application.yml)
- `SPRING_DATASOURCE_URL` - Database URL
- `SPRING_DATASOURCE_USERNAME` - Database username
- `SPRING_DATASOURCE_PASSWORD` - Database password

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details. 



#Commands used 

Create a complete User Management microservice with:
Architecture:
- RESTful API with proper HTTP methods
- Database layer with connection pooling
- Service layer for business logic
- Controller layer for HTTP handling
- Authentication middleware
- Error handling middleware
- Input validation
- Logging system
Technology: java, springboot
Database: PostgreSQL
Include:
1. Project structure
2. Dependencies/package configuration
3. Database schema
4. All endpoint implementations
5. Authentication setup
6. Error handling
7. Basic unit tests
8. README with setup instructions

Deploy
  for local deployent and running application use docker (use PostgreSQL docker image for database)
  
  
  C2  -> 
  
  getting error in - mvn clean install - fix the issue
  
  