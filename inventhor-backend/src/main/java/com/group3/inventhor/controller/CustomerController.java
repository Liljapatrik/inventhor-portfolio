package com.group3.inventhor.controller;


import com.group3.inventhor.dto.CustomerDTO;
import com.group3.inventhor.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Tatiana Fløisbonn
 *
 * The CustomerController class is a REST controller that handles HTTP requests related to customers in the Inventhor application.
 * It provides endpoints to get, create, update, and delete customers.
 *
 * @CrossOrigin allows cross-origin requests from specified origins.
 * @RestController indicates that this class is a REST controller.
 * @RequestMapping specifies the base path for all requests handled by this controller.
 * @Tag provides metadata (header and description) for Swagger documentation.
 * @RequiredArgsConstructor generates a constructor with all required dependencies.
 */
@CrossOrigin(origins = {"http://localhost:3000", "http://10.0.2.2:3000"})
@RestController
@RequestMapping("/customers")
@Tag(name="Customer Controller", description = "API for managing customers")
@RequiredArgsConstructor
public class CustomerController {

    // The CustomerService instance used to handle customer-related operations
    private final CustomerService customerService;

    /**
     * Get all customers.
     *
     * @Operation provides a summary and description for the endpoint in Swagger documentation.
     * @GetMapping handles HTTP GET requests to retrieve all customers.
     *
     * @return ResponseEntity containing a list of CustomerDTO with details of all customers.
     */
    @Operation(summary = "Get all customers", description = "Retrieve a list of all customers")
    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    /**
     * Get customer by nr.
     *
     * @param customernr the unique identifier for the customer
     * @return ResponseEntity containing CustomerDTO with customer details
     */
    @Operation(summary = "Get customer by nr", description = "Retrieve customer by nr")
    @GetMapping("/{customernr}")
    public ResponseEntity<CustomerDTO> getCustomerById(Integer customernr) {
        return ResponseEntity.ok(customerService.getCustomerById(customernr));
    }

    /**
     * Get customer by email.
     *
     * @param email the email of the customer
     * @return ResponseEntity containing CustomerDTO with customer details
     */
    @Operation(summary = "Get customer by email", description = "Retrieve customer by email")
    @GetMapping("/email/{email}")
    public ResponseEntity<CustomerDTO> getCustomerByEmail(@PathVariable String email) {
        return ResponseEntity.ok(customerService.getCustomerByEmail(email));
    }

    /**
     * Create a new customer.
     *
     * @Operation provides a summary and description for the endpoint in Swagger documentation.
     * @PostMapping handles HTTP POST requests to create a new customer.
     *
     * @param customerDTO the CustomerDTO containing customer details to be created
     * @return ResponseEntity containing the created CustomerDTO
     *
     * @RequestesBody indicates that the method expects a request body containing the customer data in JSON format.
     */
    @Operation(summary = "Create customer", description = "Create new customer")
    @PostMapping
    public ResponseEntity<CustomerDTO> createCustomer(@RequestBody CustomerDTO customerDTO) {
        return ResponseEntity.ok(customerService.createCustomer(customerDTO));
    }

    /**
     * Update customer information.
     *
     * @Operation provides a summary and description for the endpoint in Swagger documentation.
     * @PutMapping handles HTTP PUT requests to update an existing customer.
     *
     * @param customernr the unique identifier for the customer to be updated
     * @param customerDTO the CustomerDTO containing updated customer details
     * @return ResponseEntity containing the updated CustomerDTO
     *
     * @PathVariable indicates that the method expects a path variable containing the customer number.
     */
    @Operation(summary = "Update customer", description = "Update information about customer")
    @PutMapping("/{customernr}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable Integer customernr, @RequestBody CustomerDTO customerDTO) {
        return ResponseEntity.ok(customerService.updateCustomer(customernr, customerDTO));
    }

    @Operation(summary = "Delete customer", description = "Delete customer from DB")
    @DeleteMapping("/{customernr}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Integer customernr) {
        customerService.deleteCustomer(customernr);
        return ResponseEntity.noContent().build();
    }
}
