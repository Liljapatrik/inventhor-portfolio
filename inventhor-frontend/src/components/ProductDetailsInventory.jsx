/**
 * Author: Tatiana Fløisbonn
 * Date: 3 February 2025
 * Description: ProductDetailsInventory component for displaying the inventory details of a product.
 * It fetches the product's inventory information from a warehouse based on the product number
 * and displays it in a table format.
 */

import React, { useEffect, useState } from 'react';

import { useParams } from 'react-router-dom';

function ProductDetailsInventory({ getLocationProductsByProductnr }) {

    // Extract the product number from the URL parameters
    const { id } = useParams();

    const [warehouseProductInfo, setWarehouseProductInfo] = useState([]);

    // Fetch the warehouse product information when the component mounts or when the id changes
    useEffect(() => {
        console.log('Fetching warehouse product info for productnr:', id);
        async function fetchData() {
            // Check if id is defined before making the API call
            try {
                const data = await getLocationProductsByProductnr(id);
                console.log('Fetched warehouse product info:', data);
                setWarehouseProductInfo(data);
            } catch (error) { // Handle any errors that occur during the fetch
                console.error('Failed to fetch warehouse product info:', error);
            }
        }
        if (id) {
            fetchData();
        }
    }, [id, getLocationProductsByProductnr]);

    return (
        <table>
            <thead>
                <tr key={warehouseProductInfo.warehousenr}>
                    <th>Warehouse</th>
                    <th>Quantity</th>
                    <th>Location</th>
                </tr>
            </thead>
            <tbody>
                {warehouseProductInfo.map((info, idx) => (
                    <tr
                        key={
                            [
                                info.warehousenr,
                                info.racknr,
                                info.placenr,
                                info.productnr
                            ].every(val => val !== undefined && val !== null)
                                ? `${info.warehousenr}-${info.racknr}-${info.placenr}-${info.productnr}`
                                : `row-${idx}`
                        }
                    >
                        <td>
                            <a href={'/warehouses/warehouse-info/' + info.warehouse.warehousenr}>
                                {info.warehouse?.name ?? 'Unknown'}
                            </a>
                        </td>
                        <td>{info.quantity}</td>
                        <td>R{info.racknr}-P{info.placenr}</td>
                    </tr>
                ))}
            </tbody>
        </table>
    );
}

export default ProductDetailsInventory;