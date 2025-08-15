package com.group3.inventhor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.group3.inventhor.model.WarehouseOrder;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * @author Nils Patrik Lilja
 *
 * The WarehoudeOrderRepository interface provides methods to interact with the Warehouse entity in the database.
 * It extends JpaRepository, which provides basic CRUD operations.
 */
@Repository
public interface WarehouseOrderRepository extends JpaRepository<WarehouseOrder, Integer> {
}
