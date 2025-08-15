/**
 * Author: Furo Muktar Eshetu
 * Date: 4 February 2025
 * Description: Notifications component for displaying and managing employee notifications.
 * It includes search and sort functionalities, and a popup for detailed notification view.
 * Notifications are fetched based on the authorised user's employee number.
 * Notifications can be marked as read when clicked, and a popup displays the notification details.
 * 
 * Trigger in Database set in data for notification automatically when customer order is created.
 */

import React, { useEffect, useState } from 'react';
import './notifications.css';

function Notifications({ getNotificationsForEmployee, authorisedUser, updateNotification }) {
  const [employeenr] = useState(authorisedUser.employeenr);
  const [notifications, setNotifications] = useState([]);
  const [search, setSearch] = useState("");
  const [sort, setSort] = useState("date");
  const [selectedNotification, setSelectedNotification] = useState(null);

  // Fetch notifications when the component mounts or when employeenr changes
  useEffect(() => {
    if (authorisedUser && employeenr) {
      getNotificationsForEmployee(employeenr).then(data => setNotifications(data));
    }
  }, [employeenr, getNotificationsForEmployee, authorisedUser]);

  // Get icon class from notification.notificationType.name
  function onSearchChange(e) {
    setSearch(e.target.value);
  }

  // Handle sort change
  function onSortChange(e) {
    setSort(e.target.value);
  }

  // Get sorted notifications data based on search and sort criteria
  function getSortedNotificationsData() {
    return notifications
      .filter((item) => item.message.toLowerCase().includes(search.toLowerCase()))
      .sort((a, b) => {
        if (sort === "date") {
          return new Date(b.date) - new Date(a.date); // Descending order
        }
        return (a[sort] + "").localeCompare(b[sort] + "");
      });
  }

  // Handle row click to mark notification as read and show details
  function handleRowClick(notification) {
    if (!notification.isread) {
      setNotifications((prev) =>
        prev.map((item) =>
          item.notificationnr === notification.notificationnr
            ? { ...item, isread: true }
            : item
        )
      );
    }
    setSelectedNotification(notification);
  }

  // Close popup and mark notification as read if it was unread
  function closePopup() {
    if (selectedNotification && !selectedNotification.isread) {
      updateNotification(selectedNotification.notificationnr);
    }
    setSelectedNotification(null);
  }

  return (
    <>
      <h1>Notifications</h1>

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

        <select className="tableSort" onChange={onSortChange} value={sort}>
          <option value="date">Date</option>
          <option value="type">Type</option>
        </select>
      </div>

      <div className="table-container">
        <table>
          <thead>
            <tr>
              <th>Date</th>
              <th>Type</th>
              <th>Title</th>
            </tr>
          </thead>
          <tbody>
            {getSortedNotificationsData().map((item) => (
              <tr
                key={item.notificationnr}
                onClick={() => handleRowClick(item)}
                className={item.isread ? 't-notification-row read' : 't-notification-row unread'}
              >
                <td>
                  {item.date && item.date.length >= 16
                    ? `${item.date.slice(8, 10)}.${item.date.slice(5, 7)}.${item.date.slice(0, 4)} ${item.date.slice(11, 16)}`
                    : item.date}
                </td>
                <td>{item.notificationType.name}</td>
                <td>{item.title}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      {selectedNotification && (
        <div className="notification-popup-overlay">
          <div className="notification-popup">
            <h2>{selectedNotification.title}</h2>
            <p>{selectedNotification.message}</p>
            <button type='submit' onClick={closePopup}>Close</button>
          </div>
        </div>
      )}
    </>
  );
}

export default Notifications;