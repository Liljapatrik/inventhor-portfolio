package com.group3.inventhor.controller;

import com.group3.inventhor.dto.EmployeeRoleDTO;
import com.group3.inventhor.service.EmployeeRoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Tatiana Fløisbonn
 *
 * The EmployeeRoleController class is a REST controller that handles HTTP requests related to employee roles in the Inventhor application.
 * It provides endpoints to manage employee roles.
 *
 * @CrossOrigin allows cross-origin requests from specified origins.
 * @RestController indicates that this class is a REST controller.
 * @RequestMapping specifies the base path for all requests handled by this controller.
 * @Tag provides metadata (header and description) for Swagger documentation.
 * @RequiredArgsConstructor generates a constructor with all required dependencies.
 */
@CrossOrigin(origins = {"http://localhost:3000", "http://10.0.2.2:3000"} )
@RestController
@RequestMapping("/employee-roles")
@Tag(name="Employee Role Controller", description = "API for managing employee roles")
@RequiredArgsConstructor
public class EmployeeRoleController {

    // The EmployeeRoleService instance used to handle employee role-related operations
    private final EmployeeRoleService employeeRoleService;

    /**
     * Get all employee roles.
     * This endpoint retrieves all employee roles from the service layer and returns them as a list of EmployeeRoleDTOs.
     *
     * @Operation provides metadata for the endpoint, including a summary and description for Swagger documentation.
     * @GetMapping specifies that this method handles GET requests to the "/employee-roles" endpoint.
     *
     * @return ResponseEntity containing a list of EmployeeRoleDTOs and an HTTP status code.
     */
    @Operation(summary = "Get all employee roles", description = "Retrieves all employee roles from the service layer")
    @GetMapping
    public ResponseEntity<List<EmployeeRoleDTO>> getAllEmployeeRoles() {
        // Call the service to get all employee roles and return them as a ResponseEntity
        List<EmployeeRoleDTO> employeeRoles = employeeRoleService.getAllEmployeeRoles();
        return ResponseEntity.ok(employeeRoles);
    }
}
