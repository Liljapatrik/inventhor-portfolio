package com.group3.inventhor.repository;

import com.group3.inventhor.model.SellingHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Author Steewen Dennis Chanavi Holden
 *
 * The SellingHistoryRepository interface provides methods to interact with the SellingHistory entity in the database.
 * It extends JpaRepository, which provides basic CRUD operations.
 */

public interface SellingHistoryRepository extends JpaRepository<SellingHistory, Integer> {

    // Find all selling history records by product number
    List<SellingHistory> findByProductnr(Integer productnr);
}
