# APP2000-Group3-Backend

A Spring Boot application developed for IMS Inventhor, designed to handle products, warehouses, suppliers, customers, users, and addresses. It includes a RESTful API with Swagger OpenAPI documentation.

## Features (UNDER DEVELOPMENT!)

- Employee management and registration
- Customers management and registration

## Tech Stack

- Java
- Spring Boot
- PostgreSQL
- OpenAPI (Swagger) for documentation
- MapStruct for object mapping
- Lombok to minimize boilerplate code, enhancing readability and maintainability.

# Get started

1. Clone the repository:
```bash
git clone https://github.com/Mifagen/APP2000-Group3-Backend.git
cd APP2000-Group3-Backend/inventhor
```

2. Create PostgreSQL database:
```sql
CREATE DATABASE inventhor;
```

3. Configure database connection in `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/inventhor
spring.datasource.username=your_username
spring.datasource.password=your_password
```

4. Build the project:
```bash
mvn clean install
```

5. Run the application:
```bash
mvn spring-boot:run
```

The application will be available at `http://localhost:8080`

## API Documentation

Access the Swagger UI to explore and test the API:
- Swagger UI: `http://localhost:8080/swagger-ui.html`

## Project Structure

```
src/main/java/com/group3/inventhor
├── config/          # Configuration classes
├── controller/      # REST controllers
├── model/          # Entity classes
├── repository/     # Data access layer
├── service/        # Business logic
├── dto/            # Data Transfer Objects
├── mapper/         # Object mappers
└── exception/      # Exception handling
```

## Key Features Explained (UNDER DEVELOPMENT!)

### Employee management
- Create and update information about employee
- Managing accecibility for different users
- Getting all information about user

### Customer management
- Create and update information about customers




## API Examples

### Create a new address

```bash
POST /address
{
  "street": "Karl Johan 23",
  "postCode": 1995,
  "city": "Oslo",
  "country": "Norway"
}
```

### Create a new employee and asign address to him

```bash
{
  "email": "bruno@gmail.com",
  "password": "bruno123",
  "firstName": "Mike",
  "lastName": "Bruno",
  "role": "Administrator",
  "position": "It developer",
  "employedDate": "2020-11-22",
  "address": {
    "addressID": 23
  },
  "active": true
}
```