package com.group3.inventhor.repository;


import com.group3.inventhor.model.LocationProduct;
import com.group3.inventhor.model.LocationProductId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Tatiana Fløisbonn
 *
 * The LocationProductRepository interface provides methods to interact with the LocationProduct entity in the database.
 * It extends JpaRepository, which provides basic CRUD operations.
 *
 * @Repository annotation indicates that this interface is a Spring Data repository.
 */
@Repository
public interface LocationProductRepository extends JpaRepository<LocationProduct, LocationProductId> {

    @Query("SELECT CASE WHEN COUNT(lp) > 0 THEN true ELSE false END " +
            "FROM LocationProduct lp WHERE lp.locationProductId.locationIdForLocationProduct.warehouse.warehousenr = :warehousenr " +
            "AND lp.locationProductId.locationIdForLocationProduct.racknr = :racknr " +
            "AND lp.locationProductId.locationIdForLocationProduct.placenr = :placenr " +
            "AND lp.locationProductId.product.productnr = :productnr ")
    boolean existsByRacknrAndPlacenrAndWarehouseAndProduct(Integer racknr, Integer placenr, Integer warehousenr, Integer productnr);

    @Query("SELECT lp FROM LocationProduct lp WHERE lp.locationProductId.locationIdForLocationProduct.warehouse.warehousenr = :warehousenr " +
            "AND lp.locationProductId.locationIdForLocationProduct.racknr = :racknr " +
            "AND lp.locationProductId.locationIdForLocationProduct.placenr = :placenr " +
            "AND lp.locationProductId.product.productnr = :productnr ")
    Optional<LocationProduct> findByRacknrAndPlacenrAndWarehouseAndProduct(Integer warehousenr, Integer racknr, Integer placenr, Integer productnr);

    @Query("SELECT lp FROM LocationProduct lp WHERE lp.locationProductId.locationIdForLocationProduct.warehouse.warehousenr = :warehousenr")
    List<LocationProduct> findByWarehousenr(Integer warehousenr);

    @Query("SELECT lp FROM LocationProduct lp WHERE lp.locationProductId.locationIdForLocationProduct.warehouse.warehousenr = :warehousenr" +
            " AND lp.locationProductId.product.productnr = :productnr")
    Optional<List<LocationProduct>> findByWarehousenrAndProductnr(Integer warehousenr, Integer productnr);

    // Sum og all quantities for each product in all locations in all warehouses
    @Query("SELECT SUM(lp.quantity) FROM LocationProduct lp WHERE lp.locationProductId.product.productnr = :productnr")
    Optional<Integer> sumQuantityByProductnr(Integer productnr);

    // Get locations by product number
    @Query("SELECT lp FROM LocationProduct lp WHERE lp.locationProductId.product.productnr = :productnr")
    List<LocationProduct> findLocationByProductnr (Integer productnr);
}
