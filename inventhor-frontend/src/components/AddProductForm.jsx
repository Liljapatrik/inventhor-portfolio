/**
 * Author: Tatiana Fløisbonn og Patrik Lilja
 * Date: March 2025
 * Description: This component provides a form for adding new products to the inventory.
 * It includes fields for product details, category selection, and supplier selection.
 * The form validates input fields and displays success or error messages in modals.
 * It also allows admins to add new categories directly from the form.
 */


import React, { useState, useEffect } from 'react';
import './addProductForm.css';
import { employees } from '../data/mockData';

function AddProductForm({ addProduct, getCategories, getSuppliers, addCategory, authorisedUser }) {

    // State variables for form fields
    const [name, setName] = useState("");
    const [description, setDescription] = useState("");
    const [width, setWidth] = useState("");
    const [height, setHeight] = useState("");
    const [depth, setDepth] = useState("");
    const [weight, setWeight] = useState("");
    const [sellprice, setSellprice] = useState("");
    const [unit, setUnit] = useState("");
    const [image, setImage] = useState("");
    const [categorynr, setCategorynr] = useState("");
    const [categories, setCategories] = useState([]);
    const [suppliers, setSuppliers] = useState([]);
    const [supplier, setSupplier] = useState("");
    const [showCategoryPopup, setShowCategoryPopup] = useState(false);
    const [newCategory, setNewCategory] = useState("");

    // Modal state for success and error
    const [isSuccessModalOpen, setIsSuccessModalOpen] = useState(false);
    const [isErrorModalOpen, setIsErrorModalOpen] = useState(false);
    const [modalMessage, setModalMessage] = useState("");

    // Fetch suppliers and categories on component mount
    useEffect(() => {
        async function fetchSuppliers() {
            try {
                const data = await getSuppliers();
                setSuppliers(data);
            } catch (error) {
                console.error("Error fetching suppliers:", error);
            }
        }
        fetchSuppliers();
    }, [getSuppliers]);

    useEffect(() => {
        async function fetchCategories() {
            try {
                const data = await getCategories();
                setCategories(data);
            } catch (error) {
                console.error("Error fetching categories:", error);
            }
        }
        fetchCategories();
    }, [getCategories]);

    // Render access denied if not admin
    if (!authorisedUser || authorisedUser.role?.name !== "admin") {
        return (
            <div style={{ padding: "2em", color: "red", textAlign: "center" }}>
                Access denied. This page is only available to admin users.
            </div>
        );
    }

    // Handle form submission
    const handleSubmit = async (e) => {
        e.preventDefault();

        // Validate required fields
        if (
            !name.trim() ||
            !description.trim() ||
            !width.trim() ||
            !height.trim() ||
            !depth.trim() ||
            !weight.trim() ||
            !sellprice.trim() ||
            !unit.trim() ||
            !categorynr ||
            !supplier
        ) {
            // Show error modal if any required field is empty
            setModalMessage("Please fill in all fields");
            setIsErrorModalOpen(true);
            return;
        }

        // Parse numbers
        const parsedSellprice = parseFloat(sellprice);
        const parsedWidth = parseFloat(width);
        const parsedHeight = parseFloat(height);
        const parsedDepth = parseFloat(depth);
        const parsedWeight = parseFloat(weight);

        if (
            isNaN(parsedSellprice) || parsedSellprice <= 0 ||
            isNaN(parsedWidth) || parsedWidth <= 0 ||
            isNaN(parsedHeight) || parsedHeight <= 0 ||
            isNaN(parsedDepth) || parsedDepth <= 0 ||
            isNaN(parsedWeight) || parsedWeight <= 0
        ) {
            // Show error modal if any numeric field is invalid
            setModalMessage("Please enter valid numeric values for price, width, height, depth, and weight");
            setIsErrorModalOpen(true);
            return;
        }
        // Create new product object
        const newProduct = {
            name: name,
            description: description,
            width: parsedWidth,
            height: parsedHeight,
            depth: parsedDepth,
            weight: parsedWeight,
            sellprice: parsedSellprice,
            unit: unit,
            image: image,
            category: {
                categorynr: categorynr
            },
            suppliernr: supplier,
            employeenr: authorisedUser.employeenr
        };

        // Call addProduct function to save the new product
        try {
            await addProduct(newProduct);
            setModalMessage("Product added successfully!");
            setIsSuccessModalOpen(true);
            // Clear fields
            setName("");
            setDescription("");
            setWidth("");
            setHeight("");
            setDepth("");
            setWeight("");
            setSellprice("");
            setUnit("");
            setImage("");
            setCategorynr("");
            setSupplier("");
        } catch (error) {
            // Handle error during product addition
            setModalMessage("Failed to add product. Please try again.");
            setIsErrorModalOpen(true);
        }
    };

    const handleSuccessModalClose = () => {
        setIsSuccessModalOpen(false);
        setModalMessage('');
    };

    // Handler for closing error modal
    const handleErrorModalClose = () => {
        setIsErrorModalOpen(false);
        setModalMessage('');
    };

    // Handler for adding a new category
    const handleAddCategory = async () => {
        // Validate new category input
        if (newCategory.trim() === "") {
            // Show error modal if category name is empty
            setModalMessage("Please enter a category name");
            setIsErrorModalOpen(true);
            return;
        }
        // Check if category already exists
        try {
            await addCategory({
                name: newCategory
            });
            setShowCategoryPopup(false);
            setNewCategory("");
            const updatedCategories = await getCategories();
            setCategories(updatedCategories);
            // Show success modal after adding category
            setModalMessage("Category added successfully!");
            setIsSuccessModalOpen(true);
        } catch (error) { // Handle error during category addition
            setModalMessage("Failed to add category");
            setIsErrorModalOpen(true);
        }
    };

    return (
        <>
            <form onSubmit={handleSubmit}>
                <h1>Add a New Product</h1>

                <div className='formField'>
                    <label>Name:</label>
                    <input type="text" value={name} onChange={e => setName(e.target.value)} className='shortInputField' />
                </div>

                <div className='twoFieldsHolder'>
                    <div className='formField'>
                        <label>Category:</label>
                        <select
                            value={categorynr}
                            onChange={e => setCategorynr(e.target.value)}
                            className='shortInputField'
                        >
                            <option value="">Select category</option>
                            {categories.map((cat) => (
                                <option key={cat.categorynr} value={cat.categorynr}>{cat.name}</option>
                            ))}
                        </select>
                    </div>
                    <div className='formField'>
                        <button
                            className='addBtn mt-4' // Button to open category popup
                            type="button"
                            onClick={() => setShowCategoryPopup(true)}
                            style={{ marginLeft: 8 }} // Add button to open category popup
                        >
                            Add category
                        </button>
                    </div>
                </div>

                {showCategoryPopup && (
                    <div className="popup-overlay">
                        <div className="popup-content">
                            <h3>Add New Category</h3>
                            <input
                                type="text"
                                value={newCategory}
                                onChange={(e) => setNewCategory(e.target.value)}
                                placeholder="Category name"
                                className='longInputField'
                            />
                            <div style={{ marginTop: 10 }}>
                                <button className='addBtn' type="button" onClick={handleAddCategory}>Save</button>
                                <button
                                    className='addBtn'
                                    type="button"
                                    onClick={() => setShowCategoryPopup(false)}
                                    style={{ marginLeft: 8 }} // Button to close category popup. 
                                >
                                    Cancel
                                </button>
                            </div>
                        </div>
                    </div>
                )}

                <div className='formField'>
                    <label>Supplier:</label>
                    <select
                        value={supplier}
                        onChange={e => setSupplier(e.target.value)}
                        className='shortInputField'
                    >
                        <option value="">Select supplier</option>
                        {suppliers.map((sup) => (
                            <option key={sup.suppliernr} value={sup.suppliernr}>{sup.name}</option>
                        ))}
                    </select>
                </div>

                <div className='formField'>
                    <label>Description:</label>
                    <textarea value={description} onChange={e => setDescription(e.target.value)} className='longInputField' />
                </div>

                <div className='twoFieldsHolder'>
                    <div className='formField'>
                        <label>Width:</label>
                        <input type="number" value={width} onChange={e => setWidth(e.target.value)} className='shortInputField' min="0" step="any" />
                    </div>
                    <div className='formField'>
                        <label>Height:</label>
                        <input type="number" value={height} onChange={e => setHeight(e.target.value)} className='shortInputField' min="0" step="any" />
                    </div>
                </div>

                <div className='twoFieldsHolder'>
                    <div className='formField'>
                        <label>Depth:</label>
                        <input type="number" value={depth} onChange={e => setDepth(e.target.value)} className='shortInputField' min="0" step="any" />
                    </div>
                    <div className='formField'>
                        <label>Weight:</label>
                        <input type="number" value={weight} onChange={e => setWeight(e.target.value)} className='shortInputField' min="0" step="any" />
                    </div>
                </div>

                <div className='twoFieldsHolder'>
                    <div className='formField'>
                        <label>Price:</label>
                        <input type="number" value={sellprice} onChange={e => setSellprice(e.target.value)} className='longInputField' min="0" step="any" />
                    </div>
                    <div className='formField'>
                        <label>Unit:</label>
                        <input type="text" value={unit} onChange={e => setUnit(e.target.value)} className='shortInputField' />
                    </div>
                </div>

                <div className='formField'>
                    <label>Product Image:</label>
                    <input
                        type="text"
                        name="image"
                        value={image}
                        onChange={(e) => setImage(e.target.value)}
                        className='longInputField'
                    />
                </div>

                <button className='addBtn' type="submit">Add Product</button>
            </form>

            {/* Success Modal */}
            {isSuccessModalOpen && (
                <div className="t-modal">
                    <div className="t-modal-content">
                        <span className="t-close" onClick={handleSuccessModalClose}>
                            &times;
                        </span>
                        <h2>Success</h2>
                        <p>{modalMessage}</p>
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
                        <h2>Error</h2>
                        <p>{modalMessage}</p>
                        <button className="t-deleteErrorBtn" onClick={handleErrorModalClose}>
                            Close
                        </button>
                    </div>
                </div>
            )}
        </>
    );
}

export default AddProductForm;