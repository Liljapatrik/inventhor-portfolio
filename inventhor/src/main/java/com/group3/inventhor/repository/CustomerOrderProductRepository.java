package com.group3.inventhor.repository;

import com.group3.inventhor.model.CustomerOrderProduct;
import com.group3.inventhor.model.CustomerOrderProductId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Author Steewen Dennis Chanavi Holden
 *
 * The CustomerOrderProductRepository interface provides methods to interact with the CustomerOrderProduct entity in the database.
 * It extends JpaRepository, which provides basic CRUD operations.
 */

public interface CustomerOrderProductRepository extends JpaRepository<CustomerOrderProduct, CustomerOrderProductId> {
    List<CustomerOrderProduct> findByOrdernr(Integer ordernr);
}
