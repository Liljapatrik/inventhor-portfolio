/**
 * Author: Group 3
 * Date: 27 April 2025
 * Description: This file contains functions to interact with the server API for managing employees, products, categories, orders, and other data.
 */

import axios from "axios";


// Employee data
//const API_URL_Employees = "http://10.0.2.2:8080/employees"; // For Android emulator
const API_URL_Employees = "http://localhost:8080/employees";


// Define the API URL for fetching employees
export const getEmployees = async () => {
    try {
        const response = await axios.get(API_URL_Employees,
            {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem("access_token")}`
                }
            }
        );
        return response.data;
    } catch (error) {
        console.error("Error fetching employees:", error);
        throw error;
    }
};

export const getEmployeeByEmail = async (email) => {
    try {
        const response = await axios.get(API_URL_Employees + `/email/${email}`,
            {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem("access_token")}`
                }
            }
        );
        return response.data;
    } catch (error) {
        console.error("Error fetching employees:", error);
        throw error;
    }
};

// get employee for settings
export const getEmployeeForSettings = async (email) => {
    try {
        const response = await axios.get(`${API_URL_Employees}/email-for-settings/${email}`,
            {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem("access_token")}`
                }
            }
        );
        console.log(response.data);
        return response.data;
    } catch (error) {
        console.error("Error fetching authenticated employee:", error);
        throw error;
    }
};


// Authenticated employee can update their own information.
export const updateAuthenticatedEmployee = async (employee, employeenr, email) => {
    try {
        const response = await axios.put(`${API_URL_Employees}/update/${employeenr}/${email}`, employee,
            {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem("access_token")}`
                }
            }
        );
        return response.data;
    } catch (error) {
        console.error("Error updating authenticated employee:", error);
        throw error;
    }
};

// Define the API URL for fetching a specific employee by nr
export const getEmployeeByEmployeenr = async (employeenr) => {
    try {
        const response = await axios.get(`${API_URL_Employees}/${employeenr}`,
            {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem("access_token")}`
                }
            }
        );
        console.log(response.data);
        return response.data;
    } catch (error) {
        console.error("Error fetching employee:", error);
        throw error;
    }
};

// Define the API URL for adding a new employee
export const addEmployee = async (employee) => {
    try {
        const response = await axios.post(API_URL_Employees, employee,
            {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem("access_token")}`
                }
            }
        );
        return response.data;
    } catch (error) {
        console.error("Error adding employee:", error);
        throw error;
    }
};


// Define the API URL for updating an existing employee
export const updateEmployee = async (id, employee) => {
    try {
        const response = await axios.put(`${API_URL_Employees}/${id}`, employee,
            {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem("access_token")}`
                }
            }
        );
        return response.data;
    } catch (error) {
        console.error("Error updating employee:", error);
        throw error;
    }
};

// Define the API URL for deleting an employee
export const deleteEmployee = async (id) => {
    try {
        await axios.delete(`${API_URL_Employees}/${id}`,
            {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem("access_token")}`
                }
            }
        );
    } catch (error) {
        console.error("Error deleting employee:", error);
        throw error;
    }
};

// ----------------------------------------------------------------------------------------
// Employees roles
const API_URL_EmployeeRoles = "http://localhost:8080/employee-roles";

// Get all employee roles
export const getEmployeeRoles = async () => {
    try {
        const response = await axios.get(API_URL_EmployeeRoles,
            {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem("access_token")}`
                }
            }
        );
        return response.data;
    } catch (error) {
        console.error("Error fetching employee roles:", error);
        throw error;
    }
}

// ----------------------------------------------------------------------------------------
// Address data
const API_URL_Address = "http://localhost:8080/address";

// Define the API URL for fetching address 
export const getAddressById = async (id) => {
    try {
        const response = await axios.get(`${API_URL_Address}/${id}`,
            {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem("access_token")}`
                }
            }
        );
        console.log(response.data);
        return response.data;
    } catch (error) {
        console.error("Error fetching address:", error);
        throw error;
    }
};

