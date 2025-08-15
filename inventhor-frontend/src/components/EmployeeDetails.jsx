/**
 * Author: Tatiana Fløisbonn
 * Date: 27 April 2025
 * Description: This component displays the details of an employee.
 * It fetches the employee data based on the employee number from the URL,
 * and displays the employee's information such as name, email, phone, address, position, role, employed date, and active status.
 * It also checks if the user is authorised (admin) to view this page.
 */

import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';

import './employeeDetails.css';

function EmployeeDetails({ getEmployeeByEmployeenr, authorisedUser }) {

    const { id } = useParams(); // get the employeeID from the URL

    const [employees, setEmployees] = useState([]);

    useEffect(() => {
        getEmployeeByEmployeenr(id).then(data => setEmployees([data]));
    }, [getEmployeeByEmployeenr, id]);

    // Render access denied if not admin
    if (!authorisedUser || authorisedUser.role?.name !== "admin") {
        return (
            <div style={{ padding: "2em", color: "red", textAlign: "center" }}>
                Access denied. This page is only available to admin users.
            </div>
        );
    }

    return (
        <div className='employee-details-container'>

            {employees.map(employees => (
                <>
                    <div className='employee-details-header'>

                        <h1 key={employees.employeenr}>{employees.firstname} {employees.lastname}</h1>

                        <img
                            src={employees.image}
                            alt={
                                employees.firstname && employees.lastname
                                    ? `${employees.firstname} ${employees.lastname}`
                                    : "Employee photo"
                            }
                        />

                    </div>

                    <div className='employee-details-info'>

                        <div className='employee-details-info-item'>

                            <h5>Email:</h5>
                            <h6 key={employees.employeenr}>{employees.email}</h6>

                        </div>

                        <div className='employee-details-info-item'>
                            <h5>Phone:</h5>
                            <h6 key={employees.employeenr}>{employees.phone}</h6>
                        </div>

                        <div className='employee-details-info-item'>
                            <h5>Address:</h5>
                            <h6>{employees.address.street} {employees.address.postcode}</h6>
                            <h6> {employees.address.city} {employees.address.country}</h6>
                        </div>

                        <div className='employee-details-info-item'>

                            <h5>Position:</h5>
                            <h6 key={employees.employeenr}>{employees.position}</h6>

                        </div>

                        <div className='employee-details-info-item'>

                            <h5>Role:</h5>
                            <h6 key={employees.employeenr}>{employees.role.name}</h6>

                        </div>

                        <div className='employee-details-info-item'>

                            <h5>Employed Date:</h5>
                            <h6 key={employees.employeenr}>{employees.employeddate}</h6>

                        </div>

                        <div className='employee-details-info-item'>
                            <h5>Active:</h5>
                            <h6 key={employees.employeenr}>
                                {employees.isactive ? "Active" : "Not Active"}
                            </h6>
                        </div>

                    </div>

                </>


            ))}
        </div>
    )
}

export default EmployeeDetails