/**
 * Auhor: Group 3
 * Date: 1 February 2025
 * Description: Main component for the application.
 * It contains the main routes and components for the application,
 * including inventory, suppliers, employees, orders, and more.
 * It uses React Router for navigation and includes various forms and details pages.
 * The component receives various props for fetching data and managing state.
 * It checks if the user is authorised before rendering the main content.
 */


import { BrowserRouter, Routes, Route } from "react-router-dom";
import './main.css';

// Importing components for the main application
import ProductDetails from './ProductDetails';
import Report from './Report';
import Inventory from './Inventory';
import Suppliers from './Suppliers';
import AddSupplierForm from './AddSupplierForm';
import Notifications from './Notifications';


import CustomerOrders from './CustomerOrders';
import WarehouseOrders from './WarehouseOrders';
import AddWarehouseOrderForm from './AddWarehouseOrderForm';
import WarehouseOrderProduct from './WarehouseOrdersProduct';
import EditWarehouseOrderForm from './EditWarehouseOrderForm';

import Employees from './Employees';
import AddEmployeeForm from './AddEmployeeForm';
import Settings from './Settings';
import EditSupplierForm from './EditSupplierForm';
import EditInventoryForm from './EditInventoryForm';
import AddProductForm from './AddProductForm';
import SupplierInfo from './SupplierInfo';
import WarehouseInfo from './WarehouseInfo';
import Support from './Support';
import WarehousesList from './WarehousesList';
import EmployeeDetails from './EmployeeDetails';

import CustomerOrderDetails from './CustomerOrderDetails';
import AddCustomerOrder from './AddCustomerOrder';



