import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import './addWarehouseOrderForm.css';

/**
 * AddWarehouseOrderForm component
 * 
 * This React component renders a form for creating a new warehouse order.
 * It fetches and manages data for warehouses, suppliers, and products dynamically,
 * allowing the user to select a warehouse, supplier, delivery date, and multiple products
 * with quantities and buy prices. The form submits the data via a POST request to the backend API.
 * 
 * Access is restricted to authorized users with a valid token, and error handling
 * is included for API responses.
 * 
 * Author: Patrik Lilja
 */
function AddWarehouseOrderForm({addWarehouseOrder}) {
    // Declare a state variable to store information about the supplier
    // With an initial empty string value "set..."
    // to update the information state
    const [ordernr, setOrdernr] = useState("");
    const [warehousenr, setWarehousenr] = useState("");
    const [supplier, setSupplier] = useState("");
    const [orderdate, setOrderdate] = useState("");
    const [statusnr, setStatusnr] = useState("");
    const [deliverydate, setDeliverydate] = useState("");
    const [products, setProducts] = useState([
        { productnr: "", quantity: "", buyprice: "" }
    ]);
    const[suppliers, setSuppliers] = useState([]);
    const [availableProducts, setAvailableProducts] = useState([]);
    const [warehouses, setWarehouses] = useState([]);


  const navigate = useNavigate();

  // Modal state for success and error
  const [isSuccessModalOpen, setIsSuccessModalOpen] = useState(false);
  const [isErrorModalOpen, setIsErrorModalOpen] = useState(false);
  const [errorMessage, setErrorMessage] = useState("");

    // Get employeeID från authorisedUser i localStorage
    const token = localStorage.getItem("access_token");
    const authorisedUserStr = localStorage.getItem("authorisedUser");
    const authorisedUser = authorisedUserStr ? JSON.parse(authorisedUserStr) : null;
    const employeenr = authorisedUser ? authorisedUser.employeenr : null;

  useEffect(() => {
    const fetchWarehouses = async () => {
      try {
        const res = await fetch("http://localhost:8080/warehouses", {
          headers: token ? { Authorization: `Bearer ${token}` } : {}
        });
        if (!res.ok) {
          throw new Error(`HTTP error! status: ${res.status}`);
        }
        const text = await res.text();
        const data = text ? JSON.parse(text) : [];
        setWarehouses(data);
      } catch (error) {
        console.error("Failed to fetch warehouses:", error);
        setWarehouses([]);
      }
    };
    fetchWarehouses();
  }, [token]);

  useEffect(() => {
    const fetchSuppliers = async () => {
      try {
        const res = await fetch("http://localhost:8080/suppliers", {
          headers: token ? { Authorization: `Bearer ${token}` } : {}
        });
        if (!res.ok) {
          throw new Error(`HTTP error! status: ${res.status}`);
        }
        const text = await res.text();
        const data = text ? JSON.parse(text) : [];
        setSuppliers(data);
      } catch (error) {
        console.error("Failed to fetch suppliers:", error);
        setSuppliers([]);
      }
    };
    fetchSuppliers();
  }, [token]);

  useEffect(() => {
    const fetchProducts = async () => {
      if (!supplier) return;
      try {
        const res = await fetch(`http://localhost:8080/product-suppliers/products-by-supplier/${supplier}`, {
          headers: token ? { Authorization: `Bearer ${token}` } : {}
        });
        if (!res.ok) {
          throw new Error(`HTTP error! status: ${res.status}`);
        }
        const text = await res.text();
        const data = text ? JSON.parse(text) : [];
        setAvailableProducts(data);
      } catch (error) {
        console.error("Failed to fetch products for supplier:", error);
        setAvailableProducts([]);
      }
    };
    fetchProducts();
  }, [supplier, token]);

  const handleProductChange = (index, field, value) => {
    const updated = [...products];
    updated[index][field] = value;
    setProducts(updated);
  };

  const handleAddProduct = () => {
    setProducts([...products, { productnr: "", quantity: "", buyprice: "" }]);
  };

  const handleRemoveProduct = (index) => {
    setProducts(products.filter((_, i) => i !== index));
  };



  const handleSubmit = async (e) => {
    e.preventDefault();

    
    if (!warehousenr || !supplier || products.some(p => !p.productnr || !p.quantity || !p.buyprice)) {
      setErrorMessage("Please fill in all fields");
      setIsErrorModalOpen(true);
      return;
    }

  
    const newOrder = {
      warehousenr: parseInt(warehousenr, 10),
      suppliernr: parseInt(supplier, 10),
      deliverydate: deliverydate || null,
      products: products.map(p => ({
        productnr: parseInt(p.productnr, 10),
        quantity: parseFloat(p.quantity),
        buyprice: parseFloat(p.buyprice),
      })),
    };

    try {
      const response = await fetch(`http://localhost:8080/warehouse-orders?employeenr=${employeenr}`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          "Authorization": `Bearer ${token}`
        },
        body: JSON.stringify(newOrder),
      });

      if (!response.ok) {
        const errorText = await response.text();
        console.error("Server error response:", errorText);
        if (response.status === 403) {
          throw new Error("You are not authorized to add warehouse orders.");
        }
        throw new Error(errorText || `Failed to add warehouse order. Status: ${response.status}`);
      }

      setIsSuccessModalOpen(true);

    } catch (error) {
      console.error("Error adding warehouse order:", error);
      setErrorMessage("Error adding warehouse order: " + error.message);
      setIsErrorModalOpen(true);
    }
  };

  // Handler for closing the success modal and navigating
  const handleSuccessModalClose = () => {
    setIsSuccessModalOpen(false);
    navigate("/warehouse-orders");
  };

  return (

    <>

      <form onSubmit={handleSubmit}>
        <h1>New Warehouse Order</h1>

        <div className="formField">
          <label>Warehouse:</label>
          <select
            value={warehousenr}
            onChange={(e) => setWarehousenr(e.target.value)}
            required
            className='shortInputField'
          >
            <option value="">Select warehouse</option>
            {warehouses.map(w => (
              <option key={w.warehousenr} value={w.warehousenr}>
                {w.name}
              </option>
            ))}
          </select>
        </div>

        <div className="formField">
          <label>Supplier:</label>
          <select
            value={supplier}
            onChange={(e) => setSupplier(e.target.value)}
            required
            className='shortInputField'
          >
            <option value="">Select supplier</option>
            {suppliers.map(s => (
              <option key={s.suppliernr} value={s.suppliernr}>
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

        <h3>Products:</h3>
        {products.map((p, index) => (
          <div key={index} className="productFields">
            <select
              value={p.productnr}
              onChange={(e) => handleProductChange(index, "productnr", e.target.value)}
              required
            >
              <option value="">Select product</option>
              {availableProducts.map(prod => (
                <option key={prod.productnr} value={prod.productnr}>
                  {prod.productname}
                </option>
              ))}
            </select>
            <input
              type="number"
              placeholder="Quantity"
              value={p.quantity}
              onChange={(e) => handleProductChange(index, "quantity", e.target.value)}
              required
            />
            <input
              type="number"
              step="0.01"
              placeholder="Buy Price"
              value={p.buyprice}
              onChange={(e) => handleProductChange(index, "buyprice", e.target.value)}
              required
            />
            {products.length > 1 && (
              <button type="button" onClick={() => handleRemoveProduct(index)}>🗑</button>
            )}
          </div>
        ))}

        <button type="button" onClick={handleAddProduct}>➕ Add Product</button>
        <br /><br />
        <button type="submit" className="addBtn">Submit Order</button>
      </form>

      {/* Success Modal */}
      {isSuccessModalOpen && (
        <div className="t-modal">
          <div className="t-modal-content">
            <span className="t-close" onClick={handleSuccessModalClose}>
              &times;
            </span>
            <h2>Order Added</h2>
            <p>The warehouse order was successfully added.</p>
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
            <span className="t-close" onClick={() => setIsErrorModalOpen(false)}>
              &times;
            </span>
            <h2>Add Error</h2>
            <p>{errorMessage}</p>
            <button className="t-deleteErrorBtn" onClick={() => setIsErrorModalOpen(false)}>
              Close
            </button>
          </div>
        </div>
      )}

    </>


  );
}

export default AddWarehouseOrderForm;