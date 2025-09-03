package com.group3.inventhor.repository;

import com.group3.inventhor.model.WarehouseOrderProduct;
import com.group3.inventhor.model.WarehouseOrderProductId;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing WarehouseOrderProduct entities.
 *
 * Extends JpaRepository to provide CRUD operations for WarehouseOrderProduct
 * with composite key WarehouseOrderProductId.
 *
 * Contains custom query methods to:
 * - Find a WarehouseOrderProduct by order number and product number.
 * - Find all WarehouseOrderProduct entries by order number.
 * - Delete all WarehouseOrderProduct entries by order number.
 *
 * Author: Nils Patrik Lilja
 */
@Repository
public interface WarehouseOrderProductRepository extends JpaRepository<WarehouseOrderProduct, WarehouseOrderProductId> {

    Optional<WarehouseOrderProduct> findByWarehouseOrder_OrdernrAndProduct_Productnr(Integer productnr, Integer ordernr);
    List<WarehouseOrderProduct> findByWarehouseOrder_Ordernr(Integer ordernr);
    void deleteAllByWarehouseOrder_Ordernr(Integer ordernr);
}
