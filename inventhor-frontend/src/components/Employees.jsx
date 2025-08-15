/**
 * Author: Tatiana Fløisbonn
 * Date: 14 February 2025
 * Description: Employees component for displaying and managing employees.
 * It includes search and sort functionalities, allows editing employee details,
 * deleting employees, and fetching employee roles.
 * It uses React hooks for state management and side effects.
 * It also handles modals for editing and deleting employees.
 * 
 * This component is part of the admin panel
 */

import { useState, useEffect } from 'react';

function Employees({ getEmployees, updateEmployee, updateAddress, deleteEmployee, getEmployeeRoles, authorisedUser }) {

  let [search, setSearch] = useState("")
  let [sort, setSort] = useState("name")
  let [employees, setEmployees] = useState([])

  // State for modals
  const [isDeleteMessageOpen, setIsDeleteMessageOpen] = useState(false);
  const [isDeleteSuccessOpen, setIsDeleteSuccessOpen] = useState(false);

  const [isEditModalOpen, setIsEditModalOpen] = useState(false);
  const [selectedEmployee, setSelectedEmployee] = useState(null);

  function onSearchChange(e) {
    setSearch(e.target.value)
  }

  // Handle sort change
  function onSortChange(e) {
    setSort(e.target.value)
  }

  // Function to get sorted and filtered employees data
  function getSortedEmployeesData() {
    return employees
      .filter((item) => {
        const searchTerm = search.toLowerCase();
        return (
          item.employeddate.toLowerCase().includes(searchTerm) ||
          item.firstname.toLowerCase().includes(searchTerm) ||
          item.lastname.toLowerCase().includes(searchTerm) ||
          item.email.toLowerCase().includes(searchTerm) ||
          item.role.name.toLowerCase().includes(searchTerm) ||
          (item.isactive ? "Active" : "Not Active").toLowerCase().includes(searchTerm)
        );
      })
      .sort((a, b) => {
        if (sort === "role") {
          return (a.role.name + "").localeCompare(b.role.name + "");
        }
        if (sort === "active") {
          return (a.isactive ? "Active" : "Not Active").localeCompare(b.isactive ? "Active" : "Not Active");
        }
        if (sort === "date") {
          return (a.employeddate + "").localeCompare(b.employeddate + "");
        }
        else {
          return (a[sort] + "").localeCompare(b[sort] + "");
        }
      });
  }

  // Fetch employees when the component mounts
  useEffect(() => {
    getEmployees().then((employees) => setEmployees(employees));
  }, []);

  // Handle delete click
  const handleDeleteClick = async (employee) => {
    if (!employee.active && employee.email !== authorisedUser.email) {
      try {
        // delete the employee
        await deleteEmployee(employee.employeenr);

        // Update the local state
        getEmployees().then((employees) => setEmployees(employees))

        // Prevent URL changes by ensuring no redirection occurs
        window.history.replaceState(null, "", "/employees");

        // Show success popup
        setIsDeleteSuccessOpen(true);

      } catch (error) {
        console.error("Error deleting employee or address:", error);
        alert("An error occurred while deleting the employee or their address.");
      }
    } else {
      setIsDeleteMessageOpen(true);
    }
  };


  const handleDeleteMessageClose = () => {
    setIsDeleteMessageOpen(false);
  };

  const handleDeleteSuccessClose = () => {
    setIsDeleteSuccessOpen(false);
  };

  // Edit modal handlers
  const handleEditClick = async (employee) => {
    setSelectedEmployee(employee);
    setIsEditModalOpen(true);
  };

  const handleEditModalClose = () => {
    setIsEditModalOpen(false);
    setSelectedEmployee(null);
  };

  const handleEditFormSubmit = async (e) => {
    e.preventDefault()

    // Update the employee details
    const updatedEmployee = {
      firstname: selectedEmployee.firstname,
      lastname: selectedEmployee.lastname,
      phone: selectedEmployee.phone,
      email: selectedEmployee.email,
      position: selectedEmployee.position,
      role: {
        rolenr: selectedEmployee.role.rolenr
      },
      isactive: selectedEmployee.isactive,
      employeddate: selectedEmployee.employeddate,
      image: selectedEmployee.image,
      passwordhash: selectedEmployee.passwordhash
    };

    await updateEmployee(selectedEmployee.employeenr, updatedEmployee);

    // Update the address first
    const updatedAddress = {
      street: selectedEmployee.address.street,
      postcode: selectedEmployee.address.postcode,
      city: selectedEmployee.address.city,
      country: selectedEmployee.address.country
    };

    await updateAddress(selectedEmployee.address.addressnr, updatedAddress);

    // Update the local state
    getEmployees().then((employees) => setEmployees(employees))

    // Prevent URL changes by ensuring no redirection occurs
    window.history.replaceState(null, "", "/employees");

    setIsEditModalOpen(false);
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    // Check if the field belongs to the address object
    if (["street", "postcode", "city", "country"].includes(name)) {
      setSelectedEmployee((prevState) => ({
        ...prevState,
        address: {
          ...prevState.address,
          [name]: value, // Update the specific field in the address object
        },
      }));
    } else if (["role"].includes(name)) {
      setSelectedEmployee((prevState) => ({
        ...prevState,
        role: {
          ...prevState.role,
          rolenr: value, // Update the role number directly
        },
      }));
    } else if (name === "isactive") {
      // Convert the value to a boolean
      const isactive = value === "true";
      setSelectedEmployee((prevState) => ({
        ...prevState,
        isactive: isactive, // Update the isactive field directly
      }));
    } else {
      // Update fields directly on selectedEmployee
      setSelectedEmployee((prevState) => ({
        ...prevState,
        [name]: value,
      }));
    }
  };

  // State to store roles for the dropdown
  const [roles, setRoles] = useState([]);
  // State to control if the role dropdown is open (for fetching roles on open)
  const [roleDropdownOpen, setRoleDropdownOpen] = useState(false);

  // Render access denied if not admin
  if (!authorisedUser || authorisedUser.role?.name !== "admin") {
    return (
      <div style={{ padding: "2em", color: "red", textAlign: "center" }}>
        Access denied. This page is only available to admin users.
      </div>
    );
  }

  // Fetch roles only when the edit modal is opened and dropdown is interacted with
  const handleRoleDropdownOpen = async () => {
    if (roles.length === 0) {
      const fetchedRoles = await getEmployeeRoles();
      setRoles(fetchedRoles);
    }
    setRoleDropdownOpen(true);
  };

  const handleRoleDropdownClose = () => {
    setRoleDropdownOpen(false);
  };

  return (
    <>

      <h1>Employees</h1>

      <div className="tableFunctions">

        <div className="tableSeachbar">
          <i className="bi bi-search"></i>
          <input className="tableSeachInput" type="text" placeholder="Search" onChange={onSearchChange} value={search}></input>
        </div>

        <div className='sortWithAddFunctions'>


          <select className="tableSort" onChange={onSortChange} value={sort}>
            <option value="employeddate">
              Date
              <i className='bi bi-chevron-down'></i>
            </option>
            <option value="firstname">F. Name</option>
            <option value="lastname">L. Name</option>
            <option value="email">Email</option>
            <option value="role">Role</option>
            <option value="active">Activity</option>
          </select>

          <button className="addBtn" onClick={() => window.location.href = "/employees/add-employee"}>
            Add Employee
          </button>

        </div>

      </div>

      <div className="table-container">
        <table>
          <thead>
            <tr>
              <th>Date</th>
              <th>First Name</th>
              <th>Last Name</th>
              <th>Email</th>
              <th>Role</th>
              <th>Activity</th>
              <th></th>
              <th></th>
            </tr>
          </thead>

          <tbody>
            {employees.length > 0 &&
              getSortedEmployeesData().map((item) => {
                return <tr key={item.employeenr}>
                  <td><a href={'/employees/info/' + item.employeenr} >{item.employeddate}</a></td>
                  <td><a href={'/employees/info/' + item.employeenr} >{item.firstname}</a></td>
                  <td><a href={'/employees/info/' + item.employeenr} >{item.lastname}</a></td>
                  <td><a href={'/employees/info/' + item.employeenr} >{item.email}</a></td>
                  <td><a href={'/employees/info/' + item.employeenr} >{item.role.name}</a></td>
                  <td><a href={'/employees/info/' + item.employeenr} >{item.isactive ? "Active" : "Not Active"}</a></td>

                  <td className='bi bi-pencil-square' onClick={() => handleEditClick(item)}></td>
                  <td className='bi bi-trash' onClick={() => handleDeleteClick(item)}></td>
                </tr>
              })
            }
          </tbody>
        </table>

      </div>

      {isEditModalOpen && selectedEmployee && (
        <div className="t-modal">
          <div className="t-modal-content">
            <span className="close" onClick={handleEditModalClose}>
              &times;
            </span>
            <h2>Edit Employee</h2>
            <form onSubmit={handleEditFormSubmit}>
              <div className="t-form-group">
                <label htmlFor="firstname">First Name:</label>
                <input
                  type="text"
                  id="firstname"
                  name="firstname"
                  value={selectedEmployee.firstname}
                  onChange={handleInputChange}
                />
              </div>
              <div className="t-form-group">
                <label htmlFor="lastname">Last Name:</label>
                <input
                  type="text"
                  id="lastname"
                  name="lastname"
                  value={selectedEmployee.lastname}
                  onChange={handleInputChange}
                />
              </div>
              <div className="t-form-group">
                <label htmlFor="email">Email:</label>
                <input
                  type="email"
                  id="email"
                  name="email"
                  value={selectedEmployee.email}
                  onChange={handleInputChange}
                />
              </div>
              <div className="t-form-group">
                <label htmlFor="phone">Phone:</label>
                <input
                  type="text"
                  id="phone"
                  name="phone"
                  value={selectedEmployee.phone}
                  onChange={handleInputChange}
                />
              </div>
              <div className="t-form-group">
                <label htmlFor="position">Position:</label>
                <input
                  type="text"
                  id="position"
                  name="position"
                  value={selectedEmployee.position}
                  onChange={handleInputChange}
                />
              </div>
              <div className="t-form-group">
                <label htmlFor="role">Role:</label>
                <select
                  id="role"
                  name="role"
                  value={selectedEmployee.role.rolenr}
                  onChange={handleInputChange}
                  onFocus={handleRoleDropdownOpen}
                  onBlur={handleRoleDropdownClose}
                >
                  {/* Show current role as default */}
                  <option value={selectedEmployee.role.rolenr}>
                    {selectedEmployee.role.name}
                  </option>
                  {/* Show all roles if dropdown is open and roles are loaded */}
                  {roleDropdownOpen && roles.length > 0 &&
                    roles
                      .filter(role => role.rolenr !== selectedEmployee.role.rolenr)
                      .map(role => (
                        <option key={role.rolenr} value={role.rolenr}>
                          {role.name}
                        </option>
                      ))
                  }
                </select>
              </div>
              <div className="t-form-group">
                <label htmlFor="isactive">Active:</label>
                <select
                  id="isactive"
                  name="isactive"
                  value={selectedEmployee.isactive ? "true" : "false"}
                  onChange={handleInputChange}
                >
                  <option value="true">Yes</option>
                  <option value="false">No</option>
                </select>
              </div>
              <div className='t-form-group'>
                <label htmlFor="street">Street:</label>
                <input
                  type="text"
                  id="street"
                  name="street"
                  value={selectedEmployee.address.street}
                  onChange={handleInputChange}
                />
              </div>
              <div className='t-form-group'>
                <label htmlFor="postCode">Post Code:</label>
                <input
                  type="text"
                  id="postcode"
                  name="postcode"
                  value={selectedEmployee.address.postcode}
                  onChange={handleInputChange}
                />
              </div>
              <div className='t-form-group'>
                <label htmlFor="city">City:</label>
                <input
                  type="text"
                  id="city"
                  name="city"
                  value={selectedEmployee.address.city}
                  onChange={handleInputChange}
                />
              </div>
              <div className='t-form-group'>
                <label htmlFor="country">Country:</label>
                <input
                  type="text"
                  id="country"
                  name="country"
                  value={selectedEmployee.address.country}
                  onChange={handleInputChange}
                />
                <div className='t-form-group'>
                  <label htmlFor="image">Image:</label>
                  <input
                    type="text"
                    id="image"
                    name="image"
                    value={selectedEmployee.image}
                    onChange={handleInputChange}
                  />
                </div>
              </div>
              <button type="submit">Save</button>
            </form>
          </div>
        </div>
      )}

      {isDeleteSuccessOpen && (
        <div className="t-modal">
          <div className="t-modal-content">
            <span className="t-close" onClick={handleDeleteSuccessClose}>
              &times;
            </span>
            <h2>Employee Deleted</h2>
            <p>The employee was successfully deleted.</p>
            <button className="t-deleteErrorBtn" onClick={handleDeleteSuccessClose}>
              Close
            </button>
          </div>
        </div>
      )}

      {isDeleteMessageOpen && (
        <div className="t-modal">
          <div className="t-modal-content">
            <span className="t-close" onClick={handleDeleteMessageClose}>
              &times;
            </span>
            <h2>Delete Error</h2>
            <p>The employee cannot be deleted because he/she is active or it is you.</p>
            <button className="t-deleteErrorBtn" onClick={handleDeleteMessageClose}>
              Close
            </button>
          </div>
        </div>
      )}
    </>
  )
}

export default Employees;