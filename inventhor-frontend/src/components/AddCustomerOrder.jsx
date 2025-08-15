/**
 * Author: Tatiana Fløisbonn
 * Date: 9 June 2025
 * 
 * Description: Component for adding a new customer order.
 * This component allows users to create a new order by selecting products from warehouses,
 * entering customer information, and specifying payment details.
 * It includes functionality to search for existing customers by email,
 * add multiple products to the order, and handle payment methods.
 * It also provides feedback on the order creation process through a modal dialog.
 * 
 * Note: This component is only accessible to users with admin privileges.
 */

import React, { useState } from 'react';
import './addCustomerOrder.css';

function AddCustomerOrder({ getWarehouses, getCustomerPayments, getLocationProductsByWarehouse, getCustomerByEmail, addCustomerOrder, getPaymentMethods, authorisedUser }) {
    // All hooks must be called unconditionally
    const [orderProducts, setOrderProducts] = useState([
        { warehouse: '', product: '', quantity: '', available: null }
    ]);
    const [customer, setCustomer] = useState({
        email: '', phone: '', firstname: '', lastname: '', country: '', city: '', street: '', postcode: ''
    });
    const [paymentType, setPaymentType] = useState('now');
    const [payment, setPayment] = useState({ paymentmethod: '', paymentdate: '' });
    const [products, setProducts] = useState([]);
    const [warehouses, setWarehouses] = useState([]);
    const [paymentMethods, setPaymentMethods] = useState([]);
    const [customerFound, setCustomerFound] = useState(null);
    const [isSearching, setIsSearching] = useState(false);
    
    const [isOrderModalOpen, setIsOrderModalOpen] = useState(false);
    const [orderModalSuccess, setOrderModalSuccess] = useState(true);
    const [orderModalMessage, setOrderModalMessage] = useState('');

    // Fetch warehouses and payment methods on component mount
    React.useEffect(() => {
        async function fetchData() {
            const whs = await getWarehouses();
            setWarehouses(whs);
            const pms = await getPaymentMethods();
            setPaymentMethods(pms);
        }
        fetchData();
    }, [getWarehouses, getCustomerPayments, getPaymentMethods]);

    // Fetch products for the first warehouse on initial load
    const getProductsForWarehouse = (warehousenr) => {
        if (!warehousenr) return [];
        getLocationProductsByWarehouse(warehousenr).then(products => {
            setProducts(products);
        });
        return [];
    };

    // Get available quantity for a product in a specific warehouse. If there are multiple locations with the same product in the warehouse, it sums their quantities.
    const getAvailableQuantity = (warehousenr, productnr) => {
        if (!warehousenr || !productnr) return null;
        return products
            .filter(p => (p.product?.productnr ?? p.productnr) === Number(productnr))
            .reduce((sum, p) => sum + Number(p.quantity), 0); // Sum quantities of all locations with the same product
    };

    // Handle changes in order product fields
    const handleOrderProductChange = (idx, field, value) => {
        // Update the specific field in the orderProducts array
        const updated = [...orderProducts];
        updated[idx][field] = value;
        // If warehouse or product changes, update available quantity and reset quantity
        if (field === 'warehouse' || field === 'product') {
            const wh = field === 'warehouse' ? value : updated[idx].warehouse;
            const pr = field === 'product' ? value : updated[idx].product;
            updated[idx].available = getAvailableQuantity(wh, pr);
            updated[idx].quantity = '';
            getProductsForWarehouse(wh);
        }
        setOrderProducts(updated);
    };

    // Handle quantity change for a specific product
    const handleQuantityChange = (idx, value) => {
        const updated = [...orderProducts];
        updated[idx].quantity = value;
        setOrderProducts(updated);
    };

    // Add a new product block to the order
    const addProductBlock = () => {
        setOrderProducts([...orderProducts, { warehouse: '', product: '', quantity: '', available: null }]);
    };

    // Remove a product block from the order
    const removeProductBlock = (idx) => {
        setOrderProducts(orderProducts.filter((_, i) => i !== idx));
    };

    // Get unique products from the orderProducts array to avoid duplicates in the product dropdown
    const getUniqueProducts = (products) => {
        const seen = new Set();
        return products.filter(p => {
            const prodNr = p.product?.productnr ?? p.productnr;
            if (seen.has(prodNr)) return false;
            seen.add(prodNr);
            return true;
        });
    };

    // Handle customer search by email
    const handleFindCustomer = async () => {
        setIsSearching(true);
        // Reset customer found state
        try {
            const found = await getCustomerByEmail(customer.email);
            if (found) {
                setCustomer({
                    email: found.email || customer.email,
                    phone: found.phone || '',
                    firstname: found.firstname || '',
                    lastname: found.lastname || '',
                    country: found.country || '',
                    city: found.city || '',
                    street: found.street || '',
                    postcode: found.postcode || ''
                });
                setCustomerFound(true);
            } else { // If customer not found, reset customer state
                setCustomer({
                    ...customer,
                    phone: '', firstname: '', lastname: '', country: '', city: '', street: '', postcode: ''
                });
                setCustomerFound(false);
            }
        } catch (err) {
            // If there is an error during the search, reset customer state
            setCustomerFound(false);
        }
        setIsSearching(false);
    };

    // Handle form submission to create a new order
    const handleSubmit = async (e) => {
        // Prevent default form submission behavior
        e.preventDefault();
        const orderData = {
            customer,
            products: orderProducts.map(p => ({
                warehousenr: p.warehouse,
                productnr: p.product,
                quantity: p.quantity
            })),
            payment,
            status: {
                statusnr: 2 // Picked
            }
        };

        // Validate order data
        try {
            await addCustomerOrder(orderData);
            setOrderModalSuccess(true);
            setOrderModalMessage('Order created successfully!');
            setIsOrderModalOpen(true);
        } catch (error) { // Handle error during order creation
            setOrderModalSuccess(false);
            setOrderModalMessage('Failed to create order. Please try again.');
            setIsOrderModalOpen(true);
        }
    };

    // Handle closing the order modal
    const handleOrderModalClose = () => {
        setIsOrderModalOpen(false);
        setOrderProducts([{ warehouse: '', product: '', quantity: '', available: null }]);
        setCustomer({
            email: '', phone: '', firstname: '', lastname: '', country: '', city: '', street: '', postcode: ''
        });
        setPaymentType('now');
        setPayment({ paymentmethod: '', paymentdate: '' });
        setProducts([]);
        setCustomerFound(null);
        setIsSearching(false);
        setOrderModalSuccess(true);
        setOrderModalMessage('');
    };

    // Render access denied if not admin
    if (!authorisedUser || authorisedUser.role?.name !== "admin") {
        return (
            <div style={{ padding: "2em", color: "red", textAlign: "center" }}>
                Access denied. This page is only available to admin users.
            </div>
        );
    }

    return (
        <>
            <form className="add-customer-order" onSubmit={handleSubmit}>
                <div className="order-header general-info">
                    <h1>New Order</h1>
                </div>
                {orderProducts.length > 0 && orderProducts.map((item, idx) => (
                    <div key={idx} className="order-product-block" style={{ display: 'flex', alignItems: 'flex-end', gap: '10px' }}>
                        <div>
                            <label>Warehouse</label>
                            <select
                                value={item.warehouse}
                                onChange={e => handleOrderProductChange(idx, 'warehouse', e.target.value)}
                            >
                                <option value="">Choose warehouse</option>
                                {warehouses.length > 0 && warehouses.map(w => (
                                    <option key={w.warehousenr} value={w.warehousenr}>{w.name}</option>
                                ))}
                            </select>
                        </div>
                        <div>
                            <label>Product</label>
                            <select
                                value={item.product}
                                onChange={e => handleOrderProductChange(idx, 'product', e.target.value)}
                                disabled={!item.warehouse}
                            >
                                <option value="">Choose product</option>
                                {getUniqueProducts(products).map(p => (
                                    <option key={p.product?.productnr ?? p.productnr} value={p.product?.productnr ?? p.productnr}>
                                        {p.product?.name ?? p.name}
                                    </option>
                                ))}
                            </select>
                        </div>
                        <div>
                            <label>Quantity</label>
                            <input
                                type="number"
                                min="1"
                                value={item.quantity}
                                onChange={e => handleQuantityChange(idx, e.target.value)}
                                disabled={!item.product}
                            />
                        </div>
                        <div>
                            {item.warehouse && item.product && (
                                <span>
                                    {item.available !== null
                                        ? `Available ${item.available}`
                                        : ''}
                                </span>
                            )}
                        </div>
                        <div>
                            {item.quantity && item.available !== null && Number(item.quantity) > item.available && (
                                <span style={{ color: 'red' }}>Not enough in stock</span>
                            )}
                        </div>
                        {idx > 0 && (
                            <button
                                type="button"
                                onClick={() => removeProductBlock(idx)}
                                aria-label="Delete product"
                                style={{
                                    background: 'none',
                                    border: 'none',
                                    color: 'red',
                                    fontSize: '1.5em',
                                    cursor: 'pointer',
                                    padding: 0,
                                    marginLeft: '8px'
                                }}
                                title="Remove product"
                            >
                                &#10006;
                            </button>
                        )}
                    </div>
                ))}
                <button type="button" onClick={addProductBlock}>
                    Add more product
                </button>

                <div className="customer-info-block">
                    <h3>Customer Information</h3>
                    <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                        <input
                            placeholder="Email"
                            value={customer.email}
                            onChange={e => {
                                setCustomer({ ...customer, email: e.target.value });
                                setCustomerFound(null); // Reset found state on change
                            }}
                            disabled={isSearching}
                        />
                        <button
                            type="button"
                            onClick={handleFindCustomer}
                            disabled={!customer.email || isSearching}
                        >
                            {isSearching ? 'Searching...' : 'Find'}
                        </button>
                        {customerFound === true && (
                            <span style={{ color: 'green', marginLeft: '8px' }}>Customer found!</span>
                        )}
                        {customerFound === false && (
                            <span style={{ color: 'red', marginLeft: '8px' }}>
                                Customer not found. Please register the user.
                            </span>
                        )}
                    </div>
                    {/* Only show after Find is pressed and user is not found */}
                    {customerFound === false && (
                        <div>
                            <input placeholder="Phone" value={customer.phone} onChange={e => setCustomer({ ...customer, phone: e.target.value })} />
                            <input placeholder="First name" value={customer.firstname} onChange={e => setCustomer({ ...customer, firstname: e.target.value })} />
                            <input placeholder="Last name" value={customer.lastname} onChange={e => setCustomer({ ...customer, lastname: e.target.value })} />
                            <input placeholder="Country" value={customer.country} onChange={e => setCustomer({ ...customer, country: e.target.value })} />
                            <input placeholder="City" value={customer.city} onChange={e => setCustomer({ ...customer, city: e.target.value })} />
                            <input placeholder="Street" value={customer.street} onChange={e => setCustomer({ ...customer, street: e.target.value })} />
                            <input placeholder="Postcode" value={customer.postcode} onChange={e => setCustomer({ ...customer, postcode: e.target.value })} />
                        </div>
                    )}
                </div>

                <div className="payment-block">
                    <h3>Payment</h3>
                    <select value={paymentType} onChange={e => setPaymentType(e.target.value)}>
                        <option value="now">Pay now</option>
                        <option value="later">Pay later</option>
                    </select>
                    {paymentType === 'now' && (
                        <div>
                            <select
                                value={payment.paymentmethod}
                                onChange={e => setPayment({ ...payment, paymentmethod: e.target.value })}
                            >
                                <option value="">Choose payment method</option>
                                {paymentMethods.length > 0 && paymentMethods.map(pm => (
                                    <option key={pm.paymentmethodnr} value={pm.paymentmethodnr}>{pm.name}</option>
                                ))}
                            </select>
                            <input
                                type="datetime-local"
                                value={payment.paymentdate}
                                onChange={e => setPayment({ ...payment, paymentdate: e.target.value })}
                            />
                        </div>
                    )}
                </div>

                <button type="submit">Create Order</button>
            </form>

            {/* Order feedback modal */}
            {isOrderModalOpen && (
                <div className="t-modal">
                    <div className="t-modal-content">
                        <span className="t-close" onClick={handleOrderModalClose}>
                            &times;
                        </span>
                        <h2>{orderModalSuccess ? 'Success' : 'Error'}</h2>
                        <p>{orderModalMessage}</p>
                        <button className="t-deleteErrorBtn" onClick={handleOrderModalClose}>
                            Close
                        </button>
                    </div>
                </div>
            )}
        </>
    );
}

export default AddCustomerOrder;