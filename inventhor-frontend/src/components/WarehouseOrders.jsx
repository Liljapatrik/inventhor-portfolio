import React, { useState, useEffect } from 'react';
import { useNavigate } from "react-router-dom";
import { Link } from "react-router-dom";
import './warehouseOrders.css';

/**
 * WarehouseOrders component handles displaying, searching, sorting,
 * and deleting warehouse orders.
 * 
 * Features include:
 * - Fetching orders from backend with authentication token.
 * - Searching orders by multiple fields.
 * - Sorting orders by selected column.
 * - Navigation to order details and edit pages.
 * - Confirmation modal for deleting orders.
 * - Handling delete requests to backend with user identification.
 * 
 * Uses React Hooks for state management and React Router for navigation and links.
 * 
 * @Author Patrik Lilja
 * 
 */

function WarehouseOrders() {

  // Functions
  const navigate = useNavigate();

  let [search, setSearch] = useState("")
  let [sort, setSort] = useState("name")
  let [orders, setOrders] = useState([])

  // State for modal vivibility and selected supplie
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [orderToDelete, setOrderToDelete] = useState(null);

  // Fetch orders from backend
  async function getOrders() {
  const token = localStorage.getItem("access_token");
  console.log("Token from localStorage:", token); 

  

  try {
    const response = await fetch("http://localhost:8080/warehouse-orders", {
      method: "GET",
      headers: {
        "Authorization": `Bearer ${token}`
      }
    });

    if (!response.ok) {
      const errorData = await response.json().catch(() => null);
      throw new Error(errorData?.message || `HTTP error! status: ${response.status}`);
    }

    const data = await response.json();
    console.log("Received data:", data);
    return data;
  } catch (error) {
    console.error("Full error:", error);
    alert("Could not fetch orders: " + error.message);
    return [];
  }
}

  

  function onSearchChange(e) {
    setSearch(e.target.value)
  }

  function onSortChange(e) {
    setSort(e.target.value)
  }

  function getSortedWarehouseOrdersData() {
    return orders
      .filter((item) => {
        const searchTerm = search.toLowerCase();
        return (
          String(item.ordernr).toLowerCase().includes(searchTerm) ||
          String(item.name).toLowerCase().includes(searchTerm) ||
          String(item.warehousenr).toLowerCase().includes(searchTerm) ||
          String(item.suppliername).toLowerCase().includes(searchTerm) ||
          String(item.orderdate).toLowerCase().includes(searchTerm) ||
          String(item.orderstatusname).toLowerCase().includes(searchTerm) ||
          String(item.deliverydate).toLowerCase().includes(searchTerm)
        );
      })
      .sort((a, b) => {
        return (a[sort] + "").localeCompare(b[sort] + "");
      });
  };

  useEffect(() => {
      getOrders().then(setOrders);
    }, []); // Get the new list if it is any changes

  const handleDelete = (ordernr) => {
    setOrderToDelete(ordernr);
    setIsModalOpen(true);
  };

  const cancelDelete = () => {
    setIsModalOpen(false);
    setOrderToDelete(null);
  };


  const confirmDelete = async () => {
    const token = localStorage.getItem("access_token");
    if (!orderToDelete) return;

    
    const authorisedUserStr = localStorage.getItem("authorisedUser");
    const authorisedUser = authorisedUserStr ? JSON.parse(authorisedUserStr) : null;
    const employeenr = authorisedUser ? authorisedUser.employeenr : null;

    if (!employeenr) {
      alert("User not authorized");
      setIsModalOpen(false);
      setOrderToDelete(null);
      return;
    }

    try {
      const response = await fetch(`http://localhost:8080/warehouse-orders/${orderToDelete}?employeenr=${employeenr}`, {
        method: 'DELETE',
        headers: {
          "Authorization": `Bearer ${token}`
        }
      });

      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(errorText || `Status: ${response.status}`);
      }

      alert(`Order ${orderToDelete} deleted!`);
      setIsModalOpen(false);
      setOrderToDelete(null);
      getOrders().then(setOrders); 
    } catch (error) {
      console.error("Wrong with deliciton:", error);
      alert("Could not delete order: " + error.message);
      setIsModalOpen(false);
      setOrderToDelete(null);
    }
  };

  return (
    <>
      <h1>Warehouse Orders</h1>

      <div className="tableFunctions">
        <div className="tableSeachbar">
          <i class="bi bi-search"></i>
          <input class="tableSeachInput" type="text" placeholder="Search" onChange={onSearchChange} value={search}></input>
        </div>

        <div className='sortWithAddFunctions'>
          <select className="tableSort" onChange={onSortChange} value={sort}>
            <option selected value="ordernr">
              Order nr.
              <i className='bi bi-chevron-down'></i>
            </option>
            <option value="name">Name</option>
            <option value="contact">Orderdate</option>
          </select>
          <button className="addBtn" onClick={() => window.location.href = "/warehouse-order/add-order"}>
            Add Order
          </button>
        </div>
      </div>

      <div className="table-container">
        <table>
          <thead>
            <tr>
              <th>Order nr.</th>
              <th>Warehouse name</th>
              <th>Warehouse nr.</th>
              <th>Supplier name.</th>
              <th>Orderdate</th>
              <th>Status</th>
              <th>Deleverydate</th>
              <th>Edit</th>
              <th>Delete</th>
            </tr>
          </thead>

          <tbody>
            { orders.length > 0 &&
              getSortedWarehouseOrdersData().map((item) => {
                return <tr key ={item.ordernr}>
                  <td>
                    <Link to={`/warehouse-order/${item.ordernr}`}>
                      {item.ordernr}
                    </Link>
                  </td>
                  <td>
                    <Link to={`/warehouse-order/${item.ordernr}`}>
                      {item.name}
                    </Link>
                  </td>
                  <td>
                    <Link to={`/warehouse-order/${item.warehousenr}`}>
                      {item.warehousenr}
                    </Link>
                  </td>
                  <td>
                    <Link to={`/warehouse-order/${item.ordernr}`}>
                      {item.suppliername}
                    </Link>
                  </td>
                  <td>
                      <Link to={`/warehouse-order/${item.ordernr}`}>
                        {new Date(item.orderdate).toLocaleString()}
                      </Link>
                  </td>
                  <td>
                    <Link to={`/warehouse-order/${item.ordernr}`}>
                      {item.orderstatusname}
                    </Link>
                  </td>
                  <td>
                      <Link to={`/warehouse-order/${item.ordernr}`}>
                        {item.deliverydate ? new Date(item.deliverydate).toLocaleString() : "-"}
                      </Link>
                  </td>
                  <td>
                    {/*Edit button for warehouseorders */}
                    <button className="editBtn" onClick={() => navigate(`/warehouse-order/edit-order/${item.ordernr}`)}>
                      <i className="bi bi-pencil-square"></i>
                    </button>
                  </td>
                  <td>
                    {/*Delete button for warehouseorders  */}
                    <button className="deleteBtn" onClick={() => handleDelete(item.ordernr)}>
                      <i className="bi bi-trash"></i>
                    </button>
                  </td>
                </tr>
              })
            }
          </tbody>
        </table>
      </div>
    {isModalOpen && (
    <div className="modal">
      <div className="modal-content">
        <h3>Are you sure you want to delete order nr {orderToDelete}?</h3>
        <div className="modal-buttons">
          <button className="modal-btn" onClick={cancelDelete}>
            Cancel
          </button>
          <button className="modal-btn" onClick={confirmDelete}>
            Yes
          </button>
          </div>
        </div>
      </div>
      )}
    </>
  )
}

export default WarehouseOrders