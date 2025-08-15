/**
 * Author: Tatiana Fløisbonn
 * Date: 1 February 2025
 * Description: ProductDetails component for displaying detailed information about a product.
 * It includes product information, suppliers, warehouse inventory, and charts for price and selling trends.
 */

import { useEffect, useState } from 'react';
import PriceTrendChart from './PriceTrendChart';
import SellingTrendChart from './SellingTrendChart';
import ProductDetailsInventory from './ProductDetailsInventory';
import './productDetails.css';
import { useParams } from 'react-router-dom';

function ProductDetails({ getProductByNr, getSuppliersByProductnr, getLocationProductsByProductnr, getPriceHistoryByProductnr, getSellingHistoryByProduct }) {

    // Extract the product number from the URL parameters
    const { id } = useParams();

    const [product, setProduct] = useState(null);
    const [suppliers, setSuppliers] = useState([]);

    // Fetch the product details when the component mounts or when the id changes
    useEffect(() => {
        getProductByNr(id).then(data => setProduct(data));
    }, [getProductByNr]);

    // Fetch the suppliers for the product when the product changes
    useEffect(() => {
        if (product && product.productnr) {
            getSuppliersByProductnr(product.productnr).then(data => {
                setSuppliers(data);
                console.log(data);
            });

        }

    }, [product, getSuppliersByProductnr]);


    return (
        <div className="product-details">

            {product != null &&
                <div className='product-header'>
                    <div className='general-info'>
                        <h1>{product.name}</h1>
                        <section className='price-info'>
                            <h4>Price:</h4>
                            <h5>{product.sellprice}</h5>
                        </section>
                    </div>

                    <div className='picture'>
                        <section>
                            <img src={product.image}></img>
                        </section>
                    </div>
                </div>
            }

            <div className='product-supplier'>
                <h3>Suppliers</h3>
                <div className="table-container">
                    <table>
                        <thead>
                            <tr>
                                <th>Supplier Name</th>
                                <th>Contact Person</th>
                                <th>Phone</th>
                            </tr>
                        </thead>
                        <tbody>
                            {suppliers && suppliers.map(supplier => (
                                <tr key={supplier.supplier.suppliernr}>
                                    <td>
                                        <a href={`/suppliers/info/${supplier.supplier.suppliernr}`}>
                                            {supplier.supplier.name}
                                        </a>
                                    </td>
                                    <td>
                                        <a href={`/suppliers/info/${supplier.supplier.suppliernr}`}>
                                            {supplier.supplier.contact}
                                        </a>
                                    </td>
                                    <td>
                                        <a href={`/suppliers/info/${supplier.supplier.suppliernr}`}>
                                            {supplier.supplier.phone}
                                        </a>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
            </div>

            <div className='product-location'>

                <h3>Warehouse Inventory</h3>
                <div className="table-container">
                    <ProductDetailsInventory getLocationProductsByProductnr={getLocationProductsByProductnr} />
                </div>

            </div>

            <div className='charts-holder'>


                <div className='chart-info mb-5'>

                    <h3>Selling Change</h3>

                    <SellingTrendChart getSellingHistoryByProduct={getSellingHistoryByProduct} />


                </div>

                <div className='chart-info mb-5'>

                    <h3>Buy Price Trend</h3>

                    <PriceTrendChart getPriceHistoryByProductnr={getPriceHistoryByProductnr} />


                </div>

            </div>


        </div>

    )
}

export default ProductDetails;