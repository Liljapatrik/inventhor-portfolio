import React, { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';

/**
 * EditWarehouseOrderForm component
 * 
 * Allows editing an existing warehouse order's status and delivery date.
 * Fetches the order based on URL parameter `ordernr`, loads available statuses,
 * populates form fields, and submits updates to the backend with authentication.
 * 
 * @Author Patrik Lilja
 */
function EditWarehouseOrderForm({editWarehouseOrder}) {
  const [statusnr, setStatusnr] = useState("");
  const [deliverydate, setDeliverydate] = useState("");
  const [statuses, setStatuses] = useState([]);

  const { ordernr } = useParams(); 
  const navigate = useNavigate();

  // Get employeeID från authorisedUser i localStorage
  const authorisedUserStr = localStorage.getItem("authorisedUser");
  const authorisedUser = authorisedUserStr ? JSON.parse(authorisedUserStr) : null;
  const employeenr = authorisedUser ? authorisedUser.employeenr : null;

  const token = localStorage.getItem("access_token");



  // Define state for modals and error messages
  const [isSuccessModalOpen, setIsSuccessModalOpen] = useState(false);
  const [isErrorModalOpen, setIsErrorModalOpen] = useState(false);
  const [errorMessage, setErrorMessage] = useState("");

  
  
  useEffect(() => {
    const fetchOrder = async () => {
      try {
        const response = await fetch(`http://localhost:8080/warehouse-orders/${ordernr}`, {
          headers: { "Authorization": `Bearer ${token}` },
        });

        if (!response.ok) throw new Error("Could not get order");

        const data = await response.json();
        setStatusnr(data.statusnr);
        setDeliverydate(data.deliverydate ? data.deliverydate.substring(0, 16) : ""); 
      } catch (error) {
        console.error("Failed to get order:", error);
        alert("Could not get order data.");
      }
    };

    fetchOrder();
  }, [ordernr, token]);

  
  useEffect(() => {
    const fetchStatuses = async () => {
      try {
        const res = await fetch("http://localhost:8080/order-status", {
          headers: { "Authorization": `Bearer ${token}` }
        });
        const data = await res.json();
        setStatuses(data);
      } catch (error) {
        console.error("Could not get status:", error);
      }
    };

    fetchStatuses();
  }, [token]);

  const handleSubmit = async (e) => {
    e.preventDefault();

    const updatedOrder = {
      statusnr: parseInt(statusnr, 10),
      deliverydate: deliverydate || null,
    };

    try {
      const response = await fetch(`http://localhost:8080/warehouse-orders/${ordernr}?employeenr=${employeenr}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
          "Authorization": `Bearer ${token}`,
        },
        body: JSON.stringify(updatedOrder),
      });

      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(errorText || `Status: ${response.status}`);
      }

      setIsSuccessModalOpen(true);
    } catch (error) {
      console.error("Failed to update:", error);
      // Set error message and open error modal
      setErrorMessage("Cannot update order: " + error.message);
      setIsErrorModalOpen(true);
    }
  };

  // Function to close the error modal
  const handleSuccessModalClose = () => {
    setIsSuccessModalOpen(false);
    navigate("/warehouse-orders");
  };

  // Function to close the error modal and redirect
  const handleErrorModalClose = () => {
    setIsErrorModalOpen(false);
    navigate("/warehouse-orders");
  };

  return (

    <>

      <form onSubmit={handleSubmit}>
        <h1>Edit Warehouse Order</h1>

        <div className="formField">
          <label>Status:</label>
          <select
            value={statusnr}
            onChange={(e) => setStatusnr(e.target.value)}
            required
            className='shortInputField'
          >
            <option value="">Choose status</option>
            {statuses.map((s) => (
              <option key={s.statusnr} value={s.statusnr}>
                {s.name}
              </option>
            ))}
          </select>
        </div>

        <div className="formField">
          <label>Delivery Date:</label>
          <input
            type="datetime-local"
            value={deliverydate}
            onChange={(e) => setDeliverydate(e.target.value)}
            className='shortInputField'
          />
        </div>

        <button type="submit" className="addBtn">Update Order</button>
      </form>

      {/* Success Modal */}
      {isSuccessModalOpen && (
        <div className="t-modal">
          <div className="t-modal-content">
            <span className="t-close" onClick={handleSuccessModalClose}>
              &times;
            </span>
            <h2>Order Updated</h2>
            <p>The warehouse order was successfully updated.</p>
            <button className="t-deleteErrorBtn" onClick={handleSuccessModalClose}>
              Close
            </button>
          </div>
        </div>
      )}

      {/* Error Modal */}
      {isErrorModalOpen && (
        <div className="t-modal">
          <div className="t-modal-content">
            <span className="t-close" onClick={handleErrorModalClose}>
              &times;
            </span>
            <h2>Update Error</h2>
            <p>{errorMessage}</p>
            <button className="t-deleteErrorBtn" onClick={handleErrorModalClose}>
              Close
            </button>
          </div>
        </div>
      )}

    </>
  );
}

export default EditWarehouseOrderForm;