// Define the API URL for updating address
export const updateAddress = async (id, address) => {
    try {
        console.log("Updating address:", address);
        console.log("ID:", id);
        const response = await axios.put(`${API_URL_Address}/${id}`, address,
            {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem("access_token")}`
                }
            }
        );
        return response.data;
    } catch (error) {
        console.error("Error updating address:", error);
        throw error;
    }
};

// Define the API URL for adding a new address
export const addAddress = async (address) => {
    try {
        const response = await axios.post(API_URL_Address, address,
            {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem("access_token")}`
                }
            }
        );
        return response.data;
    } catch (error) {
        console.error("Error adding address:", error);
        throw error;
    }
};

// ------------------------------------------------------------------------------------
// Category data
const API_URL_Categories = "http://localhost:8080/category";

// Get all categories
export const getCategories = async () => {
    try {
        const response = await axios.get(API_URL_Categories,
            {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem("access_token")}`
                }
            }
        );
        return response.data;
    } catch (error) {
        console.error("Error fetching categories:", error);
        throw error;
    }
};

// Get category by id
export const getCategoryById = async (id) => {
    try {
        const response = await axios.get(`${API_URL_Categories}/${id}`,
            {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem("access_token")}`
                }
            }
        );
        return response.data;
    } catch (error) {
        console.error("Error fetching category:", error);
        throw error;
    }
};

// Add a new category
export const addCategory = async (category) => {
    try {
        const response = await axios.post(API_URL_Categories, category,
            {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem("access_token")}`
                }
            }
        );
        return response.data;
    } catch (error) {
        console.error("Error adding category:", error);
        throw error;
    }
};

// Update an existing category
export const updateCategory = async (id, category) => {
    try {
        const response = await axios.put(`${API_URL_Categories}/${id}`, category,
            {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem("access_token")}`
                }
            }
        );
        return response.data;
    } catch (error) {
        console.error("Error updating category:", error);
        throw error;
    }
};

// Delete a category
export const deleteCategory = async (id) => {
    try {
        await axios.delete(`${API_URL_Categories}/${id}`,
            {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem("access_token")}`
                }
            }
        );
    } catch (error) {
        console.error("Error deleting category:", error);
        throw error;
    }
};


//-------------------------------------------------------------------------------------
// Product data
const API_URL_Products = "http://localhost:8080/products";

// Get all products
export const getProducts = async () => {
    try {
        const response = await axios.get(API_URL_Products,
            {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem("access_token")}`
                }
            }
        );
        return response.data;
    } catch (error) {
        console.error("Error fetching products:", error);
        throw error;
    }
};

// Get product by nr
export const getProductByNr = async (productnr) => {
    try {
        const response = await axios.get(`${API_URL_Products}/${productnr}`,
            {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem("access_token")}`
                }
            }
        );
        return response.data;
    } catch (error) {
        console.error("Error fetching product:", error);
        throw error;
    }
};

// Add a new product
export const addProduct = async (product) => {
    try {
        const response = await axios.post(API_URL_Products, product,
            {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem("access_token")}`
                }
            }
        );
        return response.data;
    } catch (error) {
        console.error("Error adding product:", error);
        throw error;
    }
};

// Update an existing product
export const updateProduct = async (productnr, product) => {
    try {
        const response = await axios.put(`${API_URL_Products}/${productnr}`, product,
            {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem("access_token")}`
                }
            });
        return response.data;
    } catch (error) {
        console.error("Error updating product:", error);
        throw error;
    }
};

// Delete a product
export const deleteProduct = async (productnr) => {
    try {
        await axios.delete(`${API_URL_Products}/${productnr}`,
            {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem("access_token")}`
                }
            });
    } catch (error) {
        console.error("Error deleting product:", error);
        throw error;
    }
};


// -------------------------------------------------------------------------------------
// Get all notifications
const API_URL_Notifications = "http://localhost:8080/notifications/{employeenr}";
export const getNotificationsForEmployee = async (employeenr) => {
    console.log("Fetching notifications for employee:", employeenr);
    try {
        const response = await axios.get(API_URL_Notifications.replace("{employeenr}", employeenr),
            {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem("access_token")}`
                }
            });
        return response.data;
    } catch (error) {
        console.error("Error fetching notifications:", error);
        throw error;
    }
};

const API_URL_Notification = "http://localhost:8080/notifications";
// Update a notification
export const updateNotification = async (notificationnr, notification) => {
    console.log("Updating notification:", notificationnr, notification);
    try {
        const response = await axios.put(`${API_URL_Notification}/${notificationnr}/read`, notification,
            {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem("access_token")}`
                }
            }
        );
        return response.data;
    } catch (error) {
        console.error("Error updating notification:", error);
        throw error;
    }
};

