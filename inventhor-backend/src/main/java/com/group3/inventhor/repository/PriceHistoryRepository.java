package com.group3.inventhor.repository;

import com.group3.inventhor.model.PriceHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Author Steewen Dennis Chanavi Holden
 *
 * The PriceHistoryRepository interface provides methods to interact with the PriceHistory entity in the database.
 * It extends JpaRepository, which provides basic CRUD operations.
 */

public interface PriceHistoryRepository extends JpaRepository<PriceHistory, Integer> {

    // Find all price history records by product number
    List<PriceHistory> findByProductnr(Integer productnr);
}
