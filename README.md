# Inventhor application 
<<<<<<< HEAD
Inventhor is a full-stack application developed for IMS Inventhor, managing products, warehouses, orders, suppliers, customers, users, notifications, and addresses. The backend is built in Java with Spring Boot and exposes a RESTful API with Swagger documentation. The frontend is developed in React with JavaScript, providing an interactive user experience. Keycloak is integrated for centralized authentication and authorization, enabling secure login and role-based access control across the application.

This project was developed in a team, with each member contributing to both frontend and backend functionality. In this portfolio, the focus is on my personal contributions.

## Architecture
The system follows a layered architecture with clear separation of concerns: 
- **Frontend:** React application that provides a responsive and user-friendly interface for employees and administrators. 
- **Backend:** Java + Spring Boot service that implements business logic and exposes REST APIs. 
- **Authentication & Authorization:** Keycloak handles secure login and role-based access control (Admin vs. Staff). 
- **Database:** PostgreSQL stores application data in a relational structure. 
- **API Documentation:** Swagger (OpenAPI) makes backend endpoints easy to explore and test.

## Tech Stack 
- **Frontend:** React, JavaScript, HTML, CSS 
- **Backend:** Java, Spring Boot, MapStruct, Lombok 
- **Database:** PostgreSQL 
- **Authentication:** Keycloak (role-based access control) 
- **Build & Tools:** Maven, npm 
- **Other:** Swagger (OpenAPI), Postman, Git, GitHub

## Version Control 
This project was developed collaboratively using **Git** for version control and **GitHub** for repository hosting and team collaboration. All code changes were tracked via commits, branches, and pull requests to ensure smooth teamwork and maintain a clean history.

## Prerequisites 
Before running the Inventhor application locally, make sure you have the following installed:

- **Node.js** (v18 or higher) – for the frontend 
- **npm** (v9 or higher) – for frontend package management 
- **Java JDK** (v17 or higher) – for the backend 
- **Maven** (v3.8 or higher) – for building the backend - **PostgreSQL** (v14 or higher) – for the database 
- **Keycloak** (v21 or higher) – for authentication and role-based access control

**Note:** Keycloak must be running and configured with the appropriate realms, roles, and test users to fully test authentication and authorization in the application. For more detailed instructions on setting up Keycloak and logging in, see the inventhor-frontend/README.md.

## Get started
=======

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

>>>>>>> origin/main
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

6. The backend will start on [http://localhost:8080](http://localhost:8080), and you can access Swagger documentation at [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html).


### Frontend
<<<<<<< HEAD
=======

>>>>>>> origin/main
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
<<<<<<< HEAD
=======

>>>>>>> origin/main
I contributed to all parts of the project:
- **Frontend:** Developed react components and views for managing warehouses, suppliers and products.
- **Backend:** Implemented RESTful API endpoints, service layer logic, and entity mappings in Spring Boot.
- **Database:** Designed and set up PostgreSQL schema, wrote queries, and handled data relationships.

## More Information
<<<<<<< HEAD
For more detailed instructions, see the README files in the `inventhor-frontend/` and `inventhor-backend/` folders. 

## Future Improvements 
While the application is functional, there are areas planned for further development and refinement: 
- Improved error handling and validation in forms.
- A more consistent and polished design across all modules for a unified user experience.
- Additional report types with advanced filtering and visualization options for better analytics.
- Extended role-based access control for more fine-grained security management.

These improvements are part of the roadmap and will help ensure the system becomes more robust, user-friendly, and scalable.

=======

For more detailed instructions, see the README files in the `inventhor-frontend/` and `inventhor-backend/` folders. 

>>>>>>> origin/main



