import React, { useState, useEffect } from 'react';
import { useParams } from "react-router-dom";

/**
 * WarehouseOrdersProduct component fetches and displays the list of products
 * related to a specific warehouse order identified by the URL parameter `ordernr`.
 * 
 * Features include:
 * - Fetching product details of a warehouse order from backend with authentication.
 * - Searching products by name.
 * - Sorting products by selected column (product number, name, or category).
 * - Displaying product details in a sortable and searchable table.
 * - Linking each product name to its detailed inventory page.
 * 
 * Uses React Hooks for state management and React Router's useParams to get the order number.
 * 
 *  @Author Patrik Lilja 
 * 
 */

function WarehouseOrdersProduct() {
    const [products, setProducts] = useState([]);
    const [ warehouseOrderProduct, setarehouseOrderProduct] = useState(null);
    const { ordernr } = useParams();


    let [search, setSearch] = useState("")
    let [sort, setSort] = useState("name")
    let [order, setOrder] = useState([])

    async function getOrder() {
        const token = localStorage.getItem("access_token");  

        try {
            const response = await fetch(`http://localhost:8080/warehouse-order-product/${ordernr}`, {
                method: "GET",
                headers: {
                    "Authorization": `Bearer ${token}`,  
                    "Content-Type": "application/json"  
                }
            });

            if (!response.ok) {
                throw new Error("Order not found");
            }

            const data = await response.json();
            return data;

    } catch (error) {
        console.error(error);
        alert("Could not fetch order: " + error.message);
        return [];
    }
}
        
    function onSearchChange(e) {
        setSearch(e.target.value)
    }
    
    function onSortChange(e) {
        setSort(e.target.value)
    }
    
    function getSortedOrderData() {
        return order
            .filter((item) => {
                return item.name && item.name.toLowerCase().includes(search.toLowerCase());
             })
            .sort((a, b) => {
                const valA = (a[sort] || "").toString();
                const valB = (b[sort] || "").toString();
                return (a[sort] + "").localeCompare(b[sort] + "");
            });
    }


    useEffect(() => {
          getOrder().then(setOrder);
        }, []); // Get the new list if it is any changes


    return (
        <>
            <div className='listProductsFromSupplier'>
                <h3>Product Details In Order</h3>

                <div className="tableFunctions">


                    <div className="tableSeachbar">
                        <i class="bi bi-search"></i>
                        <input class="tableSeachInput" type="text" placeholder="Search" onChange={onSearchChange} value={search}></input>
                    </div>

                    
                    <div className="sortWithAddFunctions">
                        <select className="tableSort" onChange={onSortChange} value={sort}>
                            <option value="productnr">Order nr.</option>
                            <option value="productname">Product</option>
                            <option value="productCategory">Orderdate</option>
                        </select>
                    </div>
                </div>

                <div className="table-container">
                    <table>
                        <thead>
                            <tr>
                                <th>Product name</th>
                                <th>Product nr</th>
                                <th>Quantity</th>
                                <th>Buyprice</th>
                            </tr>
                        </thead>

                        <tbody>
                            {order.length > 0 &&
                                getSortedOrderData().map((item) => (
                                    <tr key={`${item.ordernr}-${item.productnr}`}>
                                        <td><a href={`/inventory/product-details/${item.productnr}`}>{item.name}</a></td>
                                        <td>{item.productnr}</td>
                                        <td>{item.quantity}</td>
                                        <td>{item.buyprice}</td>
                                    </tr>
                                ))
                            }
                        </tbody>
                    </table>
                </div>
            </div>
        </>
  )

}

export default WarehouseOrdersProduct;