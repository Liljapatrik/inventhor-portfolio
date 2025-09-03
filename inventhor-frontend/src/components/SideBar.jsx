/**
 * Author: Tatiana Fløisbonn
 * Date: 1 February 2025
 * Description: SideBar component for displaying the navigation sidebar in the application.
 * It includes links to various sections such as Inventory, Orders, Report, Warehouses, Suppliers, Employees (only for admin), Notifications, and Settings.
 * It also handles dropdowns for Orders and dynamically shows/hides the Employees section based on the user's role.
 * It uses React Router's useNavigate for navigation and localStorage to check if the user is authorised.
 * The sidebar visibility is controlled by a localStorage item.
 */

import React from 'react';
import './sideBar.css';
import { useNavigate } from 'react-router-dom';
import { useEffect, useState } from 'react';

function SideBar({ authorisedUser }) {

    const navigate = useNavigate();
    const [openDropdown, setOpenDropdown] = useState(null);

    // Check if user is authorised
    useEffect(() => {
        const storedUser = localStorage.getItem('authorisedUser');
        if (!storedUser) {
            navigate("/signin");
        }
    }, []);

    // Build navList based on role
    const isAdmin = authorisedUser && authorisedUser.role && authorisedUser.role.name === 'admin';
    const navList = [
        {sidebar_id:1, sidebar_name:'Inventory', sidebar_icon:'bi bi-box-fill', sidebar_link:'/inventory'},
        {
            sidebar_id:2,
            sidebar_name:'Orders',
            sidebar_icon:'bi bi-truck',
            dropdown: [
                { name: 'Customer orders', link: '/orders/customer' },
                { name: 'Warehouse orders', link: '/orders/warehouse' }
            ]
        },
        {sidebar_id:3, sidebar_name:'Report', sidebar_icon:'bi bi-clipboard-data-fill', sidebar_link:'/report'},
        {sidebar_id:4, sidebar_name:'Warehouses', sidebar_icon:'bi bi-building-fill', sidebar_link:'/warehouses'},
        {sidebar_id:5, sidebar_name:'Suppliers', sidebar_icon:'bi bi-buildings-fill', sidebar_link:'/suppliers'},
        {sidebar_id:6, sidebar_name:'Employees', sidebar_icon:'bi bi-person-vcard-fill', sidebar_link:'/employees', show: isAdmin}, // Show only for admin
        {sidebar_id:7, sidebar_name:'Notifications', sidebar_icon:'bi bi-bell-fill', sidebar_link:'/notifications'},
        {sidebar_id:8, sidebar_name:'Settings', sidebar_icon:'bi bi-gear-fill', sidebar_link:'/settings'}
    ];
    // Filter navList to hide Employees for staff
    const filteredNavList = navList.filter(nav => nav.show === undefined || nav.show);

    let sidebar_visibility = localStorage.getItem('sidebar_visibility');

    return (
        <aside id="sidebar" className='sidebar'>
            <ul className='sidebar-nav' id='sidebar-nav'>
                {filteredNavList.map(nav => (
                    nav.dropdown ? (
                        <React.Fragment key={nav.sidebar_id}>
                            <li className='sidebar-nav-item'>
                                <div
                                    className='sidebar-nav-link'
                                    style={{ display: 'flex', alignItems: 'center', cursor: 'pointer' }}
                                    onClick={() => setOpenDropdown(openDropdown === nav.sidebar_id ? null : nav.sidebar_id)}
                                >
                                    <i className={nav.sidebar_icon}></i>
                                    <span>{nav.sidebar_name}</span>
                                    <i className={`bi ${openDropdown === nav.sidebar_id ? 'bi-caret-up-fill' : 'bi-caret-down-fill'}`} style={{ marginLeft: 'auto' }}></i>
                                </div>
                            </li>
                            {openDropdown === nav.sidebar_id && nav.dropdown.map((item) => (
                                <li key={item.link} className='sidebar-nav-item sidebar-nav-subitem'>
                                    <a href={item.link} className='sidebar-nav-link sidebar-nav-link-sub'>
                                        <span>{item.name}</span>
                                    </a>
                                </li>
                            ))}
                        </React.Fragment>
                    ) : (
                        <li key={nav.sidebar_id} className='sidebar-nav-item'>
                            <a href={nav.sidebar_link} className='sidebar-nav-link'>
                                <i className={nav.sidebar_icon}></i>
                                <span>{nav.sidebar_name}</span>
                            </a>
                        </li>
                    )
                ))}           
            </ul>
        </aside>
    )
}

export default SideBar;