// --------------------------------------------------------------------------------------
// Get all suppliers
const API_URL_Suppliers = "http://localhost:8080/suppliers";
export const getSuppliers = async () => {
    try {
        const response = await axios.get(API_URL_Suppliers,
            {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem("access_token")}`
                }
            }
        );
        return response.data;
    } catch (error) {
        console.error("Error fetching suppliers:", error);
        throw error;
    }
};

// Add product to supplier
const API_URL_SupplierProducts = "http://localhost:8080/product-suppliers";

export const addProductToSupplier = async (productSupplier) => {
    try {
        const response = await axios.post(API_URL_SupplierProducts, productSupplier,
            {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem("access_token")}`
                }
            }
        );
        return response.data;
    } catch (error) {
        console.error("Error adding product to supplier:", error);
        throw error;
    }
}

// Get suppliers by productnr
export const getSuppliersByProductnr = async (productnr) => {
    try {
        const response = await axios.get(`${API_URL_SupplierProducts}/suppliers-by-product/${productnr}`,
            {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem("access_token")}`
                }
            }
        );
        return response.data;
    } catch (error) {
        console.error("Error fetching suppliers by productnr:", error);
        throw error;
    }
};

// -------------------------------------------------------------------------------------------
// Get all customer orders
const API_URL_CustomerOrders = "http://localhost:8080/customer-orders";
export const getCustomerOrders = async () => {
    try {
        const response = await axios.get(API_URL_CustomerOrders,
            {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem("access_token")}`
                }
            }
        );
        return response.data;
    } catch (error) {
        console.error("Error fetching customer orders:", error);
        throw error;
    }
};

// Add a new customer order
export const addCustomerOrder = async (customerOrder) => {
    try {
        const response = await axios.post(API_URL_CustomerOrders, customerOrder,
            {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem("access_token")}`
                }
            }
        );
        return response.data;
    } catch (error) {
        console.error("Error adding customer order:", error);
        throw error;
    }
};

// Update an existing customer order
export const updateCustomerOrder = async (ordernr, customerOrder) => {
    try {
        const response = await axios.put(`${API_URL_CustomerOrders}/${ordernr}`, customerOrder,
            {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem("access_token")}`
                }
            }
        );
        return response.data;
    } catch (error) {
        console.error("Error updating customer order:", error);
        throw error;
    }
};

// Delete a customer order
export const deleteCustomerOrder = async (ordernr) => {
    try {
        await axios.delete(`${API_URL_CustomerOrders}/${ordernr}`,
            {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem("access_token")}`
                }
            }
        );
    } catch (error) {
        console.error("Error deleting customer order:", error);
        throw error;
    }
};

// Get customer order by ordernr
export const getCustomerOrderByOrdernr = async (ordernr) => {
    try {
        const response = await axios.get(`${API_URL_CustomerOrders}/${ordernr}`,
            {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem("access_token")}`
                }
            }
        );
        return response.data;
    } catch (error) {
        console.error("Error fetching customer order:", error);
        throw error;
    }
};

// -------------------------------------------------------------------------------------------
// Gett all payments methods
const API_URL_PaymentMethods = "http://localhost:8080/payment-methods";
export const getPaymentMethods = async () => {
    try {
        const response = await axios.get(API_URL_PaymentMethods,
            {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem("access_token")}`
                }
            }
        );
        return response.data;
    } catch (error) {
        console.error("Error fetching payment methods:", error);
        throw error;
    }
};

// -------------------------------------------------------------------------------------------
// Get customerpayment by ordernr
const API_URL_CustomerPayment = "http://localhost:8080/payments";
export const getCustomerPaymentByOrdernr = async (ordernr) => {
    try {
        const response = await axios.get(`${API_URL_CustomerPayment}/order/${ordernr}`,
            {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem("access_token")}`
                }
            }
        );
        return response.data;
    } catch (error) {
        console.error("Error fetching customer payment:", error);
        throw error;
    }
};

