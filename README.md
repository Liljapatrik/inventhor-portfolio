# Inventhor application 

Inventhor is a full-stack application developed for IMS Inventhor, managing products, warehouses, orders, suppliers, customers, users, and addresses.  
The backend is built in Java with Spring Boot and exposes a RESTful API with Swagger documentation.  
The frontend is developed in React with JavaScript, providing an interactive user experience.

> This project was developed in a team, with each member contributing to both frontend and backend functionality. In this portfolio, the focus is on my personal contributions.

## Architecture

- **Frontend:** React (JavaScript)
- **Backend:** Java + Spring Boot
- **Database:** PostgreSQL
- **API Documentation:** Swagger (OpenAPI)

## Tech Stack

- **Frontend:** React, JavaScript, HTML, CSS
- **Backend:** Java, Spring Boot, MapStruct, Lombok
- **Database:** PostgreSQL
- **Build & Tools:** Maven, npm
- **Other:** Swagger (OpenAPI), Postman, Git, GitHub

## Get started

Follow these steps to run the application locally:

1. Clone the repository:
```bash
git clone https://github.com/Liljapatrik/inventhor-portfolio.git

```

### Backend
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
3. Navigate to the backend folder:
```bash
  cd inventhor-backend
```
4. Build the project:
```bash
  mvn clean install
```

5. Run the Spring Boot application:
```bash
mvn spring-boot:run
```

6. The backend will start on [http://localhost:8080](http://localhost:8080), and you can access Swagger documentation at [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html).


### Frontend

1. Navigate to the frontend folder:
```bash
  cd inventhor-frontend
```

2. Install dependencies:
```bash
npm install
```

3. Start the React application:
```bash
npm start
```

4. The frontend will start on [http://localhost:3000](http://localhost:3000).

## Contributions

I contributed to all parts of the project:
- **Frontend:** Developed react components and views for managing warehouses and suppliers.
- **Backend:** Implemented RESTful API endpoints, service layer logic, and entity mappings in Spring Boot.
- **Database:** Designed and set up PostgreSQL schema, wrote queries, and handled data relationships.

## More Information

For more detailed instructions, see the README files in the `inventhor-frontend/` and `inventhor-backend/` folders. 




