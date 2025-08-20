package com.group3.inventhor.repository;

import com.group3.inventhor.model.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
* @author Furo Muktar Eshetu
* the warehouserepository interface provides CRUD operations for the warehouse entity
* It extends JpaRepository, which provides basic CRUD operations.
*/

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Integer> {
}

