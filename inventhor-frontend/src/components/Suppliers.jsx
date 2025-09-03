import React, { useState, useEffect } from 'react';
import { useNavigate } from "react-router-dom";
import { Link } from "react-router-dom";
import './supplier.css';

/**
 * Suppliers component
 * 
 * Displays a list of all suppliers with functionality to search, sort, and navigate
 * to detailed supplier info. Allows authorized admin users to add, edit, and delete suppliers.
 * Deletion requires confirmation via a modal dialog.
 * Fetches supplier data from the backend with proper authentication.
 * 
 * @Author Patrik Lilja
 */

function Suppliers() {

  // Define navigate
  const navigate = useNavigate();

  let [search, setSearch] = useState("")
  let [sort, setSort] = useState("name")
  let [suppliers, setSuppliers] = useState([])



  // State for modal vivibility and selected supplie
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [supplierToDelete, setSupplierToDelete] = useState(null);

  // Get employeeID från authorisedUser i localStorage
  const authorisedUserStr = localStorage.getItem("authorisedUser");
  const authorisedUser = authorisedUserStr ? JSON.parse(authorisedUserStr) : null;
  const employeenr = authorisedUser ? authorisedUser.employeenr : null;


  async function getSuppliers() {
    try {
      const response = await fetch("http://localhost:8080/suppliers",
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("access_token")}`
          }
        }
      )
      if (!response.ok) {
        throw new Error("Supplier not found");
      }
      const data = await response.json();
      return data;
    } catch (error) {
      console.error(error);
      alert("Could not fetch supplier: " + error.message);
      return [];
    }
  }

  function onSearchChange(e) {
    setSearch(e.target.value)
  }

  function onSortChange(e) {
    setSort(e.target.value)
  }

  function getSortedSuppliersData() {
    return suppliers
      .filter((item) => {
        const searchTerm = search.toLowerCase();
        return (
          item.name.toLowerCase().includes(searchTerm) ||
          item.contact.toLowerCase().includes(searchTerm) ||
          item.phone.toLowerCase().includes(searchTerm) ||
          item.email.toLowerCase().includes(searchTerm) ||
          item.website.toLowerCase().includes(searchTerm)
        );
      })
      .sort((a, b) => {
        return (a[sort] + "").localeCompare(b[sort] + "");
      });
  }

  useEffect(() => {
    getSuppliers().then(setSuppliers);
  }, []); // Get the new list if it is any changes

  const deleteSupplier = async (suppliernr) => {
    try {
      const response = await fetch(`http://localhost:8080/suppliers/${suppliernr}?employeenr=${employeenr}`, {
        method: "DELETE",
        headers: {
          Authorization: `Bearer ${localStorage.getItem("access_token")}`
        }
      });

      if (!response.ok) {
        const errorMessage = await response.text();
        throw new Error(errorMessage || "Could not delete supplier");
      }

      const deletedSupplier = await response.json();
      console.log("Deleted:", deletedSupplier);

      // Update list
      setSuppliers((prev) => prev.filter(supplier => supplier.suppliernr !== suppliernr));
    } catch (error) {
      console.error("Error deleting supplier:", error);
      alert("Error deleting supplier: " + error.message);
    }
  };

  // Function when someone click on the delete-button
  const handleDelete = (id) => {
    setSupplierToDelete(id);
    setIsModalOpen(true);
  };

  // Confirm deletion 
  const confirmDelete = () => {
    if (deleteSupplier !== null) {
      deleteSupplier(supplierToDelete);
    }
    setIsModalOpen(false); 
    setSupplierToDelete(null);
  };

  // Cancel deletion
  const cancelDelete = () => {
    setIsModalOpen(false); 
  };



  return (
    <>

      <h1>Suppliers</h1>


      <div className="tableFunctions">
        <div className="tableSeachbar">
          <i class="bi bi-search"></i>
          <input class="tableSeachInput" type="text" placeholder="Search" onChange={onSearchChange} value={search}></input>
        </div>

        <div className='sortWithAddFunctions'>
          <select className="tableSort" onChange={onSortChange} value={sort}>
            <option selected value="supplierID">
              Supplier nr.
              <i className='bi bi-chevron-down'></i>
            </option>
            <option value="name">Name</option>
            <option value="contact">Contact Pers.</option>
          </select>

          
          {authorisedUser && authorisedUser.role.name === "admin" && (
            <button className="addBtn" onClick={() => window.location.href = "/suppliers/add-supplier"}>
              Add Supplier
            </button>
          )}
        </div>
      </div>

      <div className="table-container">
        <table>
          <thead>
            <tr>
              <th>Supplier nr.</th>
              <th>Name</th>
              <th>Contact person</th>
              <th>Phone number</th>
              <th>Email</th>
              <th>Website</th>
              
              {authorisedUser && authorisedUser.role.name === "admin" && <th>Edit</th>}
              {authorisedUser && authorisedUser.role.name === "admin" && <th>Delete</th>}
            </tr>
          </thead>

          <tbody>
            {suppliers.length > 0 &&
              getSortedSuppliersData().map((item) => {
                return (
                  <tr key={item.suppliernr}>
                    <td>{item.suppliernr}</td>
                    <td>
                      <Link to={`/suppliers/info/${item.suppliernr}`}>
                        {item.name}
                      </Link>
                    </td>
                    <td>{item.contact}</td>
                    <td>{item.phone}</td>
                    <td>{item.email}</td>
                    <td>{item.website}</td>
                    
                    {authorisedUser && authorisedUser.role.name === "admin" && (
                      <>
                        <td>
                          <button
                            className="editBtn"
                            onClick={() => navigate(`/suppliers/edit/${item.suppliernr}`)}
                          >
                            <i className="bi bi-pencil-square"></i>
                          </button>
                        </td>
                        <td>
                          <button
                            className="deleteBtn"
                            onClick={() => handleDelete(item.suppliernr)}
                          >
                            <i className="bi bi-trash"></i>
                          </button>
                        </td>
                      </>
                    )}
                  </tr>
                );
              })
            }
          </tbody>
        </table>
      </div>
      
      {isModalOpen && (
        <div className="modal">
          <div className="modal-content">
            <h3>Are you sure you want to delete this supplier?</h3>
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

export default Suppliers;