/**
 * Author: Tatiana Fløisbonn
 * Date: 1 February 2025
 * Description: NavNotice component for displaying notifications in the navigation bar.
 * It fetches notifications for the authorised user, displays them in a dropdown,
 * and allows marking them as read when clicked.
 * Notifications are fetched every 60 seconds and displayed with appropriate icons based on their type.
 */

import React, { useEffect, useState } from 'react';

function NavNotice({ getNotificationsForEmployee, authorisedUser, updateNotification }) {

    const [notifications, setNotifications] = useState([]);
    const [selectedNotification, setSelectedNotification] = useState(null);

    // Fetch notifications only when bell is clicked
    const fetchNotifications = () => {
        if (authorisedUser && authorisedUser.employeenr) {
            console.log("Fetching notifications for authorised user:", authorisedUser);
            getNotificationsForEmployee(authorisedUser.employeenr).then(data => {
                setNotifications(data.filter(n => !n.isread));
            });
        }
    };

    // Call fetchNotifications when component mounts
    useEffect(() => {

        // Get notification every 4 mniuntes
        if (authorisedUser && authorisedUser.employeenr) {

            fetchNotifications();
            const interval = setInterval(() => {    
                fetchNotifications();
            }, 60000); // every 60 secondss

            return () => clearInterval(interval); // Cleanup on unmount
        }

    }, [authorisedUser]);

    // Get icon class from notification.notificationstype.name
    const getIconClass = (type) => {
        switch (type) {
            case 'Warning':
                return 'bi bi-exclamation-circle text-warning';
            case 'Danger':
                return 'bi bi-x-circle text-danger';
            case 'Success':
                return 'bi bi-check-circle text-success';
            case 'Info':
                return 'bi bi-info-circle text-primary';
            default:
                return '';
        }
    };

    // Handle notification click
    const handleNotificationClick = (notification) => {
        setSelectedNotification(notification);
    };

    // Handle popup close and mark as read
    const handleClosePopup = () => {
        if (selectedNotification && !selectedNotification.isread) {
            updateNotification(selectedNotification.notificationnr);
        }
        setSelectedNotification(null);
    };

    return (
        <li className='notice-item dropdown'>
            <a
                className='notice-link notice-icon'
                data-bs-toggle='dropdown'
                onClick={fetchNotifications}
            >
                <i className='bi bi-bell'></i>
                {notifications.length > 0 && (
                    <span className="badge bg-danger badge-number">{notifications.length}</span>
                )}
            </a>

            <ul className='dropdown-menu dropdown-menu-end dropdown-menu-arrow notifications mt-3'>
                <li className='dropdown-header'>
                    You have {notifications.length} unread notifications
                </li>

                <li>
                    <hr className='dropdown-divider' />
                </li>

                {notifications.map((notification, index) => (
                    <React.Fragment key={notification.id || index}>
                        <li
                            className='notification-item'
                            style={{ cursor: 'pointer' }}
                            onClick={() => handleNotificationClick(notification)}
                        >
                            <i className={getIconClass(notification.notificationType.name)}></i>
                            <div>
                                <h4>{notification.title}</h4>
                            </div>
                        </li>
                        <li>
                            <hr className='dropdown-divider' />
                        </li>
                    </React.Fragment>
                ))}

                <li className='dropdown-footer'>
                    <a href='/notifications'>Show all notifications</a>
                </li>
            </ul>

            {/* Popup for notification details */}
            {selectedNotification && (
                <div className="notification-popup-overlay">
                    <div className="notification-popup">
                        <h2>{selectedNotification.title}</h2>
                        <p>{selectedNotification.message}</p>
                        <button type='submit' onClick={handleClosePopup}>Close</button>
                    </div>
                </div>
            )}
        </li>
    );
}

export default NavNotice;