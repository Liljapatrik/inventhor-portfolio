package com.group3.inventhor.repository;

import com.group3.inventhor.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
/**
 * @author Tatiana Fløisbonn
 *
 * The EmployeeRepository interface provides methods to interact with the Employee entity in the database.
 * It extends JpaRepository, which provides basic CRUD operations.
 *
 * The @Repository annotation indicates that this interface is a Spring Data repository.
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    Optional<Employee> findByEmail(String email);

}