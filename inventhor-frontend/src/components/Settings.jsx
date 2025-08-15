/**
 * Author: Tatiana Fløisbonn
 * Date: february 2025
 * Description:
 * This component is responsible for displaying and managing the settings of an employee.
 * It allows the user to view and update their personal information, including email, phone number, name, position, and address.
 */

/**UseEffect and useState are imported from React to manage component state and side effects.
* UseEffect is used to fetch employee data when the component mounts.
* UseState is used to manage the employee data and success message state.
*/
import { useEffect, useState } from 'react';

function Settings({authorisedUser, getEmployeeForSettings, updateAuthenticatedEmployee, updateAddress}) {

    const [employee, setEmployee] = useState(null); // Initialize employee state to null
               
    useEffect(() => {
        console.log("Authorised User:", authorisedUser);
        if (authorisedUser && authorisedUser.email) {
            getEmployeeForSettings(authorisedUser.email) // Fetch employee data using the provided function
                .then(data => setEmployee(data))
                .catch(error => console.error("Error fetching employee data:", error));
            console.log("Employee data fetched:", employee);
        }
    }, []);

    // Function to handle form submission
    // This function updates the employee and address information when the form is submitted.
    const formSubmit = async (event) => {
        event.preventDefault();

        // Set the employee object with the updated values from the form
        const updatedEmployee = {
            employeenr: employee.employeenr,
            email: employee.email,
            password: employee.password,
            phone: employee.phone,
            firstname: employee.firstname,
            lastname: employee.lastname,
            position: employee.position,
            role: employee.role,
            active: employee.active,
            image: employee.image,
            employedDate: employee.employeddate
        };

        await updateAuthenticatedEmployee(updatedEmployee,employee.employeenr, employee.email);

        // Set the address object with the updated values from the form
        const updatedAddress = {
            postcode: employee.address.postcode,
            street: employee.address.street,
            city: employee.address.city,
            country: employee.address.country
        };

        await updateAddress(employee.address.addressnr, updatedAddress);

        //Pop up for success message
        setShowSuccessMessage(true);
        
        getEmployeeForSettings(employee.email).then((employee) => setEmployee(employee));

        // Prevent URL changes by ensuring no redirection occurs
        window.history.replaceState(null, "", "/settings");
    }

    // Function to handle changes in form fields
    // This function updates the employee state when form fields are changed.
    const handleChange = (event) => {
        // Destructure name and value from the event target
        const { name, value } = event.target;

        // Check if the field belongs to the address object
        if (["street", "postcode", "city", "country"].includes(name)) {
            // Update fields in the address object
            setEmployee((prevState) => ({
            // Spread the previous state to keep other fields intact
            ...prevState,
            address: {
                ...prevState.address,
                [name]: value, // Update the specific field in the address object
            },
            }));
        } else {
            // Update fields directly on selectedEmployee
            setEmployee((prevState) => ({
            ...prevState,
            // Update the specific field in the employee object
            [name]: value,
            }));
        }
    };

    // State to manage the visibility of the success message modal
    const [showSuccessMessage, setShowSuccessMessage] = useState(false);

    // Function to handle closing the success message modal
    const handleCloseSuccessMessage = () => {
        // Set the showSuccessMessage state to false to hide the modal
        setShowSuccessMessage(false);
    };

  return (
    <>
    
        <div className='settings-container'>
            <h1>Settings</h1>

            <form className='settings-form' onSubmit={formSubmit}>
                
                <div className='imageProfileContainer'>
                    <div className='imageContainer'>
                        { employee &&
                        
                            <img src={""+employee.image} />
                        }
                    </div>

                    <div className='addImageForm'>
                        <label>Change Profile Image:</label>
                        { employee &&
                            <input
                                type="text"
                                className='urlInputField'
                                value={employee.image}
                                onChange={handleChange}
                                name='image' />
                        }
                    </div>
                </div>

                <div className='formField'>
                    <label>Employee nr:</label>
                    { employee &&
                        <h6>{employee.employeenr}</h6>
                    }
                </div>

                <div className='twoFieldsHolder'>
                    <div className='formField'>
                        <label>Email:</label>
                            { employee &&
                                <input
                                    type="email"
                                    className='longInputField'
                                    value={employee.email}
                                    onChange={handleChange}
                                    name='email' />
                            }
                    </div>

                    <div className='formField'>
                        <label>Tlf.:</label>
                        { employee &&
                            <input
                                type="text"
                                className='shortInputField'
                                value={employee.phone}
                                onChange={handleChange}
                                name='tlf' />
                        }
                    </div>
                </div>

                <div className='twoFieldsHolder'>
                    <div className='formField'>
                        <label>First Name:</label>
                        { employee &&
                            <input
                                type="text"
                                className='longInputField'
                                value={employee.firstname}
                                onChange={handleChange}
                                name='firstname' />
                        }
                    </div>

                    <div className='formField'>
                        <label>Last Name:</label>
                        { employee &&
                            <input
                                type="text"
                                className='longInputField'
                                value={employee.lastname}
                                onChange={handleChange}
                                name='lastname' />
                        }
                    </div>
                </div>
                
                <div className='formField'>
                    <label>Position:</label>
                    { employee &&
                        <input
                            type="text"
                            className='longInputField'
                            value={employee.position}
                            onChange={handleChange}
                            name='position' 
                            readOnly />
                    }
                </div>

                <div className='formField'>
                    <label>Employeed Date:</label>
                    { employee &&
                        <input
                            type="text"
                            className='shortInputField'
                            value={employee.employeddate} 
                            readOnly/>
                    }
                </div>

                <div className='twoFieldsHolder'>
                    <div className='formField'>
                        <label>Street:</label>
                        { employee &&
                            <input
                                type="text"
                                className='longInputField'
                                value={employee.address.street}
                                onChange={handleChange}
                                name='street' />
                        }
                    </div>

                    <div className='formField'>
                    <label>Postal Code:</label>
                        { employee &&
                            <input
                                type="text"
                                className='shortInputField'
                                value={employee.address.postcode}
                                onChange={handleChange}
                                name='postcode' />
                        }
                    </div>

                </div>
                
                <div className='twoFieldsHolder'>

                    <div className='formField'>
                        <label>City:</label>
                        { employee &&
                            <input
                                type="text"
                                className='longInputField'
                                value={employee.address.city}
                                onChange={handleChange}
                                name='city' />
                        }
                    </div>

                    <div className='formField'>
                        <label>Country:</label>
                        { employee &&
                            <input
                                type="text"
                                className='longInputField'
                                value={employee.address.country}
                                onChange={handleChange}
                                name='country' />
                        }
                    </div>

                </div>

                <button className='addBtn' type='submit'>Save</button>

            </form>
        </div>
    
        {/* Success message modal */}                
        {showSuccessMessage && (
            <div className='t-modal'>
                <div className='t-modal-content'>
                    <span className='close' onClick={handleCloseSuccessMessage}> {/* Close button */ }
                        &times; {/* Close icon */}
                    </span>
                    <h2>Information changed!</h2>
                    <p>Information about employee is successfully changed!</p>
                    <button type='submit' onClick={handleCloseSuccessMessage}>
                        OK
                    </button>
                </div>
            </div>
        )}
    </>
  )
}

export default Settings;