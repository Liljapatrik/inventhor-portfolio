// This file is used to configure Keycloak for authentication in the application.
import { ReactKeycloakProvider, useKeycloak } from "@react-keycloak/web";
import keycloak from './keycloak';

/*Enable touse bootstrap icons*/
import 'bootstrap-icons/font/bootstrap-icons.css';

/*Enable bootstrap*/
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.min.js';

/*Connect stylesheet for App.jsx*/
import './App.css';

/*Connecting components*/
import Header from './components/Header';
import SideBar from './components/SideBar';
import Main from './components/Main';
import SignIn from './components/SignIn';

import { useState, useEffect } from 'react';

import { useNavigate, Routes, Route} from "react-router-dom";

import { Modal, Button } from 'react-bootstrap';



/*Data implementation*/
import { getWarehouseInfo} from './data/dataFetching';
import { getEmployees,
          getEmployeeByEmail,
          updateEmployee, 
          updateAddress, 
          deleteEmployee, 
          getEmployeeByEmployeenr,
          getEmployeeForSettings,
          getEmployeeRoles,
          getNotificationsForEmployee,
          updateAuthenticatedEmployee,
          updateNotification, 
          addAddress, 
          addEmployee, 
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
          getSuppliers,
          getSuppliersByProductnr,
          addProductToSupplier,
          getCustomerOrderByOrdernr,
          getCustomerOrders,
          addCustomerOrder,
          deleteCustomerOrder,
          getWarehouses,
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
          createCustomerOrder,
          getLocationProducts,
          getLocationProductsByProductnr,
          getLocationProductsByWarehouse,
          getPriceHistoryByProductnr,
          getSellingHistoryByProduct} from './data/ServerData';

function App() {

  const navigate = useNavigate();

  const [authorisedUser, setauthorisedUser] = useState({});

  const [showErrorModal, setShowErrorModal] = useState(false);

  useEffect(() => {
    const storedUser = localStorage.getItem('authorisedUser');
    if (storedUser) {
      setauthorisedUser(JSON.parse(storedUser));
    }
  }, []);

  function login(email, password) {

    getEmployeeByEmail(email).then((employee) => {

      let authok = false;

      if (email == employee.email) {
        console.log(employee);
        setauthorisedUser(employee);
        authok = true;

        localStorage.setItem('authorisedUser', JSON.stringify(employee));

        navigate("/inventory" );
      }

      if (!authok) {
        setShowErrorModal(true);
      }
    });
  }

  const handleCloseErrorModal = () => setShowErrorModal(false);

  function logout() {

    localStorage.removeItem('authorisedUser');
    setauthorisedUser({});

    keycloak.logout( { redirectUri: window.location.origin });

    //  navigate("/signin" );
    
  }


  return  <ReactKeycloakProvider authClient={keycloak} initOptions={{ checkLoginIframe: false }}> {/* checkLoginIframe:  */}

    <Header authorisedUser={authorisedUser} logout={logout} getNotificationsForEmployee={getNotificationsForEmployee} updateNotification={updateNotification} />
    <Routes>
      <Route path='/signin' element={<SignIn login={login} />} />
      <Route path="/*" element={
        <>
          <SideBar authorisedUser={authorisedUser}/>
          <Main
            authorisedUser={authorisedUser}

            getSuppliers={getSuppliers}
            getSuppliersByProductnr={getSuppliersByProductnr}

            getCategories={getCategories}
            getCategoryById={getCategoryById}
            addCategory={addCategory}
            updateCategory={updateCategory}
            deleteCategory={deleteCategory}

            getProductByNr={getProductByNr}  
            getProducts={getProducts} 
            updateProduct={updateProduct} 
            addProduct={addProduct} 
            deleteProduct={deleteProduct}

            getWarehouseInfo={getWarehouseInfo}
            getWarehouses={getWarehouses}
            
            getEmployees = {getEmployees}
            updateEmployee = {updateEmployee} 
            updateAddress = {updateAddress} 
            deleteEmployee = {deleteEmployee}
            getEmployeeByEmployeenr = {getEmployeeByEmployeenr}
            getEmployeeForSettings = {getEmployeeForSettings}
            getEmployeeByEmail = {getEmployeeByEmail}
            addAddress = {addAddress} 
            addEmployee = {addEmployee}
            updateAuthenticatedEmployee = {updateAuthenticatedEmployee}
            
            getEmployeeRoles = {getEmployeeRoles}
            
            getNotificationsForEmployee ={getNotificationsForEmployee}
            updateNotification = {updateNotification}

            getCustomerOrderByOrdernr = {getCustomerOrderByOrdernr}
            getCustomerOrders = {getCustomerOrders}
            addCustomerOrder = {addCustomerOrder}
            deleteCustomerOrder = {deleteCustomerOrder}
            updateCustomerOrder = {updateCustomerOrder}

            getPaymentMethods={getPaymentMethods}
            getCustomerPaymentByOrdernr={getCustomerPaymentByOrdernr}
            getCustomerPayments={getCustomerPayments}
            updateCustomerPayment={updateCustomerPayment}

            getOrderStatuses={getOrderStatuses}

            getCustomerOrderProducts={getCustomerOrderProducts}

            getLocationProductsByWarehouse={getLocationProductsByWarehouse}
            getLocationProducts={getLocationProducts}
            getLocationProductsByProductnr={getLocationProductsByProductnr}

            getCustomers={getCustomers}
            addCustomer={addCustomer}
            getCustomerByEmail={getCustomerByEmail}

            getPriceHistoryByProductnr={getPriceHistoryByProductnr}

            getSellingHistoryByProduct={getSellingHistoryByProduct}
            />
        </>
      } />
      
    </Routes>

    <Modal show={showErrorModal} onHide={handleCloseErrorModal} className="modal-centered">
      <Modal.Header closeButton>
        <Modal.Title>Authentication Error</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        Wrong email or password! If you forget your password, contact us with email: inventhor@support.no
      </Modal.Body>
      <Modal.Footer>
        <Button variant="secondary" onClick={handleCloseErrorModal}>
          Close
        </Button>
      </Modal.Footer>
    </Modal>

    {/*<SideBar />
    <Main getSuppliers={getSuppliers} addSupplier={addSupplier} getEmployees={getEmployees} addEmployees={addEmployees} />*/}

  </ReactKeycloakProvider>;
}

export default App;