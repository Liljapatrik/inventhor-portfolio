package com.group3.inventhor.repository;

import com.group3.inventhor.model.ProductSupplier;
import com.group3.inventhor.model.ProductSupplierId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing ProductSupplier entities.
 *
 * Extends JpaRepository to provide CRUD operations for ProductSupplier
 * with composite key ProductSupplierId.
 *
 * Provides additional query methods for:
 * - Finding all ProductSupplier entries by product ID.
 * - Finding all ProductSupplier entries by supplier ID.
 * - Finding a ProductSupplier by both supplier ID and product ID.
 * - Checking existence of a ProductSupplier by product ID and supplier ID.
 *
 * Author: Nils Patrik Lilja
 */
public interface ProductSupplierRepository extends JpaRepository<ProductSupplier, ProductSupplierId> {

    List<ProductSupplier> findByProduct_Productnr(Integer productnr);

    List<ProductSupplier> findBySupplier_Suppliernr(Integer suppliernr);

    Optional<ProductSupplier> findBySupplier_SuppliernrAndProduct_Productnr(Integer suppliernr, Integer productnr);

    boolean existsByProduct_ProductnrAndSupplier_Suppliernr(Integer productnr, Integer suppliernr);

    boolean existsBySupplier_Suppliernr(Integer suppliernr);
}
