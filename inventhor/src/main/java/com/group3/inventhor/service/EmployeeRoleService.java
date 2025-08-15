package com.group3.inventhor.service;

import com.group3.inventhor.dto.EmployeeRoleDTO;
import com.group3.inventhor.mapper.EmployeeRoleMapper;
import com.group3.inventhor.model.EmployeeRole;
import com.group3.inventhor.repository.EmployeeRoleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Tatiana Fløisbonn
 *
 * The EmployeeRoleService class is a service class that handles operations related to employee roles in the Inventhor application.
 *
 * @Service indicates that this class is a service component in the Spring framework.
 * @RequiredArgsConstructor generates a constructor with all required dependencies, allowing for dependency injection.
 */
@Service
@RequiredArgsConstructor
public class EmployeeRoleService {
    // The EmployeeRoleRepository instance used to interact with the database for employee role-related operations
    private final EmployeeRoleRepository employeeRoleRepository;
    // The EmployeeRoleMapper instance used to convert between EmployeeRole and EmployeeRoleDTO objects
    private final EmployeeRoleMapper employeeRoleMapper;

    /**
     * Get all employee roles.
     *
     *
     */
    public List<EmployeeRoleDTO> getAllEmployeeRoles() {
        // Fetch all employee roles from the repository
        List<EmployeeRole> employeeRoles = employeeRoleRepository.findAll();
        return employeeRoleMapper.toEmployeeRoleDTOs(employeeRoles);
    }

}
