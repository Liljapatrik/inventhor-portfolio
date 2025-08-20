package com.group3.inventhor.repository;

import com.group3.inventhor.model.Location;
import com.group3.inventhor.model.LocationId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Furo Muktar Eshetu
 *
 * The LocationRepository interface provides methods to interact with the Location entity in the database.
 * It extends JpaRepository, which provides basic CRUD operations.
 *
 * The @Repository annotation indicates that this interface is a Spring Data repository.
 */
@Repository
public interface LocationRepository extends JpaRepository<Location, LocationId> {
    @Query("SELECT CASE WHEN COUNT(l) > 0 THEN true ELSE false END " +
           "FROM Location l WHERE l.locationId.warehouse.warehousenr = :warehousenr " +
           "AND l.locationId.racknr = :racknr AND l.locationId.placenr = :placenr")
    boolean existsByRacknrAndPlacenrAndWarehouse(Integer racknr, Integer placenr, Integer warehousenr);

    @Query("SELECT l FROM Location l WHERE l.locationId.warehouse.warehousenr = :warehousenr " +
            "AND l.locationId.racknr = :racknr AND l.locationId.placenr = :placenr")
    Optional<Location> findByRacknrAndPlacenrAndWarehouse(Integer racknr, Integer placenr, Integer warehousenr);
}