// Get all customer payments
export const getCustomerPayments = async () => {
    try {
        const response = await axios.get(API_URL_CustomerPayment,
            {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem("access_token")}`
                }
            }
        );
        return response.data;
    } catch (error) {
        console.error("Error fetching customer payments:", error);
        throw error;
    }
};

// Update a customer payment
export const updateCustomerPayment = async (ordernr, payment) => {
    try {
        const response = await axios.put(`${API_URL_CustomerPayment}/${ordernr}`, payment,
            {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem("access_token")}`
                }
            }
        );
        return response.data;
    } catch (error) {
        console.error("Error updating customer payment:", error);
        throw error;
    }
}

// -------------------------------------------------------------------------------------------
//Get order statuses
const API_URL_OrderStatuses = "http://localhost:8080/order-status";
export const getOrderStatuses = async () => {
    try {
        const response = await axios.get(API_URL_OrderStatuses,
            {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem("access_token")}`
                }
            }
        );
        return response.data;
    } catch (error) {
        console.error("Error fetching order statuses:", error);
        throw error;
    }
};


// -------------------------------------------------------------------------------------------
// Get products for customer order
const API_URL_CustomerOrderProducts = "http://localhost:8080/customer-order-products/order";
export const getCustomerOrderProducts = async (ordernr) => {
    try {
        const response = await axios.get(`${API_URL_CustomerOrderProducts}/${ordernr}`,
            {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem("access_token")}`
                }
            }
        );
        return response.data;
    } catch (error) {
        console.error("Error fetching customer order products:", error);
        throw error;
    }
};

// -------------------------------------------------------------------------------------------
// Get all warehouses
const API_URL_Warehouses = "http://localhost:8080/warehouses";
export const getWarehouses = async () => {
    try {
        const response = await axios.get(API_URL_Warehouses,
            {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem("access_token")}`
                }
            }
        );
        return response.data;
    } catch (error) {
        console.error("Error fetching warehouses:", error);
        throw error;
    }
};


// -------------------------------------------------------------------------------------------
// get all locationproduct
const API_URL_LocationProducts = "http://localhost:8080/location-product";
export const getLocationProducts = async () => {
    try {
        const response = await axios.get(API_URL_LocationProducts,
            {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem("access_token")}`
                }
            }
        );
        return response.data;
    } catch (error) {
        console.error("Error fetching location products:", error);
        throw error;
    }
};

// get location products by warehouse number
export const getLocationProductsByWarehouse = async (warehousenr) => {
    try {
        const response = await axios.get(`${API_URL_LocationProducts}/warehouse/${warehousenr}`,
            {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem("access_token")}`
                }
            }
        );
        return response.data;
    } catch (error) {
        console.error("Error fetching location products by warehouse:", error);
        throw error;
    }
}

// Get location products by product number
export const getLocationProductsByProductnr = async (productnr) => {
    try {
        const response = await axios.get(`${API_URL_LocationProducts}/product/${productnr}`,
            {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem("access_token")}`
                }
            }
        );
        return response.data;
    } catch (error) {
        console.error("Error fetching location products by product number:", error);
        throw error;
    }
};

// -------------------------------------------------------------------------------------------
// Location Product operations for warehouse management

// Get all locations
export const getLocations = async () => {
    try {
        const response = await axios.get("http://localhost:8080/locations",
            {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem("access_token")}`
                }
            }
        );
        return response.data;
    } catch (error) {
        console.error("Error fetching locations:", error);
        throw error;
    }
};

// Add a product to a location
export const addProductToLocation = async (locationProduct) => {
    try {
        const response = await axios.post(API_URL_LocationProducts, locationProduct,
            {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem("access_token")}`
                }
            }
        );
        return response.data;
    } catch (error) {
        console.error("Error adding product to location:", error);
        throw error;
    }
};