function Main({ authorisedUser,
  getSuppliers,
  getSuppliersByProductnr,
  suppliers,
  addSupplier,
  deleteSupplier,
  updateSupplier,
  getWarehouses,
  getWarehouseInfo,
  getEmployees,
  updateEmployee,
  updateAuthenticatedEmployee,
  deleteEmployee,
  getEmployeeByEmployeenr,
  getEmployeeByEmail,
  getEmployeeForSettings,
  addEmployee,
  getEmployeeRoles,
  getNotificationsForEmployee,
  updateNotification,
  updateAddress,
  addAddress,
  getCategories,
  getCategoryById,
  addCategory,
  updateCategory,
  deleteCategory,
  getProducts,
  getProductByNr,
  addProduct,
  updateProduct,
  deleteProduct,
  getCustomerOrderByOrdernr,
  getCustomerOrders,
  addCustomerOrder,
  deleteCustomerOrder,
  updateCustomerOrder,
  getCustomers,
  addCustomer,
  getCustomerByEmail,
  getPaymentMethods,
  getCustomerPaymentByOrdernr,
  getCustomerPayments,
  updateCustomerPayment,
  getOrderStatuses,
  getCustomerOrderProducts,
  getLocationProducts,
  getLocationProductsByWarehouse,
  getOrders,
  getLocationProductsByProductnr,
  addWarehouseOrder,
  getPriceHistoryByProductnr,
  getSellingHistoryByProduct
}) {

  // Check if the user is authorised
  if (Object.keys(authorisedUser).length === 0) {
    return null;
  }
  return (
    <div className="main-content">
      <Routes>
        <Route path="/inventory" element={<Inventory getProducts={getProducts} updateProduct={updateProduct} deleteProduct={deleteProduct} authorisedUser={authorisedUser} getLocationProducts={getLocationProducts} />} />
        <Route path="/" element={<Inventory getProducts={getProducts} updateProduct={updateProduct} getSuppliers={getSuppliers} deleteProduct={deleteProduct} getCategories={getCategories} />} />
        <Route path="/inventory/product-details/:id" element={<ProductDetails getSellingHistoryByProduct={getSellingHistoryByProduct} getPriceHistoryByProductnr={getPriceHistoryByProductnr} getLocationProductsByProductnr={getLocationProductsByProductnr} getSuppliersByProductnr={getSuppliersByProductnr} getProductByNr={getProductByNr} />} />
        <Route path='/inventory/edit/:id' element={<EditInventoryForm getProducts={getProducts} updateProduct={updateProduct} />} />
        <Route path='/inventory/add-product' element={<AddProductForm authorisedUser={authorisedUser} addProduct={addProduct} updateProduct={updateProduct} getCategories={getCategories} addCategory={addCategory} getSuppliers={getSuppliers} />} />

        <Route path='/warehouses' element={<WarehousesList />} />
        <Route path='/warehouses/warehouse-info/:id' element={<WarehouseInfo getWarehouseInfo={getWarehouseInfo} />} />

        <Route path="/report" element={<Report />} />

        <Route path='/suppliers' element={<Suppliers getSuppliers={getSuppliers} suppliers={suppliers} deleteSupplier={deleteSupplier} authorisedUser={authorisedUser} />} />
        <Route path='/suppliers/info/:suppliernr' element={<SupplierInfo getProducts={getProducts} getSuppliers={getSuppliers} authorisedUser={authorisedUser} />} />
        <Route path='/suppliers/add-supplier' element={<AddSupplierForm addSupplier={addSupplier} />} />
        <Route path='/suppliers/edit/:id' element={<EditSupplierForm getSuppliers={getSuppliers} updateSupplier={updateSupplier} />} />

        <Route path='/employees' element={<Employees getEmployees={getEmployees} updateEmployee={updateEmployee} updateAddress={updateAddress} deleteEmployee={deleteEmployee} getEmployeeRoles={getEmployeeRoles} authorisedUser={authorisedUser} />} />
        <Route path='/employees/add-employee' element={<AddEmployeeForm authorisedUser={authorisedUser} addAddress={addAddress} addEmployee={addEmployee} getEmployeeRoles={getEmployeeRoles} />} />
        <Route path='/employees/info/:id' element={<EmployeeDetails authorisedUser={authorisedUser} getEmployeeByEmployeenr={getEmployeeByEmployeenr} />} />

        <Route path='/notifications' element={<Notifications getNotificationsForEmployee={getNotificationsForEmployee} authorisedUser={authorisedUser} updateNotification={updateNotification} />} />

        <Route path='/orders/customer' element={<CustomerOrders authorisedUser={authorisedUser} getCustomerOrderProducts={getCustomerOrderProducts} getPaymentMethods={getPaymentMethods} getOrderStatuses={getOrderStatuses} updateCustomerPayment={updateCustomerPayment} getCustomerOrders={getCustomerOrders} updateCustomerOrder={updateCustomerOrder} deleteCustomerOrder={deleteCustomerOrder} getCustomerPayments={getCustomerPayments} />} />
        <Route path='/orders/customer-order/:ordernr' element={<CustomerOrderDetails getPaymentMethods={getPaymentMethods} getCustomerOrderByOrdernr={getCustomerOrderByOrdernr} getCustomerPaymentByOrdernr={getCustomerPaymentByOrdernr} getCustomerOrderProducts={getCustomerOrderProducts} />} />
        <Route path='/orders/add-customer-order' element={<AddCustomerOrder authorisedUser={authorisedUser} getPaymentMethods={getPaymentMethods} addCustomerOrder={addCustomerOrder} getCustomerByEmail={getCustomerByEmail} getLocationProductsByWarehouse={getLocationProductsByWarehouse} getWarehouses={getWarehouses} getCustomerPayments={getCustomerPayments} />} />

        <Route path='/orders/warehouse' element={<WarehouseOrders />} />

        <Route path="/warehouse-orders" element={<WarehouseOrders />} />
        <Route path="/warehouse-order/:ordernr" element={<WarehouseOrderProduct getProducts={getProducts} getSuppliers={getSuppliers} />} />
        <Route path='/warehouse-order/add-order' element={<AddWarehouseOrderForm addWarehouseOrder={addWarehouseOrder} />} />
        <Route path='/warehouse-order/edit-order/:ordernr' element={<EditWarehouseOrderForm />} />

        <Route path='/settings' element={<Settings authorisedUser={authorisedUser} getEmployeeForSettings={getEmployeeForSettings} updateAuthenticatedEmployee={updateAuthenticatedEmployee} updateAddress={updateAddress} />} />

        <Route path='/support' element={<Support />} />

      </Routes>

    </div>

  );
}

export default Main;
