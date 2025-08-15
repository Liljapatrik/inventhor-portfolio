import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import TotalSalesChart from './TotalSalesChart';
import TotalCustomersChart from './TotalCustomersChart';
import './report.css';
import { getReportData } from '../data/ServerData';

/**
 * @Author Steewen Dennis Chanavi Holden
 *
 * Report.jsx displays an overview report page for the application.
 *
 * Fetches report data from the backend/server.
 * Shows the most popular products for week, month, and year, including image and quantity sold.
 * Displays charts/graphs for total sales and total customers, based on the retrieved data.
 *
 * The components TotalSalesChart and TotalCustomersChart are used to display the graphs.
 * CSS styling is found in 'report.css'.
 */
function Report() {
    // State for storing report data and loading status
    const [reportData, setReportData] = useState(null);
    const [loading, setLoading] = useState(true);

    // useEffect runs once when the component mounts
    useEffect(() => {
        // Fetches report data from the server
        async function fetchReportData() {
            try {
                setLoading(true);
                const data = await getReportData(); // Fetch data from server
                setReportData(data); // Store data
                setLoading(false);
            } catch (error) {
                console.error('Error fetching report data:', error);
                setLoading(false);
            }
        }

        fetchReportData();
    }, []);

    // Show a loading message while data is being fetched
    if (loading) {
        return <div>Loading report data...</div>;
    }

    // Show a message if no report data is available
    if (!reportData) {
        return <div>No report data available</div>;
    }

    // Main UI
    return (
        <>
            {/* Popular products (week, month, year) */}
            <div className='trend-holder'>
                <h1>Report</h1>

                <div className='popular-products-container'>
                    {/* Most popular product this week */}
                    {reportData.weeklyPopularProduct && (
                        <div className='popular-product-column'>
                            <h3>Week Popular</h3>
                            <img
                                src={reportData.weeklyPopularProduct.productImage || 'placeholder.png'}
                                alt={reportData.weeklyPopularProduct.productName}
                                className='popular-product-picture'
                                style={{ width: '100px', height: '100px', objectFit: 'cover' }}
                            />
                            <h5>
                                <Link to={`/inventory/product-details/${reportData.weeklyPopularProduct.productnr}`}>
                                    {reportData.weeklyPopularProduct.productName}
                                </Link>
                            </h5>
                            <p>Sold: {reportData.weeklyPopularProduct.totalQuantitySold}</p>
                        </div>
                    )}

                    {/* Most popular product this month */}
                    {reportData.monthlyPopularProduct && (
                        <div className='popular-product-column'>
                            <h3>Month Popular</h3>
                            <img
                                src={reportData.monthlyPopularProduct.productImage || 'placeholder.png'}
                                alt={reportData.monthlyPopularProduct.productName}
                                className='popular-product-picture'
                                style={{ width: '100px', height: '100px', objectFit: 'cover' }}
                            />
                            <h5>
                                <Link to={`/inventory/product-details/${reportData.monthlyPopularProduct.productnr}`}>
                                    {reportData.monthlyPopularProduct.productName}
                                </Link>
                            </h5>
                            <p>Sold: {reportData.monthlyPopularProduct.totalQuantitySold}</p>
                        </div>
                    )}

                    {/* Most popular product this year */}
                    {reportData.yearlyPopularProduct && (
                        <div className='popular-product-column'>
                            <h3>Popular in {new Date().getFullYear()}</h3>
                            <img
                                src={reportData.yearlyPopularProduct.productImage || 'placeholder.png'}
                                alt={reportData.yearlyPopularProduct.productName}
                                className='popular-product-picture'
                                style={{ width: '100px', height: '100px', objectFit: 'cover' }}
                            />
                            <h5>
                                <Link to={`/inventory/product-details/${reportData.yearlyPopularProduct.productnr}`}>
                                    {reportData.yearlyPopularProduct.productName}
                                </Link>
                            </h5>
                            <p>Sold: {reportData.yearlyPopularProduct.totalQuantitySold}</p>
                        </div>
                    )}
                </div>
            </div>

            {/* Charts and graphs */}
            <div className='charts-holder'>
                <div className='chart-info mb-5'>
                    <h3>Total Sales</h3>
                    {/* Displays sales chart with monthly data */}
                    <TotalSalesChart salesData={reportData.monthlySalesData} />
                </div>

                <div className='chart-info mb-5'>
                    <h3>Total Customers</h3>
                    {/* Displays customer chart with monthly data */}
                    <TotalCustomersChart customerData={reportData.monthlyCustomerData} />
                </div>
            </div>
        </>
    );
}

export default Report;