import React, { useState } from 'react';
import './addSupplierForm.css'
import { useNavigate } from 'react-router-dom';

/**
 * A React component that renders a form for adding a new supplier.
 * It manages form state, validates input fields, restricts access to admin users,
 * and submits the supplier data to the backend API with proper authorization.
 * 
 * @author Patrik Lilja
 */

function AddSupplierForm({ addSupplier }) {
    // Declare a state variable to store information about the supplier
    // With an initial empty string value "set..."
    // to update the information state
    const [name, setName] = useState("");
    const [contact, setContact] = useState("");
    const [website, setWebsite] = useState("");
    const [phone, setPhone] = useState("");
    const [email, setEmail] = useState("");
    const [street, setStreet] = useState("");
    const [city, setCity] = useState("");
    const [postcode, setPostcode] = useState("");
    const [country, setCountry] = useState("");
    const [notes, setNotes] = useState("");

    const navigate = useNavigate();

    // Modal state for success and error
    const [isSuccessModalOpen, setIsSuccessModalOpen] = useState(false);
    const [isErrorModalOpen, setIsErrorModalOpen] = useState(false);
    const [errorMessage, setErrorMessage] = useState("");

    // Get employeeID från authorisedUser i localStorage
    const authorisedUserStr = localStorage.getItem("authorisedUser");
    const authorisedUser = authorisedUserStr ? JSON.parse(authorisedUserStr) : null;
    const employeenr = authorisedUser ? authorisedUser.employeenr : null;

    // Restrict access to admin users only
    if (!authorisedUser || authorisedUser.role?.name !== "admin") {
        return (
            <div style={{ padding: "2em", color: "red", textAlign: "center" }}>
                Access denied. This page is only available to admin users.
            </div>
        );
    }

    // Handle input change and automatically add http:// if missing
    const handleWebsiteChange = (e) => {
        let inputValue = e.target.value;
        if (inputValue && !inputValue.startsWith("http://") && !inputValue.startsWith("https://")) {
            if (inputValue.startsWith("www")) {
                // Add "http//" if "www" is at the beginning
                inputValue = "http://" + inputValue;
            }
        }
        // Update 
        setWebsite(inputValue);
    };

    // Handle the form submission
    const handleSubmit = async (e) => {
        e.preventDefault();

        // Checking if all the fields are filled
        const requiredFields = [
            name, contact, website, phone, email, notes, street, city, postcode, country
        ];

        if (requiredFields.some(field => !field)) {
            setErrorMessage("Please fill in all fields");
            setIsErrorModalOpen(true);
            return;
        }

        // Creating a object for the new supplier
        const newSupplier = {
            name,
            contact,
            website,
            phone,
            email,
            notes,
            address: {
                street,
                city,
                postcode: parseInt(postcode, 10),
                country,
            }
        };

        const token = localStorage.getItem("access_token");

        try {
            const response = await fetch(`http://localhost:8080/suppliers?employeenr=${employeenr}`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${token}` // ✅ Token tillagd här!
                },
                body: JSON.stringify(newSupplier)
            });

            if (!response.ok) {
                if (response.status === 403) {
                    throw new Error("You are not authorized to add suppliers.");
                }
                const errorText = await response.text();
                throw new Error(errorText || "Failed to add supplier");
            }


            setIsSuccessModalOpen(true);

        } catch (error) {
            console.error("Error adding supplier:", error);
            setIsErrorModalOpen(true);
        }
    };

    // Handler for closing the success modal and navigating
    const handleSuccessModalClose = () => {
        setIsSuccessModalOpen(false);
        navigate("/suppliers");
    };

    return (
        <>

            <form onSubmit={handleSubmit}>

                <h1> Add a New Supplier</h1>

                <div className='twoFieldsHolder'>

                    <div className='formField'>
                        <label>Company Name: </label>
                        <input
                            type="text"
                            value={name}
                            onChange={(e) => setName(e.target.value)}
                            className='shortInputField'
                        />
                    </div>

                    <div className='formField'>
                        <label>Contact Person: </label>
                        <input
                            type="text"
                            value={contact}
                            onChange={(e) => setContact(e.target.value)}
                            className='longInputField'
                        />
                    </div>

                </div>

                <div className='twoFieldsHolder'>

                <div className='formField'>
                    <label>Website: </label>
                    <input
                        type="url"
                        value={website}
                        onChange={handleWebsiteChange} 
                        placeholder="www.example.com"
                        required
                        className='longInputField'
                    />
                </div>
                <div className='formField'>
                    <label>Phone Number: </label>
                    <input 
                        type="tel"
                        value={phone}
                        onChange={(e) => setPhone(e.target.value)}
                        className='shortInputField'
                    />
                </div>

                </div>

                <div className='formField'>
                    <label>Email: </label>
                    <input
                        type="email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        className='longInputField'
                    />
                </div>

                <div className='formField'>
                    <label>Street: </label>
                    <input
                        type="text"
                        value={street}
                        onChange={(e) => setStreet(e.target.value)}
                        className='longInputField'
                    />
                </div>

                <div className='twoFieldsHolder'>

                    <div className='formField'>
                        <label>City: </label>
                        <input
                            type="text"
                            value={city}
                            onChange={(e) => setCity(e.target.value)}
                            className='shortInputField'
                        />
                    </div>
                    <div className='formField'>
                        <label>Post Code: </label>
                        <input
                            type="text"
                            value={postcode}
                            onChange={(e) => setPostcode(e.target.value)}
                            className='shortInputField'
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
                    />
                </div>

                <div className='formField'>
                    <label>Additional Notes: </label>
                    <textarea
                        value={notes}
                        onChange={(e) => setNotes(e.target.value)}
                        className='longInputField'
                    />
                </div>
                <button className='addBtn' type="submit">Add Supplier</button>
            </form>

            {/* Success Modal */}
            {isSuccessModalOpen && (
                <div className="t-modal">
                    <div className="t-modal-content">
                        <span className="t-close" onClick={handleSuccessModalClose}>
                            &times;
                        </span>
                        <h2>Supplier Added</h2>
                        <p>The supplier was successfully added.</p>
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

export default AddSupplierForm;