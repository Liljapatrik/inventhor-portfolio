package com.group3.inventhor.controller;

import com.group3.inventhor.dto.AuthUserDTO;
import com.group3.inventhor.dto.EmployeeDTO;
import com.group3.inventhor.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

/**
 * @author Tatiana Fløisbonn
 *
 * The EmployeeController class is a REST controller that handles HTTP requests related to employees in the Inventhor application.
 * It provides endpoints to get, create, update, and delete employees.
 *
 * @CrossOrigin allows cross-origin requests from specified origins.
 * @RestController indicates that this class is a REST controller.
 * @RequestMapping specifies the base path for all requests handled by this controller.
 * @Tag provides metadata (header and description) for Swagger documentation.
 * @RequiredArgsConstructor generates a constructor with all required dependencies.
 */
@CrossOrigin(origins = {"http://localhost:3000", "http://10.0.2.2:3000"})
@RestController
@RequestMapping("/employees")
@Tag(name="Employee Controller", description = "API for managing users")
@RequiredArgsConstructor
public class EmployeeController {


    private final EmployeeService employeeService;

    /**
     * Get all employees.
     *
     * @Operation provides a summary and description for the endpoint in Swagger documentation.
     * @GetMapping handles HTTP GET requests to retrieve all employees.
     *
     * @return ResponseEntity containing a list of EmployeeDTO with details of all employees.
     */
    @Operation(summary = "Get all employee", description = "Retrieve a list of all employees")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    /**
     * Get employee by nr.
     *
     * @param employeenr the unique identifier for the employee
     * @return ResponseEntity containing EmployeeDTO with employee details
     *
     * @PathVariable indicates that the method expects a path variable containing the customer number.
     */
    @Operation(summary = "Get employee by employeenr", description = "Retrieve employee by employeenr")
    @GetMapping("/{employeenr}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable Integer employeenr) {
        return ResponseEntity.ok(employeeService.getEmployeeById(employeenr));
    }

    /**
     * Get employee by nr.
     *
     * @param email the email for the employee
     * @return ResponseEntity containing EmployeeDTO with employee details
     *
     * @PathVariable indicates that the method expects a path variable containing the customer number.
     */
    @Operation(summary = "Get employee by email", description = "Retrieve employee by email")
    @GetMapping("/email/{email}")
    @PreAuthorize("#email == authentication.tokenAttributes['email']")
    public ResponseEntity<AuthUserDTO> getEmployeeByEmail(@PathVariable String email) {
        return ResponseEntity.ok(employeeService.getEmployeeByEmail(email));
    }

    /**
     * Get employee for setting
     * @param email the email for the employee
     * @return ResponseEntity containing EmployeeDTO with employee details
     *
     * @PathVariable indicates that the method expects a path variable containing the customer number.
     *
     */
    @Operation(summary = "Get employee by email", description = "Retrieve employee by email")
    @GetMapping("/email-for-settings/{email}")
    @PreAuthorize("#email == authentication.tokenAttributes['email']")
    public ResponseEntity<EmployeeDTO> getEmployeeByEmailForSettings(@PathVariable String email) {
        return ResponseEntity.ok(employeeService.getEmployeeByEmailForSettings(email));
    }

    /**
     * Create a new employee.
     *
     * @PostMapping handles HTTP POST requests to create a new employee.
     *
     * @param employeeDTO the EmployeeDTO containing details of the new employee
     * @return ResponseEntity containing created EmployeeDTO
     *
     * @RequestesBody indicates that the method expects a request body containing the customer data in JSON format.
     */
    @Operation(summary = "Create employee", description = "Create new employee")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmployeeDTO> createEmployee(@RequestBody EmployeeDTO employeeDTO) {
        return ResponseEntity.ok(employeeService.createEmployee(employeeDTO));
    }

    /**
     * Update an existing employee.
     *
     * @PutMapping handles HTTP PUT requests to update an existing employee.
     *
     * @param employeenr the unique identifier for the employee to be updated
     * @param employeeDTO the EmployeeDTO containing updated employee details
     * @return ResponseEntity containing updated EmployeeDTO
     */
    @Operation(summary = "Update employee", description = "Update information about employee")
    @PutMapping("/{employeenr}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EmployeeDTO> updateEmployee(@PathVariable Integer employeenr, @RequestBody EmployeeDTO employeeDTO) {
        return ResponseEntity.ok(employeeService.updateEmployee(employeenr, employeeDTO));
    }

    /**
     * Authenticated employee can update their own information.

     * @param employeeDTO the EmployeeDTO containing updated employee details
     * @return ResponseEntity containing updated EmployeeDTO
     */
    @Operation(summary = "Update own employee information", description = "Authenticated employee can update their own information")
    @PutMapping("/update/{employeenr}/{email}")
    public ResponseEntity<EmployeeDTO> updateEmployee(@RequestBody EmployeeDTO employeeDTO,  @PathVariable Integer employeenr, @AuthenticationPrincipal(expression = "claims['email']") String email) {
        if (!employeeDTO.getEmail().equals(email)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(employeeService.updateEmployee(employeenr, employeeDTO));
    }


    /**
     * Delete an employee by employeenr.
     *
     * @DeleteMapping handles HTTP DELETE requests to delete an employee by its unique identifier (employeenr).
     *
     * @param employeenr the unique identifier for the employee to be deleted
     * @return ResponseEntity with no content status
     */
    @Operation(summary = "Delete employee", description = "Delete employee from DB")
    @DeleteMapping("/{employeenr}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Integer employeenr) {
        employeeService.deleteEmployee(employeenr);
        return ResponseEntity.noContent().build();
    }
}
