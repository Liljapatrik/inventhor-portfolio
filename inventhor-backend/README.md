# Inventhor Backend

The backend of **Inventhor** is built with **Java + Spring Boot** and provides a RESTful API for managing products, orders, warehouses, customers, suppliers, users, and addresses.  
API documentation is available via **Swagger (OpenAPI)**.

## Tech Stack

- **Backend:** Java, Spring Boot, MapStruct, Lombok  
- **Database:** PostgreSQL  
- **API Documentation:** Swagger (OpenAPI)  
- **Build & Tools:** Maven  

# Get started (Backend only)

1. Create PostgreSQL database:
```sql
CREATE DATABASE inventhor;
```

2. Configure database connection in `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/inventhor
spring.datasource.username=your_username
spring.datasource.password=your_password
```

3. Build the project:
```bash
mvn clean install
```

4. Run the application:
```bash
mvn spring-boot:run
```

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

## API Documentation

Once the application is running, you can explore the API via Swagger UI:

- Swagger UI: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)  
- OpenAPI spec (JSON): [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)  

## API Features

### Employee Management
- Create, update and delete employee records
- Assign roles and manage user access rights 
- Retrieve detailed information about employees
- Link employees to address

### Customer Management
- Create, update and delete customer records
- Retrieve detailed information about customers
- Associate customers with orders and addresses

### Product Management
- Create product records
- Retrieve product details and list all products
- Manage stock levels and availability
- Link products to suppliers and warehouses

### Order Management for Customers and Warehouses
- Create, update and delete orders
- Retrieve order details and order history
- Associate orders with customers and products
- Manage order status (e.g. picked, shipped, cancelled)

### Warehouse Management
- Create, update and delete warehouse records
- Retrieve warehouse details and list all warehouse
- Manage product inventory within warehouse
- Link warehouse to addresses

### Supplier Management
- Create, update and delete supplier records
- Retrieve supplier details and list all supplier
- Associate suppliers with address and products

### Address Management 
- Create, update and delete addresses
- Retrieve address details
- Associate addresses with employees, customers, warehouses and suppliers

### Notification Management
- Create, update and delete notifications
- Mark notifications as read or unread
- Retrieve notifications for a specific user
- Manage different notification types (e.g. warnings, updates and reminders)

### User Management
- Register and authenticate users
- Manage roles and permissions
- Retrieve user profile information
- Handle account activation and deactivation 



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