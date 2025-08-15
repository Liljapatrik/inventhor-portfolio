/**
 * Author: Tatiana Fløisbonn
 * Date: 3 February 2025
 * Description: Inventory component for displaying and managing products in the inventory.
 * It includes search and sort functionalities, and allows navigation to product details.
 * It also provides an option to add new products if the user is authorised as an admin.
 * This component fetches products from the server and displays them in a table format.
 * The products can be filtered by name and sorted by various attributes.
 */

import { useEffect, useState } from 'react';
import { useNavigate } from "react-router-dom";
import './inventory.css';

function Inventory({ getProducts, authorisedUser }) {

    const navigate = useNavigate();
    let [products, setProducts] = useState([]);

    // Fetch products when the component mounts
    useEffect(() => {
        getProducts().then(data => setProducts(data));
    }, [getProducts]);

    
    let [search, setSearch] = useState("")
    let [sort, setSort] = useState("id")

    // Handle search input change
    function onSearchChange(e) {
        setSearch(e.target.value)
    }

    // Handle sort selection change
    function onSortChange(e) {
        setSort(e.target.value)
    }

    // Get sorted and filtered products data
    function getSortedProductsData() {
        return products.filter((item) => {
            return item.name.toLowerCase().includes(search.toLowerCase())
        }).sort((a, b) => {
            if (sort === "category") {
                return (a.category.name + "").localeCompare(b.category.name + "")
            }
            else {
                return (a[sort] + "").localeCompare(b[sort] + "")
            }
        })
    }

    return (
        <>

            <h1>Inventory</h1>

            <div className="tableFunctions">

                <div className="tableSeachbar">
                    <i className="bi bi-search"></i>
                    <input className="tableSeachInput" type="text" placeholder="Search" onChange={onSearchChange} value={search}></input>
                </div>


                <div className="sortWithAddFunctions">
                    {/* Filter dropdown */}
                    <select className="tableSort" onChange={onSortChange} value={sort}>
                        <option value="id">Product ID</option>
                        <option value="name">Name</option>
                        <option value="category">Category</option>
                    </select>

                    {/* Add Product button, only for admin */}
                    {authorisedUser && authorisedUser.role.name === "admin" && (
                        <button className="addBtn" onClick={() => navigate("/inventory/add-product")}>
                            Add Product
                        </button>
                    )}
                </div>

            </div>

            <div className="table-container">
                <table>
                    <thead>
                        <tr>
                            <th>Productnr</th>
                            <th>Image</th>
                            <th>Name</th>
                            <th>Description</th>
                            <th>Category</th>
                            <th>Quantity</th>
                            <th>Unit</th>

                        </tr>
                    </thead>

                    <tbody>
                        {products.length > 0 &&
                            getSortedProductsData().map((item) => {
                                return <tr key={item.productnr}>
                                    <td>{item.productnr}</td>
                                    <td><img src={"" + item.image} alt={item.name} /></td>
                                    <td><a href={"/inventory/product-details/" + item.productnr}>{item.name}</a></td>
                                    <td>{item.description}</td>
                                    <td>{item.category.name}</td>
                                    <td>{item.quantity}</td>
                                    <td>{item.unit}</td>
                                </tr>
                            })
                        }
                    </tbody>
                </table>

            </div>

        </>
    )
}

export default Inventory;