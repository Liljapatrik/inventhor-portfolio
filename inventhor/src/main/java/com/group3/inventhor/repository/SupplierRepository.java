package com.group3.inventhor.repository;

import com.group3.inventhor.model.Supplier;
import com.group3.inventhor.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * @author Nils Patrik Lilja
 *
 * The SupplierRepository interface provides methods to interact with the Supplier entity in the database.
 * It extends JpaRepository, which provides basic CRUD operations.
 */
@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Integer> {
    Optional<Supplier> findByEmail(String email);
    Optional<Supplier> findByPhone(String phone);
    Optional<Supplier> findByWebsite(String website);

}
