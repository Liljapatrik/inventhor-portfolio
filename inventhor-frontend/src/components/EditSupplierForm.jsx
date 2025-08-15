import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';

/**
 * EditSupplierForm component
 * 
 * Allows editing an existing supplier's details, including contact info and address.
 * Fetches the supplier based on URL parameter `id`, populates form fields,
 * and submits updates to the backend with proper authentication.
 * 
 * @Author Patrik Lilja
 */

function EditSupplierForm() {
    const { id } = useParams();
    const suppliernr = Number(id);
    const navigate = useNavigate();

    // Get employeeID från authorisedUser i localStorage
    const authorisedUserStr = localStorage.getItem("authorisedUser");
    const authorisedUser = authorisedUserStr ? JSON.parse(authorisedUserStr) : null;
    const employeenr = authorisedUser ? authorisedUser.employeenr : null;

    const [supplier, setSupplier] = useState({
        name: '',
        contact: '',
        email: '',
        phone: '',
        website: '',
        notes: '', 
        address: {
            street: '',
            city: '',
            postCode: '',
            country: ''
        }
    });

    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

   const getSuppliers = async () => {
        try {
            const response = await fetch('http://localhost:8080/suppliers', {
                headers: {
                    Authorization: `Bearer ${localStorage.getItem("access_token")}`
                }
            });
            if (!response.ok) throw new Error('Failed to fetch suppliers');
            return await response.json();
        } catch (error) {
            setError(error.message);
            return [];
        }
    };

    const updateSupplier = async (updatedSupplier) => {
        try {
            const response = await fetch(`http://localhost:8080/suppliers/${suppliernr}?employeenr=${employeenr}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    Authorization: `Bearer ${localStorage.getItem("access_token")}`
                },
                body: JSON.stringify(updatedSupplier)
            });

            if (!response.ok) {
                const contentType = response.headers.get('content-type');
                let errorMessage = 'Failed to update supplier';

                if (contentType && contentType.indexOf('application/json') !== -1) {
                    const errorData = await response.json();
                    errorMessage = errorData.message || errorMessage;
                } else {
                    errorMessage = await response.text();
                }

                alert(errorMessage);
                navigate('/suppliers');
                throw new Error(errorMessage);
            }

            return await response.json();

        } catch (error) {
            throw error;
        }
    };


    useEffect(() => {
        getSuppliers().then((suppliers) => {
            const foundSupplier = suppliers.find(s => s.suppliernr === suppliernr);
            if (foundSupplier) {
                setSupplier(foundSupplier);
            } else {
                setError('Supplier not found');
            }
        })
        .finally(() => setLoading(false));
    }, [suppliernr]);

    const handleChange = (e) => {
        const { name, value } = e.target;

        if (['street', 'city', 'postCode', 'country'].includes(name)) {
            setSupplier(prev => ({
                ...prev,
                address: {
                    ...prev.address,
                    [name]: value
                }
            }));
        } else {
            setSupplier(prev => ({
                ...prev,
                [name]: value
            }));
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await updateSupplier(supplier);
            navigate('/suppliers');
        } catch (error) {

        }
        
    };

    if (loading) return <p>Loading...</p>
    if (error) return <p style={{color: 'red' }}>Error: {error}</p>
    if (!supplier) return <p>Supplier not found</p>

    return (
        <form onSubmit={handleSubmit}>
            <h1>Edit Supplier</h1>
            <div className='formField'>
                <label>Company Name:</label>
                <input type="text" name="name" value={supplier.name} onChange={handleChange} className='shortInputField' />
            </div>
            <div className='formField'>
                <label>Contact Person:</label>
                <input type="text" name="contact" value={supplier.contact} onChange={handleChange} className='longInputField' />
            </div>
            <div className='twoFieldsHolder'>
                <div className='formField'>
                    <label>Website:</label>
                    <input type="text" name="website" value={supplier.website} onChange={handleChange}  className='longInputField' />
                </div>
                <div className='formField'>
                    <label>Phone:</label>
                    <input type="text" name="phone" value={supplier.phone} onChange={handleChange} className='shortInputField' />
                </div>
            </div>
            <div className='formField'>
                <label>Email:</label>
                <input type="text" name="email" value={supplier.email} onChange={handleChange}className='longInputField' />
            </div>
            <div className='formField'>
                <label>Street:</label>
                <input type="text" name="street" value={supplier.address.street} onChange={handleChange} className='longInputField' />
            </div>
            <div className='twoFieldsHolder'>
                <div className='formField'>
                    <label>City:</label>
                    <input type="text" name="city" value={supplier.address.city} onChange={handleChange} className='shortInputField' />
                </div>
                <div className='formField'>
                    <label>Post Code:</label>
                    <input type="text" name="postCode" value={supplier.address.postCode} onChange={handleChange} className='shortInputField' />
                </div>
            </div>
            <div className='formField'>
                <label>Country:</label>
                <input type="text" name="country" value={supplier.address.country} onChange={handleChange} className='shortInputField' />
            </div>
            <div className='formField'>
                <label>Additional Notes:</label>
                <input type="text" name="notes" value={supplier.notes} onChange={handleChange} className='longInputField' />
            </div>
            
            
            <button className="addBtn" type="submit">Save Changes</button>
        </form>
    );
}

export default EditSupplierForm;