# Frontend 
The frontend of the application is built with **React** and **JavaScript**, focusing on creating a user-friendly and responsive interface. I was responsible for developing the modules for **Supplier**, **Warehouse**, and **Warehouse Orders**, which included:

- Structuring components and UI flow. 
- State management and data flow between views. 
- Forms for creating and updating data. 
- Tables and lists for clear data presentation.

## Tech Stack 
- **React** with **React Router** for navigation 
- **JavaScript** 
- **CSS** for styling 
- **Keycloak** for authentication and secure access

## Run project, frontend only
Clone the repository and navigate to the frontend folder:

```bash 
cd inventhor-frontend
```

### Install dependencies
```bash
npm install
```

### Start React server
```bash
npm start
```

The application runs on [http://localhost:3000](http://localhost:3000)

## Screenshots 
### Sign In Module - This feature is in development. 
Screenshots of the Sign In module, demonstrating how users log in to the inventory application.

1. When you first start the application, you may encounter a **401 error**. This happens because the token runtime has expired. 
2. To fix this, click **Sign Out** and try signing in again. 
3. Sign in with Keycloak using one of the two employee accounts: 
    - **Option 1** – Username: annaandersen@inventhor.com, Password: hash1_Abc123XyZ789 – Manager (full admin rights) - Example: Anna Andersen can manage everything in the application, from administering employees to deleting all records. - **Option 2** – Username: bjornberg@inventhor.com, Password: hash2_QwErTyUiOp12 – Staff (limited admin rights) - Example: Bjorn Berg cannot delete orders or suppliers, and only has access to non-critical information.

For more information about usernames and passwords, see inventhor.sql, line **748**.

![Sign In Module Screenshot](images/signIn1.png) 
![Sign In Module Screenshot](images/SignIn2.png) 
![Sign In Module Screenshot](images/SignIn3.png)

### Inventory Module 
The Inventory module provides an overview of all products in the system along with the most relevant information.

#### Overview 
Displays all products with key information. The search and filter function allows you to find specific products. 
![Inventory Module Screenshot](images/inventhor1.png) 

#### Dark/Light Mode 
Users can easily switch between dark and light themes. 
![Inventory Module Screenshot](images/inventor2.png) 

#### Product Details 
Clicking on a product opens a detailed view with supplier information, warehouse stock, and sales history. 
![Inventory Module Screenshot](images/inventhor5.png) 

#### Add Product 
Form for adding a new product, where users can enter details and select from existing suppliers. 
![Inventory Module Screenshot](images/inventhor3.png) 
![Inventory Module Screenshot](images/inventhor4.png)

### Customer Orders Module 
The customer orders module provides an overview of all the customer orders. 

#### Overview 
Displays all customer orders with key information. Also possible to search and filter customer orders. 
![Customer Orders Module Screenshot](images/customer1.png) 

#### Customer Order 
Detail Clicking on an order opens a detailed view of a customer order with customer information, status and product details. 
![Customer Orders Module Screenshot](images/customer2.png) 

#### Add Order 
Form for adding a new customer order, where users can enter warehouse, products, customer information and payment details. 
![Customer Orders Module Screenshot](images/customer3.png)

### Warehouse Orders Module 
Displays the Warehouse Orders module, where users can create, edit, delete, and track the status of orders. 

#### Overview 
Overview of all warehouse orders with key information. 
![Warehouse Orders Screenshot](images/warehouseOrder1.png) 

#### Order Detail 
By selecting an order, users can access detailed information related to that specific order. 
![Warehouse Orders Screenshot](images/warehouseOrder2.png) 

#### Add New Order 
When clicking "Add Order", users are guided to a form that allows them to define the order details. This includes selecting the destination warehouse, choosing a supplier, linking products to that supplier, and specifying both delivery date and product information. 
![Warehouse Orders Screenshot](images/warehouseOrder3.png) 
![Warehouse Orders Screenshot](images/warehouseOrder6.png) 
![Warehouse Orders Screenshot](images/warehouseOrder7.png) 
![Warehouse Orders Screenshot](images/warehouseOrder8.png) 
![Warehouse Orders Screenshot](images/warehouseOrder9.png) 

#### Edit Order 
The "Edit" function allows users to modify order details, such as delivery status and delivery date. 
![Warehouse Orders Screenshot](images/warehouseOrder4.png) 

#### Delete Order 
The "Delete" function allows the removal of an order, but this action is restricted to users with Admin privileges. 
![Warehouse Orders Screenshot](images/warehouseOrder5.png)

### Report Module 
The reports module provides insights into the most popular products and includes charts that visualize sales performance and customer data. 
![Report Module Screenshot](images/report.png) 

### Warehouse Module 
The Warehouse module offers a clear overview of all warehouses, presenting essential details in a structured way. 
![Warehouse Module Screenshot](images/warehouse1.png) 

#### Specific Warehouse 
Selecting a warehouse provides detailed information and displays a list of all products associated with it. From there, users can navigate to the Inventory module by clicking on a specific product. 
![Warehouse Module Screenshot](images/warehouse2.png) 

#### Add Product To Location 
The "Add Product to Location" function allows users to assign a product to a specific rack number and location in the warehouse, specifying the quantity. 
![Warehouse Module Screenshot](images/warehouse3.png)

### Supplier Module 
The Supplier module allows users to manage supplier information efficiently. Users can add new suppliers, edit existing details, delete suppliers (admin only), and view comprehensive supplier information. 

#### Overview 
The first page displays a list of all suppliers along with key information. 
![Supplier Module Screenshot](images/supplier1.png) 

#### Add New Supplier 
The "Add" function allows users to add a new supplier. This action is restricted to Admin users. 
![Supplier Module Screenshot](images/supplier2.png) 

#### Edit Supplier 
The "Edit" feature enables users to update supplier details. All fields must be completed, and updating the address will automatically generate a new record in the address table. This action is restricted to Admin users. 
![Supplier Module Screenshot](images/supplier3.png) 

#### Delete Supplier 
Users with Admin privileges can delete suppliers. If a supplier has any associated products, deletion is not permitted. All linked products must be removed prior to deleting the supplier. 
![Supplier Module Screenshot](images/supplier4.png) 
![Supplier Module Screenshot](images/supplier5.png)

### Employee Module 
The Employee module provides an overview of all employees and enables users to manage employee data by adding, editing, or deleting records. 

#### Overview 
This page provides a list of all the employees with key information. 
![Employee Module Screenshot](images/employee1.png) 

#### Add New Employee 
The "Add" function allows the user to add new employees. This action is restricted to Admin users. 
![Employee Module Screenshot](images/employee2.png) 

#### Edit Employee 
The "Edit" function allows the user to change employee information. This action is restricted to Admin users. 
![Employee Module Screenshot](images/employee3.png) 

#### Delete Employee 
The "Delete" feature enables the removal of employee records, but only for inactive employees. Attempting to delete an active employee will result in an error message. 
![Employee Module Screenshot](images/employee4.png)

### Notification Module 
#### Overview 
The Notifications page provides an overview of all notifications. Items displayed in light grey signify unread messages. 
![Notification Module Screenshot](images/notification1.png) 

#### Specific Notification 
Clicking on a notification opens the message, and the unread indicator (light grey) is automatically removed. 
![Notification Module Screenshot](images/notification2.png) 

#### Drop Down 
The notification system is integrated into the top menu bar, displaying a bell icon along with the count of unread notifications. Users can click the bell to reveal a dropdown list of all unread messages. 
![Notification Module Screenshot](images/notification3.png) 

### Settings Module 
The Settings page enables users to update their personal information. Modifying a user’s position is only permitted for users with Admin privileges. ![Setting Module Screenshot](images/settings.png)

## Future Improvements 
Although the frontend is fully functional, there are areas planned for refinement and optimization: 
- Consistent styling across all modules with a unified design system.
- Improved error handling and form validation. 
- Optimization of state management for complex data flows.

## Summary 
The frontend of the Inventhor application delivers a responsive and user-friendly interface for managing suppliers, warehouses, products, orders, employees, and reports. My primary contributions were the **Supplier**, **Warehouse**, and **Warehouse Orders** modules, where I implemented the structure, UI, state management, and data handling. 

This project demonstrates my skills in **React**, **JavaScript**, **state management**, and **UI/UX design**, as well as my ability to build scalable and maintainable frontend solutions.

