import React from 'react';
import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { getWarehouses } from '../data/ServerData';

/**
 * @Author Tatiana Fløisbonn
 * @Author Steewen Dennis Chanavi Holden
 *
 * Displays a list of all warehouses in a table.
 *
 * Fetches all warehouses from the backend.
 * Shows warehouse ID, name (with clickable link), and address in a table format.
 * Handles loading state while fetching data.
 *
 * Used as the main overview page for warehouse management.
 */

function WarehousesList() {
    // State to hold the list of warehouses and loading state
    const [warehouseList, setWarehouse] = useState([]);
    const [loading, setLoading] = useState(true);

    // Fetch all warehouses from the backend
    useEffect(() => {
        async function fetchWarehouses() {
            try {
                setLoading(true);
                const data = await getWarehouses();
                console.log('Fetched warehouses:', data);
                setWarehouse(data);
            } catch (error) {
                console.error('Error fetching warehouses:', error);
            } finally {
                setLoading(false);
            }
        }

        fetchWarehouses();
    }, []);

    // Show a loading message while data is being fetched
    if (loading) {
        return <div>Loading warehouses...</div>;
    }

    return (
        <div className='warehouses-list-container'>
            <h1>Warehouses</h1>
            <div className="table-container">
                <table>
                    <thead>
                    <tr>
                        <th>Warehouse ID</th>
                        <th>Name</th>
                        <th>Address</th>
                    </tr>
                    </thead>
                    <tbody>
                    {/* Render each warehouse as a row in the table */}
                    {warehouseList.map(warehouse => (
                        <tr key={warehouse.warehousenr}>
                            <td>{warehouse.warehousenr}</td>
                            <td>
                                {/* Warehouse name is a link to its info page */}
                                <Link to={`/warehouses/warehouse-info/${warehouse.warehousenr}`}>
                                    {warehouse.name}
                                </Link>
                            </td>
                            <td>
                                {/* Show address if available */}
                                {warehouse.address ?
                                    `${warehouse.address.street}, ${warehouse.address.postcode}, ${warehouse.address.city}, ${warehouse.address.country}`
                                    : 'N/A'}
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </table>
            </div>
        </div>
    )
}

export default WarehousesList;