import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { getSuppliers, getSuppliersProduct } from '../data/dataFetching';
import './supplierInfo.css';

/**
 * SupplierInfo component
 * 
 * Displays detailed information about a specific supplier including contact details,
 * address, and a list of products provided by the supplier.
 * Fetches supplier data and products based on the URL parameter `suppliernr`.
 * Allows searching and sorting of the supplier's products.
 * Provides an option to add a new product if the user has admin rights.
 * 
 * @Author Patrik Lilja
 */

function SupplierInfo({ authorisedUser }) {
    const { suppliernr } = useParams();
    const [supplierInfo, setSupplierInfo] = useState(null);
    const [address, setAddress] = useState(null);
    const [products, setProducts] = useState([]);
    const navigate = useNavigate();

    useEffect(() => {
        fetchSupplier(suppliernr);
        console.log("Suppliernr in useEffect:", suppliernr);
        fetchProductsBySupplier(suppliernr);
    }, [suppliernr]);


    async function fetchSupplier(suppliernr) {
        try {
            const response = await fetch(`http://localhost:8080/suppliers/${suppliernr}`,
                {
                    headers: {
                        Authorization: `Bearer ${localStorage.getItem("access_token")}`
                    }
                }
            );
            if (!response.ok) throw new Error('Failed to fetch supplier');
            const data = await response.json();
            setSupplierInfo(data);
        } catch (error) {
            console.error(error);
        }
    }

    async function fetchProductsBySupplier(suppliernr) {
        try {
            const response = await fetch(`http://localhost:8080/product-suppliers/products-by-supplier/${suppliernr}`,
                {
                    headers: {
                        Authorization: `Bearer ${localStorage.getItem("access_token")}`
                    }
                }
            );
            if (!response.ok) throw new Error('Failed to fetch products');
            const data = await response.json();
            console.log('Products by supplier:', data);
            setProducts(data);
        } catch (error) {
            console.error(error);
        }
    }

    let [search, setSearch] = useState("")
    let [sort, setSort] = useState("productname")

    function onSearchChange(e) {
        setSearch(e.target.value)
    }

    function onSortChange(e) {
        setSort(e.target.value)
    }

    function getSortedProductsData() {
        return products.filter((item) => {
            return item.productname.toLowerCase().includes(search.toLowerCase())
        }).sort((a, b) => {
            return (a[sort] + "").localeCompare(b[sort] + "")
        })
    }


    return (
        <>
            <div className='generalSupplierInfo'>
                {supplierInfo && (
                    <>
                        <h1>{supplierInfo.name}</h1>

                        <div className='generalInfoBox'>

                            <div className='supplierInfoBox'>
                                <h4>Contact Person:</h4>
                                <p>{supplierInfo.contact}</p>
                            </div>

                            <div className='supplierInfoBox'>
                                <h4>Phone:</h4>
                                <p>{supplierInfo.phone}</p>
                            </div>

                            <div className='supplierInfoBox'>
                                <h4>Email:</h4>
                                <p>{supplierInfo.email}</p>
                            </div>

                            <div className='supplierInfoBox'>
                                <h4>Website</h4>
                                <p>{supplierInfo.website}</p>
                            </div>

                            <div className='supplierInfoBox'>
                                <h4>Address</h4>
                                <p>{supplierInfo.address.street}, {supplierInfo.address.postcode}, {supplierInfo.address.city}, {supplierInfo.address.country}</p>
                            </div>

                        </div>
                    </>
                )}
            </div>

            <div className='listProductsFromSupplier'>
                <h3>Provided products</h3>

                <div className="tableFunctions">


                    <div className="tableSeachbar">
                        <i class="bi bi-search"></i>
                        <input class="tableSeachInput" type="text" placeholder="Search" onChange={onSearchChange} value={search}></input>
                    </div>


                    <div className="sortWithAddFunctions">
                        
                        <select className="tableSort" onChange={onSortChange} value={sort}>
                            <option value="productnr">Product ID</option>
                            <option value="productname">Name</option>
                            <option value="productCategory">Category</option>
                        </select>

                        
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
                                <th>Product number</th>
                                <th>Image</th>
                                <th>Name</th>
                                <th>Category</th>

                            </tr>
                        </thead>

                        <tbody>
                            {products.length > 0 &&
                                getSortedProductsData().map((item) => {
                                    return <tr key={item.productnr}>
                                        <td>{item.productnr}</td>
                                        <td>
                                            <img src={item.image} alt={item.productname} style={{ width: "50px", height: "50px" }} />
                                        </td>
                                        <td><a href={"/inventory/product-details/" + item.productnr}>{item.productname}</a></td>
                                        <td>{item.productCategory}</td>
                                    </tr>
                                })
                            }
                        </tbody>
                    </table>
                </div>
            </div>
        </>
    )
}

export default SupplierInfo;