package com.group3.inventhor.service;

import com.group3.inventhor.dto.AddressDTO;
import com.group3.inventhor.dto.AuthUserDTO;
import com.group3.inventhor.dto.EmployeeDTO;
import com.group3.inventhor.dto.EmployeeRoleDTO;
import com.group3.inventhor.mapper.EmployeeMapper;
import com.group3.inventhor.model.Address;
import com.group3.inventhor.model.Employee;
import com.group3.inventhor.model.EmployeeRole;
import com.group3.inventhor.repository.EmployeeRepository;
import com.group3.inventhor.repository.AddressRepository;

import com.group3.inventhor.repository.EmployeeRoleRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author Tatiana Fløisbonn
 *
 * The EmployeeService class provides methods to manage employees in the Inventhor application.
 * It includes methods to get, create, update, and delete employees.
 *
 * @Service indicates that this is a service class that contains business logic and interacts with the data access layer.
 * @RequiredArgsConstructor generates a constructor with all required dependencies.
 */
@Service
@RequiredArgsConstructor
public class EmployeeService {

    // The EmployeeRepository instance used to interact with the database for employee-related operations
    private final EmployeeRepository employeeRepository;
    // The AddressRepository instance used to interact with the database for address-related operations
    private final AddressRepository addressRepository;
    // The EmployeeMapper instance used to convert between Employee and EmployeeDTO objects
    private final EmployeeMapper employeeMapper;
    // The EmployeeRoleRepository instance used to interact with the database for role-related operations
    private final EmployeeRoleRepository employeeRoleRepository;

    private final NotificationService notificationService;
    private final AddressService addressService;

    /**
     * Hash a password using BCrypt.
     * This method uses BCryptPasswordEncoder to hash the provided password.
     * @param password the plain text password to be hashed
     * @return the hashed password as a String
     */
    public String hashPassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }

    /**
     * Match a plain text password with a hashed password.
     * This method uses BCryptPasswordEncoder to check if the provided password matches the hashed password.
     * @param password the plain text password to be checked
     * @param hashedPassword the hashed password to compare against
     * @return true if the passwords match, false otherwise
     */
    public boolean matchPassword(String password, String hashedPassword) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(password, hashedPassword);
    }

    /**
     * Get all employees.
     *
     * @return List of EmployeeDTO containing details of all employees.
     */
    public List<EmployeeDTO> getAllEmployees() {
        // Fetch all employees from the repository
        List<Employee> employees = employeeRepository.findAll();
        return employeeMapper.toEmployeeDTOs(employees);
    }

    /**
     * Get employee by employee number.
     *
     * @param employeenr the unique identifier for the employee.
     * @return EmployeeDTO containing employee details.
     */
    public EmployeeDTO getEmployeeById(Integer employeenr) {
        Employee employee = employeeRepository.findById(employeenr)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found"));
        return employeeMapper.toEmployeeDTO(employee);
    }

    /**
     * Get employee by email.
     *
     * @param email the email for the employee.
     * @return AuthUserDTO containing employee details.
     */
    public AuthUserDTO getEmployeeByEmail(String email) {
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found"));
        return employeeMapper.toAuthUserDTO(employee);
    }

    /**
     * Get employee for setting
     *
     * @param email the email for the employee.
     * @return EmployeeDTO containing employee details.
     */
    public EmployeeDTO getEmployeeByEmailForSettings(String email) {
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found"));
        return employeeMapper.toEmployeeDTO(employee);
    }

    /**
     * Create a new employee.
     *
     * @param employeeDTO the EmployeeDTO object containing details of the employee to be created.
     * @return EmployeeDTO containing details of the created employee.
     */
    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {

        // Validate the input data
        Employee employee = new Employee();
        employee.setEmail(employeeDTO.getEmail());
        employee.setPhone(employeeDTO.getPhone());

        String hashedPassword = hashPassword(employeeDTO.getPassword());
        employee.setPasswordhash(hashedPassword);

        employee.setFirstname(employeeDTO.getFirstname());
        employee.setLastname(employeeDTO.getLastname());

        EmployeeRoleDTO roleDTO = employeeDTO.getRole();

        // Check if roleDTO is not null and has a valid role name
        if (roleDTO != null && roleDTO.getRolenr() != null) {
            EmployeeRole role = employeeRoleRepository.findById(roleDTO.getRolenr())
                    .orElseThrow(() -> new EntityNotFoundException("Role not found"));
            employee.setRole(role);
        } else {
            throw new IllegalArgumentException("Role must be provided");
        }

        employee.setEmployeddate(employeeDTO.getEmployeddate());
        employee.setPosition(employeeDTO.getPosition());
        employee.setImage(employeeDTO.getImage());

        // Set the address
        AddressDTO addressDTO = employeeDTO.getAddress();
        // Check if addressDTO is not null and has a valid address number
        if (addressDTO != null && addressDTO.getAddressnr() != null) {
            Address address = addressRepository.findById(addressDTO.getAddressnr())
                    .orElseThrow(() -> new EntityNotFoundException("Address not found"));
            employee.setAddress(address);
        }

        // Save the employee to the repository
        Employee savedEmployee = employeeRepository.save(employee);

        Employee refreshedEmployee = employeeRepository.findById(savedEmployee.getEmployeenr())
                .orElseThrow(() -> new EntityNotFoundException("Employee not found"));
        return employeeMapper.toEmployeeDTO(refreshedEmployee);
    }

    /**
     * Update an existing employee.
     *
     * @param employeenr the unique identifier for the employee to be updated.
     * @param employeeDTO the EmployeeDTO object containing updated details of the employee.
     * @return
     */
    public EmployeeDTO updateEmployee(Integer employeenr, EmployeeDTO employeeDTO) {
        // Validate the input data
        Employee employee = employeeRepository.findById(employeenr).orElseThrow(() -> new EntityNotFoundException("Employee not found"));
        // Update the employee's details
        employee.setEmail(employeeDTO.getEmail());
        employee.setPhone(employeeDTO.getPhone());

        EmployeeRoleDTO roleDTO = employeeDTO.getRole();

        // Check if roleDTO is not null and has a valid role name
        if (roleDTO != null && roleDTO.getRolenr() != null) {
            EmployeeRole role = employeeRoleRepository.findById(roleDTO.getRolenr())
                    .orElseThrow(() -> new EntityNotFoundException("Role not found"));;
            employee.setRole(role);
        } else {
            throw new IllegalArgumentException("Role must be provided");
        }

        employee.setFirstname(employeeDTO.getFirstname());
        employee.setLastname(employeeDTO.getLastname());
        employee.setPosition(employeeDTO.getPosition());
        employee.setIsactive(employeeDTO.isIsactive());
        employee.setImage(employeeDTO.getImage());
        // Set the address if provided
        Employee updatedEmployee = employeeRepository.save(employee);
        return employeeMapper.toEmployeeDTO(updatedEmployee);
    }

    /**
     * Delete an employee by employee number.
     *
     * @param employeenr the unique identifier for the employee to be deleted.
     */
    @Transactional
    public void deleteEmployee(Integer employeenr) {
        Employee employee = employeeRepository.findById(employeenr)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found"));
        if (employee.getAddress() != null) {
            addressService.deleteAddressByAddressNr(employee.getAddress().getAddressnr());
        }
        // Delete all notifications for this employee before deleting the employee
        notificationService.deleteNotificationsByEmployeenr(employeenr);
        addressService.deleteAddressByAddressNr(employee.getAddress().getAddressnr());
        employeeRepository.deleteById(employeenr);
    }
}