// Update a location product
export const updateLocationProduct = async (locationProduct) => {
    try {
        const { warehousenr, racknr, placenr, productnr, quantity } = locationProduct;
        const response = await axios.put(
            `${API_URL_LocationProducts}/warehouse/${warehousenr}/rack/${racknr}/place/${placenr}/product/${productnr}`,
            { quantity },
            {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem("access_token")}`
                }
            }
        );
        return response.data;
    } catch (error) {
        console.error("Error updating location product:", error);
        throw error;
    }
};

// Delete a location product
export const deleteLocationProduct = async (locationProduct) => {
    try {
        const { warehousenr, racknr, placenr, productnr } = locationProduct;
        await axios.delete(
            `${API_URL_LocationProducts}/warehouse/${warehousenr}/rack/${racknr}/place/${placenr}/product/${productnr}`,
            {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem("access_token")}`
                }
            }
        );
    } catch (error) {
        console.error("Error deleting location product:", error);
        throw error;
    }
};
// ---------------------------------------------------------------------------------------------
// Get all customers
const API_URL_Customers = "http://localhost:8080/customers";
export const getCustomers = async () => {
    try {
        const response = await axios.get(API_URL_Customers,
            {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem("access_token")}`
                }
            }
        );
        return response.data;
    } catch (error) {
        console.error("Error fetching customers:", error);
        throw error;
    }
};

// add a new customer
export const addCustomer = async (customer) => {
    try {
        const response = await axios.post(API_URL_Customers, customer,
            {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem("access_token")}`
                }
            }
        );
        return response.data;
    } catch (error) {
        console.error("Error adding customer:", error);
        throw error;
    }
};

// Get customer by email
export const getCustomerByEmail = async (email) => {
    try {
        const response = await axios.get(`${API_URL_Customers}/email/${email}`,
            {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem("access_token")}`
                }
            }
        );
        return response.data;
    } catch (error) {
        console.error("Error fetching customer by email:", error);
        throw error;
    }
};

// -------------------------------------------------------------------------------------------

// Report data
const API_URL_Reports = "http://localhost:8080/reports";

// Get complete report data
export const getReportData = async () => {
    try {
        const response = await axios.get(API_URL_Reports,
            {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem("access_token")}`
                }
            }
        );
        return response.data;
    } catch (error) {
        console.error("Error fetching report data:", error);
        throw error;
    }
};

// Get monthly sales data
export const getMonthlySalesData = async (year) => {
    try {
        const url = year
            ? `${API_URL_Reports}/sales/monthly?year=${year}`
            : `${API_URL_Reports}/sales/monthly`;
        const response = await axios.get(url,
            {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem("access_token")}`
                }
            }
        );
        return response.data;
    } catch (error) {
        console.error("Error fetching monthly sales data:", error);
        throw error;
    }
};

// Get monthly customer data
export const getMonthlyCustomerData = async (year) => {
    try {
        const url = year
            ? `${API_URL_Reports}/customers/monthly?year=${year}`
            : `${API_URL_Reports}/customers/monthly`;
        const response = await axios.get(url,
            {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem("access_token")}`
                }
            }
        );
        return response.data;
    } catch (error) {
        console.error("Error fetching monthly customer data:", error);
        throw error;
    }
};// Price history data
const API_URL_PriceHistory = "http://localhost:8080/pricehistory";

// Get price history by product number
export const getPriceHistoryByProductnr = async (productnr) => {
    try {
        const response = await axios.get(`${API_URL_PriceHistory}/yearly/${productnr}`,
            {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem("access_token")}`
                }
            }
        );
        return response.data;
    } catch (error) {
        console.error("Error fetching price history by product number:", error);
        throw error;
    }
};

// --------------------------------------------------------------------------------------------
// Selling history data
const API_URL_SellingTrend = "http://localhost:8080/sellinghistory";

// Get selling history for specific product
export const getSellingHistoryByProduct = async (productnr) => {
    try {
        const response = await axios.get(`${API_URL_SellingTrend}/yearly/${productnr}`,
            {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem("access_token")}`
                }
            }
        );
        return response.data;
    } catch (error) {
        console.error("Error fetching selling trend:", error);
        throw error;
    }
};