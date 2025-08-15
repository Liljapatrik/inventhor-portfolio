/**
 * Auhthor: Patrick Lilja
 * Date: 4 March 2025
 */

import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';


function EditInventoryForm({ getProducts, updateProduct }) {
    const { id } = useParams();
    const productId = Number(id);
    const navigate = useNavigate();
    const [product, setProduct] = useState({});

    useEffect(() => {
        getProducts().then((products) => {
            console.log("Fetched products", products); // Debugging
            const foundProduct = products.find(p => p.id === productId); // Id matching
            if (foundProduct) {
                setProduct(foundProduct);
            } else {
                console.error("No product found with id:", id);
            }
        });
    }, [id, getProducts]);

    const handleChange = (e) => {
        const { name, value, type, files } = e.target;
        
        if (type === "file") {
            const file = files[0];
            if (file) {
                const imageUrl = URL.createObjectURL(file);
                setProduct((prevProduct) => ({
                    ...prevProduct,
                    product_picture: imageUrl,   // Förhandsvisning av bilden
                    product_image_file: file,    // Här lagras själva filen
                }));
            }
        } else {
            setProduct((prevProduct) => ({
                ...(prevProduct || {}),
                [e.target.name]: e.target.value
            }));
        }
    };
    
    

    const handleSubmit = (e) => {
        e.preventDefault();
    
        if (product) {
            const formData = new FormData();
            formData.append("name", product.name);
            formData.append("category", product.category);
            //formData.append("quantity", product.quantity);
            formData.append("unit", product.unit);
            formData.append("price", product.price);
            formData.append("description", product.description);
            //formData.append("supplier", product.supplier);
    
            // Om användaren har valt en ny bild, lägg till den i FormData
            if (product.product_image_file) {
                formData.append("product_picture", product.product_image_file);
            }
    
            updateProduct(formData); // Uppdatera produkten med FormData
            setTimeout(() => navigate('/inventory'), 100); // Navigera tillbaka efter uppdatering
        }
    };
    

    if (!product) return <p>Loading...</p>;

    return (
        <form onSubmit={handleSubmit}>
            <h1>Edit Product</h1>
            <div className='formField'>
                <label>Product Name:</label>
                <input type="text" name="name" value={product.name} onChange={handleChange} className='shortInputField' />
            </div>
            <div className='formField'>
                <label>Category:</label>
                <input type="text" name="category" value={product.category} onChange={handleChange} className='shortInputField' />
            </div>
            <div className='twoFieldsHolder'>
                {/*
                <div className='formField'>
                    <label>Quantity:</label>
                    <input type="text" name="quantity" value={product.quantity} onChange={handleChange} className='shortInputField' />
                </div>
                */}
                <div className='formField'>
                    <label>Unit:</label>
                    <input type="text" name="unit" value={product.unit} onChange={handleChange} className='shortInputField' />
                </div>
            </div>
            <div className='formField'>
                <label>Price:</label>
                <input type="text" name="price" value={product.price} onChange={handleChange} className='shortInputField' />
            </div>
            <div className='formField'>
                <label>Description:</label>
                <input type="text" name="description" value={product.description} onChange={handleChange} className='longInputField' style={{ 
                    height: '60px',  
                    padding: '10px 15px', 
                    borderRadius: '5px' 
        }}          />
            </div>
            {/*
            <div className='formField'>
                <label>Supplier:</label>
                <input type="text" name="supplier" value={product.supplier} onChange={handleChange} className='longInputField' />   
            <div className='formField'>
                <label>Supplier:</label>
                <input type="text" name="supplier" value={product.supplier} onChange={handleChange} className='longInputField' />
            </div>
            */}
            <div className='formField'>
                <label>Product Image:</label>
                <input type="file" accept="image/*" name="product_picture" onChange={handleChange} className='longInputField'/>
                {/*Showing the picture if it already exist*/}
                {product.product_picture && (
                    <div>
                        <p>Current Image:</p>
                        <img src={product.product_picture} alt="Product" width="100"/>
                    </div>
                )}
            </div>

            <button className="addBtn" type="submit">Save Changes</button>
        </form>
    );
}

export default EditInventoryForm;
