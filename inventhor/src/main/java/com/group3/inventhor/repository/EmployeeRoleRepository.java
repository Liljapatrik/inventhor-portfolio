package com.group3.inventhor.repository;

import com.group3.inventhor.model.EmployeeRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Tatiana Fløisbonn
 *
 * The EmployeeRoleRepository class is a placeholder for repository methods related to employee roles.
 * It extends JpaRepository to provide basic CRUD operations for EmployeeRole entities.
 *
 * @Repository annotation indicates that this class is a Spring Data repository.
 */
@Repository
public interface EmployeeRoleRepository extends JpaRepository <EmployeeRole, Integer>{
}
