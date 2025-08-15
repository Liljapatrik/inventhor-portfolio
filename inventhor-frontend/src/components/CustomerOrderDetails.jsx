/**
 * Author: Tatiana Fløisbonn
 * Date: 9 June 2025
 * Description: This component displays detailed information about a customer's order, including order date, delivery date, customer information, payment status, payment method, order status, and a list of products in the order.
 * It uses React hooks for state management and side effects, and PrimeReact's Timeline component for visualizing the order status.
 */

import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { Timeline } from 'primereact/timeline';
import 'primereact/resources/themes/saga-blue/theme.css';
import 'primereact/resources/primereact.min.css';
import 'primeicons/primeicons.css';
import './orderDetails.css';

/*
  * Formats a date string to a more readable format.
*/
function formatDate(dateString) {
  if (!dateString) return null;
  const date = new Date(dateString);
  return date.toLocaleString('en-GB', { day: '2-digit', month: '2-digit', year: 'numeric', hour: '2-digit', minute: '2-digit' }).replace(',', '');
}

function CustomerOrderDetails({ getCustomerOrderByOrdernr, getCustomerPaymentByOrdernr, getCustomerOrderProducts, getPaymentMethods }) {
  const { ordernr } = useParams();

  const [order, setOrder] = useState(null);
  const [payment, setPayment] = useState(null);
  const [products, setProducts] = useState([]);
  const [paymentMethods, setPaymentMethods] = useState([]);

  // Fetch order details and products information when the component mounts or ordernr changes
  useEffect(() => {
    getCustomerOrderByOrdernr(ordernr).then(setOrder);
    getCustomerOrderProducts(ordernr).then(setProducts);
  }, [ordernr, getCustomerOrderByOrdernr, getCustomerOrderProducts]);

  // Fetch payment information when the order is available
  useEffect(() => {
    if (order) {
      getCustomerPaymentByOrdernr(ordernr).then(setPayment);
    }
  }, [order, ordernr, getCustomerPaymentByOrdernr]);

  // Fetch payment methods when the component mounts
  useEffect(() => {
    getPaymentMethods().then(setPaymentMethods);
  }, [getPaymentMethods]);

  // Define the events for the timeline
  const events = [
    { status: 'Ordered' },
    { status: 'Picked' },
    { status: 'Shipped' },
    { status: 'Delivered' }
  ];

  // Function to get the index of the current status in the timeline
  const getStatusIndex = (status) => {
    switch (status) {
      case 'Ordered': return 0;
      case 'Picked': return 1;
      case 'Shipped': return 2;
      case 'Delivered': return 3;
      default: return -1;
    }
  };

  // If order is not yet loaded, show a loading message
  if (!order) return <div>Loading...</div>;

  // Find the current payment method based on the payment information
  const currentPaymentMethod = paymentMethods.find(
    (method) => payment && payment.paymentmethod && method.id === payment.paymentmethod.id
  );

  return (
    <div className='order-details-container'>
      <div className='order-details-header'>
        <h1>Order nr: {order.ordernr}</h1>
      </div>
      <div className='order-details-info'>
        <div className='order-date'>
          <h5>Ordered:</h5>
          <p>{formatDate(order.orderdate)}</p>
        </div>
        <div className='order-delivery'>
          <h5>Delivered:</h5>
          <p>
            {order.deliverydate
              ? formatDate(order.deliverydate)
              : (order.status.name === "Cancelled"
                ? 'Cancelled'
                : 'In process')
            }
          </p>
        </div>
        <div className='order-customer'>
          <h5>Customer:</h5>
          <p>{order.customer.firstname} {order.customer.lastname}</p>
        </div>
        <div className='order-address'>
          <h5>Delivery to:</h5>
          <p>
            {order.customer.address.street}, {order.customer.address.postcode} {order.customer.address.city}, {order.customer.address.country}
          </p>
        </div>
        <div className='order-paid'>
          <h5>Paid:</h5>
          <p>
            <PaymentDateCell
              ordernr={order.ordernr}
              getCustomerPaymentByOrdernr={getCustomerPaymentByOrdernr}
              formatDate={formatDate}
            />
          </p>
        </div>
        <div className='order-payment-method'>
          <h5>Payment method:</h5>
          <p>
            {currentPaymentMethod
              ? currentPaymentMethod.name
              : (payment && payment.paymentmethod ? payment.paymentmethod.name : '-')}
          </p>
        </div>
        <div className='order-status'>
          <h5>Status:</h5>
          {order.status.name !== "Cancelled" ? (
            <Timeline className='timeline-container' value={events} layout="horizontal"
              marker={(item, index) => (
                <i className={" " + (index < getStatusIndex(order.status.name) ? "pi pi-circle-fill active-connector" : "pi pi-circle-fill inactive-connector")} style={{ color: index <= getStatusIndex(order.status.name) ? 'var(--highlight-color)' : 'var(--stroke-color)', fontSize: '2rem' }} />
              )}
              content={(item) => (
                <h6>{item.status}</h6>
              )}
            />
          ) : (
            <p>Cancelled</p>
          )}
        </div>
        <div className='order-details'>
          <hr />
          <h4>Order details:</h4>
          <table className='order-details-table'>
            <tbody>
              {products.length > 0 &&
                products.map((item, idx) => (
                  <React.Fragment key={idx}>
                    <tr>
                      <td rowSpan={3}>
                        <img src={item.product.image} alt={item.product.name} style={{ width: 60, height: 60 }} />
                      </td>
                      <td><h5>{item.product.name}</h5></td>
                      <td rowSpan={3}>{item.product.sellprice} kr</td>
                    </tr>
                    <tr>
                      <td>Category: {item.product.category.name}</td>
                    </tr>
                    <tr>
                      <td>Quantity: {item.quantity}</td>
                    </tr>
                  </React.Fragment>
                ))
              }
            </tbody>
          </table>
          <hr />
          <h5 className='order-details-totalPrice'>
            Total price: {products.length > 0 && products.reduce((prev, cur) => prev + cur.product.sellprice * cur.quantity, 0)} kr
          </h5>
        </div>
      </div>
    </div>
  );
}

// Helper component for payment date cell
function PaymentDateCell({ ordernr, getCustomerPaymentByOrdernr, formatDate }) {
  const [paymentDate, setPaymentDate] = useState(null);

  useEffect(() => {
    let isMounted = true;
    getCustomerPaymentByOrdernr(ordernr).then(payment => {
      if (!isMounted) return;
      setPaymentDate(payment?.paymentdate || null);
    });
    return () => { isMounted = false; };
  }, [ordernr, getCustomerPaymentByOrdernr]);

  return <>{paymentDate ? formatDate(paymentDate) : 'Not paid'}</>;
}

export default CustomerOrderDetails;