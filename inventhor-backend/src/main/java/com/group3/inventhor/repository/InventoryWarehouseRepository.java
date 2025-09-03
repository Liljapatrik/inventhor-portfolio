package com.group3.inventhor.repository;

import com.group3.inventhor.model.InventoryWarehouseId;
import org.springframework.data.jpa.repository.JpaRepository;
import com.group3.inventhor.model.InventoryWarehouse;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Nils Patrik Lilja
 *
 * The InventoryWarehouseRepository interface provides methods to interact with the Warehouse entity in the database.
 * It extends JpaRepository, which provides basic CRUD operations.
 */
@Repository
public interface InventoryWarehouseRepository extends JpaRepository<InventoryWarehouse, InventoryWarehouseId> {
    Optional<InventoryWarehouse> findById_WarehousenrAndId_Productnr(Integer warehousenr, Integer productnr);
    List<InventoryWarehouse> findById_Warehousenr(Integer warehousenr);
}
