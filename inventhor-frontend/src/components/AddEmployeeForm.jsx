/**
 * Author: Tatiana Fløisbonn
 * Date: 14 February 2025
 * Description: This component renders a form to add a new employee.
 * It includes fields for employee details such as name, email, position, role, address, and image URL.
 * It also handles form submission, validates input, and displays success or error messages in modals.
 * It uses React hooks for state management and the useNavigate hook from react-router-dom for navigation.
 * The form submission triggers an API call to add the employee and their address.
 * It also fetches employee roles from an API to populate the role selection dropdown.
 * It includes error handling for API calls and form validation to ensure all required fields are filled.
 */

import React, { useState } from 'react'; // Import React and useState hook for managing component state.
import { useNavigate } from 'react-router-dom'; // Import useNavigate from react-router-dom for navigation. It allows us to programmatically navigate to different routes in the application.

function AddEmployeeForm({ addAddress, addEmployee, getEmployeeRoles, authorisedUser }) {

    // State variables for form fields
    const [email, setEmail] = useState("");
    const [employeddate, setEmployeddate] = useState("");
    const [firstname, setFirstname] = useState("");
    const [lastname, setLastname] = useState("");
    const [position, setPosition] = useState("");
    const [role, setRole] = useState();
    const [image, setImage] = useState("");
    const [password, setPassword] = useState("");
    const [phone, setPhone] = useState("");

    const [postcode, setPostcode] = useState("");
    const [city, setCity] = useState("");
    const [country, setCountry] = useState("");
    const [street, setStreet] = useState("");

    // useNavigate hook for navigation
    const navigate = useNavigate();

    // Modal state for success and error
    const [isSuccessModalOpen, setIsSuccessModalOpen] = useState(false);
    const [isErrorModalOpen, setIsErrorModalOpen] = useState(false);
    const [errorMessage, setErrorMessage] = useState("");

    // Handle the form submission
    const handleSubmit = async (e) => {
        e.preventDefault();

        // Validate required fields
        const requiredFields = [
            firstname, lastname, position, email, employeddate, postcode, street, city, country, role, image
        ];

        // Check if any required field is empty
        if (requiredFields.some(field => !field)) {
            // If any required field is empty, set error message and open error modal
            setErrorMessage("Please fill in all fields");
            setIsErrorModalOpen(true);
            return;
        };

        // If all required fields are filled, proceed to add the employee and address
        try {
            // Create a new address object
            const newAddress = {
                street: street,
                city: city,
                postcode: postcode,
                country: country
            };

            // Call addAddress function to add the address
            const response = await addAddress(newAddress);
            const addressnr = response.addressnr;
            // Check if addressnr is returned from the response
            if (!addressnr) {
                throw new Error("Failed to retrieve addressnr from the response");
            }

            // Create a new employee object with the provided details
            const newEmployee = {
                firstname: firstname,
                lastname: lastname,
                email: email,
                active: true,
                employeddate: employeddate,
                position: position,
                password: password,
                phone: phone,
                image: image,
                role: { rolenr: role },
                address: { addressnr: addressnr }
            };

            // Call addEmployee function to add the employee
            await addEmployee(newEmployee);

            setEmail("");
            setEmployeddate("");
            setFirstname("");
            setLastname("");
            setPosition("");
            setRole(null);
            setPostcode("");
            setCity("");
            setCountry("");
            setStreet("");
            setImage("");
            setPassword("");
            setPhone("");

            setIsSuccessModalOpen(true);

        } catch (error) { // Catch any errors during the API calls
            // Show error message in the error modal
            setErrorMessage("Failed to add employee. Please try again.");
            setIsErrorModalOpen(true);
        }
    };

    // Fetch roles from getEmployeeRoles
    const [roles, setRoles] = useState([]);

    // useEffect to fetch roles when the component mounts
    React.useEffect(() => {
        async function fetchRoles() {
            // Call the getEmployeeRoles function to fetch roles
            try {
                const rolesData = await getEmployeeRoles();
                setRoles(rolesData);
            } catch (error) {
                // Log error if fetching roles fails
                console.error("Failed to fetch roles:", error);
            }
        }
        fetchRoles();
    }, [getEmployeeRoles]);

    // Render access denied if not admin
    if (!authorisedUser || authorisedUser.role?.name !== "admin") {
        return (
            <div style={{ padding: "2em", color: "red", textAlign: "center" }}>
                Access denied. This page is only available to admin users.
            </div>
        );
    }

    // Handler for closing the success modal and navigating
    const handleSuccessModalClose = () => {
        // Close the success modal and navigate to the employees page
        setIsSuccessModalOpen(false);
        navigate('/employees');
    };

    return (
        <>
            <h1> Add a New Employee</h1>

            <div className='formDisplayContainer'>

                <form onSubmit={handleSubmit}>

                    <div className='formField'>
                        <label>Link to Profile Image:</label>
                        <input
                            type="text"
                            className='urlInputField'
                            onChange={(e) => setImage(e.target.value)}
                            value={image}
                            placeholder='Enter image URL'
                            name='image' />
                    </div>

                    <div className='twoFieldsHolder'>

                        <div className='formField'>
                            <label>First Name: </label>
                            <input
                                type="text"
                                value={firstname}
                                onChange={(e) => setFirstname(e.target.value)}
                                className='longInputField'
                                placeholder='Enter first name'
                            />
                        </div>

                        <div className='formField'>
                            <label>Last Name: </label>
                            <input
                                type="text"
                                value={lastname}
                                onChange={(e) => setLastname(e.target.value)}
                                className='longInputField'
                                placeholder='Enter last name'
                            />
                        </div>

                    </div>

                    <div className='twoFieldsHolder'>

                        <div className='formField'>
                            <label>Password: </label>
                            <input
                                type="password"
                                value={password}
                                className='longInputField'
                                onChange={(e) => setPassword(e.target.value)}
                                placeholder='Enter password'
                            />
                        </div>

                        <div className='formField'>
                            <label>Employeed Date: </label>
                            <input
                                type="date"
                                value={employeddate}
                                onChange={(e) => setEmployeddate(e.target.value)}
                                className='dateInput'
                            />
                        </div>

                    </div>

                    <div className='twoFieldsHolder'>

                        <div className='formField'>
                            <label>Email: </label>
                            <input
                                type="email"
                                value={email}
                                onChange={(e) => setEmail(e.target.value)}
                                className='longInputField'
                                placeholder='Enter email address'
                            />
                        </div>

                        <div className='formField'>
                            <label>Phone: </label>
                            <input
                                type="tel"
                                value={phone}
                                onChange={(e) => setPhone(e.target.value)}
                                className='longInputField'
                                placeholder='Enter phone number'
                            />

                        </div>

                    </div>

                    <div className='twoFieldsHolder'>

                        <div className='formField'>
                            <label>Position: </label>
                            <input
                                type="text"
                                value={position}
                                onChange={(e) => setPosition(e.target.value)}
                                className='longInputField'
                                placeholder='Enter position'
                            />
                        </div>

                        <div className='formField'>
                            <label>Role: </label>
                            <select
                                value={role || ""}
                                onChange={e => setRole(e.target.value)}
                                className='formSelector'
                            >
                                <option value="" disabled>Select role</option>
                                {roles && roles.length > 0 ? (
                                    roles.map((roleOption) => (
                                        <option
                                            key={roleOption.rolenr} value={roleOption.rolenr}>
                                            {roleOption.name}
                                        </option>
                                    ))
                                ) : (
                                    <option value="" disabled>Loading roles...</option>
                                )}
                            </select>
                        </div>
                    </div>

                    <div className='formField'>
                        <label>Street: </label>
                        <input
                            type="text"
                            value={street}
                            onChange={(e) => setStreet(e.target.value)}
                            className='longInputField'
                            placeholder='Enter street address'
                        />
                    </div>

                    <div className='twoFieldsHolder'>

                        <div className='formField'>
                            <label>Post Code: </label>
                            <input
                                type="text"
                                value={postcode}
                                onChange={(e) => setPostcode(e.target.value)}
                                className='shortInputField'
                                placeholder='Enter post code'
                            />
                        </div>

                        <div className='formField'>
                            <label>City: </label>
                            <input
                                type="text"
                                value={city}
                                onChange={(e) => setCity(e.target.value)}
                                className='shortInputField'
                                placeholder='Enter city'
                            />
                        </div>

                    </div>

                    <div className='formField'>
                        <label>Country: </label>
                        <input
                            type="text"
                            value={country}
                            onChange={(e) => setCountry(e.target.value)}
                            className='shortInputField'
                            placeholder='Enter country'
                        />
                    </div>

                    <button className='addBtn' type="submit">Add Employee</button>

                </form>

            </div>

            {/* Success Modal */}
            {isSuccessModalOpen && (
                <div className="t-modal">
                    <div className="t-modal-content">
                        <span className="t-close" onClick={handleSuccessModalClose}>
                            &times;
                        </span>
                        <h2>Employee Added</h2>
                        <p>The employee was successfully added.</p>
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

export default AddEmployeeForm;