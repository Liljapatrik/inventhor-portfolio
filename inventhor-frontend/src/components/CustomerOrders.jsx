/**
 * Author: Tatiana Fløisbonn
 * Date: 9 June 2025
 * Description: This component displays customer orders, allows searching, sorting, editing, and deleting orders.
 */

import { useEffect, useState } from 'react';
import { useNavigate } from "react-router-dom";
import './inventory.css';

function CustomerOrders({ getCustomerOrderProducts, getCustomerOrders, updateCustomerOrder, deleteCustomerOrder, getCustomerPayments, getOrderStatuses, getPaymentMethods, updateCustomerPayment, authorisedUser }) {
    const navigate = useNavigate();
    const [orders, setOrders] = useState([]);
    const [payments, setPayments] = useState([]);
    const [search, setSearch] = useState("");
    const [sort, setSort] = useState("id");
    const [editOrder, setEditOrder] = useState(null);
    const [editStatus, setEditStatus] = useState("");
    const [editPaid, setEditPaid] = useState(false);
    const [editPaymentDate, setEditPaymentDate] = useState("");
    const [editPaymentMethod, setEditPaymentMethod] = useState("");
    const [isEditModalOpen, setIsEditModalOpen] = useState(false);
    const [statuses, setStatuses] = useState([]);
    const [paymentMethods, setPaymentMethods] = useState([]);

    // Delete modal states
    const [isDeleteMessageOpen, setIsDeleteMessageOpen] = useState(false);
    const [isDeleteSuccessOpen, setIsDeleteSuccessOpen] = useState(false);
    const [orderToDelete, setOrderToDelete] = useState(null);

    useEffect(() => {
        getCustomerOrders().then(data => setOrders(data));
        getCustomerPayments().then(data => setPayments(data));
        getOrderStatuses().then(data => setStatuses(data));
        getPaymentMethods().then(data => setPaymentMethods(data));
    }, [getCustomerOrders, getCustomerPayments, getOrderStatuses, getPaymentMethods]);

    // Handle search and sort changes
    function onSearchChange(e) {
        setSearch(e.target.value);
    }

    // Handle sort change
    function onSortChange(e) {
        setSort(e.target.value);
    }

    // Helper function to get payment by order number
    function getPaymentByOrderNr(ordernr) {
        return payments.find(p => p.ordernr === ordernr);
    }

    // Get sorted and filtered orders data
    function getSortedOrdersData() {
        return orders
            .filter(order => {
                const customerName = ((order.customer?.firstname || "") + " " + (order.customer?.lastname || "")).toLowerCase();
                const ordernr = order.ordernr?.toString().toLowerCase() || "";
                const status = order.status?.name?.toLowerCase() || "";
                const delivered = order.deliverydate ? formatDate(order.deliverydate).toLowerCase() : "in process";
                const payment = getPaymentByOrderNr(order.ordernr);
                const paid = payment && payment.paymentdate ? "paid" : "not paid";
                const searchLower = search.toLowerCase();

                return (
                    customerName.includes(searchLower) ||
                    ordernr.includes(searchLower) ||
                    status.includes(searchLower) ||
                    delivered.includes(searchLower) ||
                    paid.includes(searchLower)
                );
            })
            .sort((a, b) => {
                if (sort === "status") {
                    return (a.status?.name + "").localeCompare(b.status?.name + "");
                } else if (sort === "paid") {
                    const aPaid = getPaymentByOrderNr(a.ordernr)?.paymentdate ? 1 : 0;
                    const bPaid = getPaymentByOrderNr(b.ordernr)?.paymentdate ? 1 : 0;
                    return bPaid - aPaid;
                } else if (sort === "id") {
                    return (a.ordernr + "").localeCompare(b.ordernr + "");
                } else {
                    return (a[sort] + "").localeCompare(b[sort] + "");
                }
            });
    }

    // Handle edit order
    function handleEdit(order) {
        const payment = getPaymentByOrderNr(order.ordernr);
        setEditOrder({
            ...order,
            payment: payment || {}
        });
        setEditStatus(order.status.name);
        setEditPaid(order.paid);
        setEditPaymentDate(payment && payment.paymentdate ? payment.paymentdate.slice(0, 16) : "");
        setEditPaymentMethod(payment && payment.paymentmethod ? payment.paymentmethod : "");
        setIsEditModalOpen(true);
    }

    // Handle edit modal close
    function handleEditModalClose() {
        setIsEditModalOpen(false);
        setEditOrder(null);
    }

    // Handle save changes in edit modal
    async function handleSave(e) {
        e.preventDefault();

        // Find statusnr based on selected status name
        const selectedStatus = statuses.find(s => s.name === editStatus);
        const statusnr = selectedStatus ? selectedStatus.statusnr : editOrder.status.statusnr;

        // Set delivery date if status is "Delivered"
        let deliverydate = editOrder.deliverydate;
        if (editStatus === "Delivered") {
            deliverydate = new Date().toISOString();
        }

        // Build order update object
        const updateOrderObj = {
            ordernr: editOrder.ordernr,
            customer: editOrder.customer,
            orderdate: editOrder.orderdate,
            status: { statusnr: statusnr },
            deliverydate: deliverydate
        };

        // Update order
        await updateCustomerOrder(editOrder.ordernr, updateOrderObj);

        // If payment date is provided and not already paid, update payment
        if ((!editOrder.payment || !editOrder.payment.paymentdate) && editPaymentDate) {
            var products = await getCustomerOrderProducts(editOrder.ordernr);
            const amount = products.reduce((sum, p) => sum + (p.product.sellprice * p.quantity), 0);

            const paymentObj = {
                ordernr: editOrder.ordernr,
                paymentdate: editPaymentDate,
                paymentmethod: editPaymentMethod,
                amount: amount
            };

            await updateCustomerPayment(editOrder.ordernr, paymentObj);
        }

        setIsEditModalOpen(false);
        setEditOrder(null);
        getCustomerOrders().then(data => setOrders(data));
        getCustomerPayments().then(data => setPayments(data));
    }

    // Delete modal logic
    function handleDelete(order) {
        // Only allow delete if status is Cancelled or Delivered
        if (
            order.status.name === "Cancelled" ||
            order.status.name === "Delivered"
        ) {
            setOrderToDelete(order);
            setIsDeleteMessageOpen(true);
        } else {
            setOrderToDelete(null);
            setIsDeleteMessageOpen(true);
        }
    }

    // Confirm delete order
    async function confirmDeleteOrder() {
        if (orderToDelete) {
            await deleteCustomerOrder(orderToDelete.ordernr);
            getCustomerOrders().then(data => setOrders(data));
            getCustomerPayments().then(data => setPayments(data));
            setIsDeleteMessageOpen(false);
            setIsDeleteSuccessOpen(true);
            setOrderToDelete(null);
        }
    }

    // Close delete message modal
    function handleDeleteMessageClose() {
        setIsDeleteMessageOpen(false);
        setOrderToDelete(null);
    }

    // Close delete success modal
    function handleDeleteSuccessClose() {
        setIsDeleteSuccessOpen(false);
    }

    // Format date function
    function formatDate(dateString) {
        if (!dateString) return "In process";
        const date = new Date(dateString);
        const pad = n => n.toString().padStart(2, '0');
        return `${pad(date.getDate())}.${pad(date.getMonth() + 1)}.${date.getFullYear()} ${pad(date.getHours())}:${pad(date.getMinutes())}`;
    }

    // Payment date cell component
    function PaymentDateCell({ ordernr, formatDate }) {
        const payment = getPaymentByOrderNr(ordernr);
        return <>{payment && payment.paymentdate ? formatDate(payment.paymentdate) : 'Not paid'}</>;
    }

    return (
        <>
            <h1>Customer Orders</h1>
            <div className="tableFunctions">
                <div className="tableSeachbar">
                    <i className="bi bi-search"></i>
                    <input
                        className="tableSeachInput"
                        type="text"
                        placeholder="Search"
                        onChange={onSearchChange}
                        value={search}
                    />
                </div>
                <div className="sortWithAddFunctions">
                    <select className="tableSort" onChange={onSortChange} value={sort}>
                        <option value="id">Order nr</option>
                        <option value="status">Status</option>
                        <option value="paid">Paid</option>
                    </select>
                    {authorisedUser.role.name === "admin" && (
                        <button className="addBtn" onClick={() => navigate(`/orders/add-customer-order`)}>
                            Add Customer Order
                        </button>
                    )}
                </div>
            </div>
            <div className="table-container">
                <table>
                    <thead>
                        <tr>
                            <th>Ordernr</th>
                            <th>Customer</th>
                            <th>Ordered</th>
                            <th>Status</th>
                            <th>Delivered</th>
                            <th>Paid</th>
                            <th></th>
                            {authorisedUser.role.name === "admin" && <th></th>}
                        </tr>
                    </thead>
                    <tbody>
                        {orders.length > 0 &&
                            getSortedOrdersData().map((order) => (
                                <tr key={order.ordernr}>
                                    <td
                                        style={{ cursor: "pointer" }}
                                        onClick={() => navigate(`/orders/customer-order/${order.ordernr}`)}
                                    >
                                        {order.ordernr}
                                    </td>
                                    <td
                                        style={{ cursor: "pointer" }}
                                        onClick={() => navigate(`/orders/customer-order/${order.ordernr}`)}
                                    >
                                        {order.customer?.firstname} {order.customer?.lastname}
                                    </td>
                                    <td
                                        style={{ cursor: "pointer" }}
                                        onClick={() => navigate(`/orders/customer-order/${order.ordernr}`)}
                                    >
                                        {formatDate(order.orderdate)}
                                    </td>
                                    <td
                                        style={{ cursor: "pointer" }}
                                        onClick={() => navigate(`/orders/customer-order/${order.ordernr}`)}
                                    >
                                        {order.status?.name}
                                    </td>
                                    <td
                                        style={{ cursor: "pointer" }}
                                        onClick={() => navigate(`/orders/customer-order/${order.ordernr}`)}
                                    >
                                        {formatDate(order.deliverydate)}
                                    </td>
                                    <td>
                                        <PaymentDateCell
                                            ordernr={order.ordernr}
                                            formatDate={formatDate}
                                        />
                                    </td>
                                    <td>
                                        <span
                                            className="bi bi-pencil-square"
                                            style={{ cursor: "pointer" }}
                                            onClick={() => handleEdit(order)}
                                        ></span>
                                    </td>
                                    {authorisedUser.role.name === "admin" && (
                                        <td>
                                            <span
                                                className="bi bi-trash"
                                                style={{
                                                    cursor: (order.status.name === "Cancelled" || order.status.name === "Delivered") ? "pointer" : "not-allowed", // Change cursor based on status
                                                    color: (order.status.name === "Cancelled" || order.status.name === "Delivered") ? "" : "#ccc" // Change color based on status
                                                }}
                                                onClick={() => handleDelete(order)}
                                            ></span>
                                        </td>
                                    )}
                                </tr>
                            ))}
                    </tbody>
                </table>
            </div>

            {isEditModalOpen && editOrder && (
                <div className="t-modal">
                    <div className="t-modal-content">
                        <span className="close" onClick={handleEditModalClose}>&times;</span>
                        <h2>Edit Customer Order</h2>
                        <form onSubmit={handleSave}>
                            <div className="t-form-group">
                                <label>Status:</label>
                                <select
                                    value={editStatus}
                                    onChange={e => setEditStatus(e.target.value)}
                                >
                                    {statuses.map(status => (
                                        <option key={status.name} value={status.name}>
                                            {status.name}
                                        </option>
                                    ))}
                                </select>
                            </div>
                            {(!editOrder.payment || editOrder.payment.paymentdate == null) ? (
                                <>
                                    <div className="t-form-group">
                                        <label>Payment Date:</label>
                                        <input
                                            type="datetime-local"
                                            value={editPaymentDate}
                                            onChange={e => setEditPaymentDate(e.target.value)}
                                            required
                                        />
                                    </div>
                                    <div className="t-form-group">
                                        <label>Payment Method:</label>
                                        <select
                                            value={editPaymentMethod}
                                            onChange={e => setEditPaymentMethod(e.target.value)}
                                            required
                                        >
                                            <option value="" disabled>Select method</option>
                                            {paymentMethods.map(method => (
                                                <option key={method.paymentmethodnr} value={method.paymentmethodnr}>
                                                    {method.name}
                                                </option>
                                            ))}
                                        </select>
                                    </div>
                                </>
                            ) : (
                                <div className="t-form-group">
                                    <div style={{ fontSize: 12, color: "#888" }}>
                                        Payment date: {formatDate(editOrder.payment.paymentdate)}
                                    </div>
                                </div>
                            )}
                            <button type="submit">Save</button>
                        </form>
                    </div>
                </div>
            )}

            {/* Delete confirmation modal */}
            {isDeleteMessageOpen && (
                <div className="t-modal">
                    <div className="t-modal-content">
                        <span className="t-close" onClick={handleDeleteMessageClose}>
                            &times;
                        </span>
                        {orderToDelete ? (
                            <>
                                <h2>Delete Order</h2>
                                <p>Are you sure you want to delete order #{orderToDelete.ordernr}?</p>
                                <button className="t-deleteErrorBtn" onClick={confirmDeleteOrder}>
                                    Yes, Delete
                                </button>
                                <button className="t-deleteErrorBtn" onClick={handleDeleteMessageClose}>
                                    Cancel
                                </button>
                            </>
                        ) : (
                            <>
                                <h2>Delete Error</h2>
                                <p>The order cannot be deleted because it is active.</p>
                                <button className="t-deleteErrorBtn" onClick={handleDeleteMessageClose}>
                                    Close
                                </button>
                            </>
                        )}
                    </div>
                </div>
            )}

            {/* Delete success modal */}
            {isDeleteSuccessOpen && (
                <div className="t-modal">
                    <div className="t-modal-content">
                        <span className="t-close" onClick={handleDeleteSuccessClose}>
                            &times;
                        </span>
                        <h2>Order Deleted</h2>
                        <p>The order was successfully deleted.</p>
                        <button className="t-deleteErrorBtn" onClick={handleDeleteSuccessClose}>
                            Close
                        </button>
                    </div>
                </div>
            )}
        </>
    );
}

export default CustomerOrders;