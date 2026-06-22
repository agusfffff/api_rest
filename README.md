# Library Management REST API

A Spring Boot REST API for managing a library system with books, users, and loans.

## Features

- **Book Management**: CRUD operations for books with search functionality
- **User Management**: CRUD operations for library users
- **Loan Management**: Create loans, return books, track overdue loans
- **Pagination**: Support for paginated responses
- **Validation**: Input validation with Bean Validation
- **Error Handling**: Global exception handling with structured error responses
- **Documentation**: Swagger UI for API documentation
- **Database**: H2 in-memory database for development

## Technologies

- Spring Boot 3.2.0
- Spring Data JPA
- H2 Database
- Spring Validation
- SpringDoc OpenAPI (Swagger)
- Maven

## API Endpoints

### Books
- `GET /api/books` - List all books (paginated)
- `GET /api/books/{id}` - Get book by ID
- `GET /api/books/search?title={title}` - Search books by title
- `POST /api/books` - Create a new book
- `PUT /api/books/{id}` - Update a book
- `DELETE /api/books/{id}` - Delete a book

### Users
- `GET /api/users` - List all users (paginated)
- `GET /api/users/{id}` - Get user by ID
- `POST /api/users` - Create a new user
- `PUT /api/users/{id}` - Update a user
- `DELETE /api/users/{id}` - Delete a user

### Loans
- `GET /api/loans` - List all loans (paginated)
- `POST /api/loans` - Create a new loan
- `PUT /api/loans/{id}/return` - Return a book
- `GET /api/loans/overdue` - Get overdue loans
- `GET /api/loans/users/{userId}` - Get active loans for a user

## Running the Application

1. Ensure you have Java 17+ and Maven installed
2. Clone the repository
3. Run `mvn spring-boot:run`
4. Access the API at `http://localhost:8080`
5. Swagger UI at `http://localhost:8080/swagger-ui.html`
6. H2 Console at `http://localhost:8080/h2-console`

## Database Configuration

The application uses H2 in-memory database by default. To change to MySQL or other databases, update `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/library
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
```

## Testing the API

You can test the endpoints using tools like Postman, curl, or directly from the Swagger UI.

Example: Create a book
## Testing

The project includes comprehensive unit and integration tests.

### Running Tests

```bash
mvn test
```

### Test Coverage

- **Unit Tests**: Service layer tests with Mockito
  - `BookServiceTest` - Tests book CRUD operations
  - `UserServiceTest` - Tests user CRUD operations  
  - `LoanServiceTest` - Tests loan management logic

- **Controller Tests**: Web layer tests with MockMvc
  - `BookControllerTest` - Tests book REST endpoints
  - `UserControllerTest` - Tests user REST endpoints
  - `LoanControllerTest` - Tests loan REST endpoints

- **Integration Tests**: Full application tests
  - `LibraryApiIntegrationTest` - Tests end-to-end scenarios with real database

### Test Examples

Run specific test class:
```bash
mvn test -Dtest=BookServiceTest
```

Run with coverage using a configured coverage plugin if needed.