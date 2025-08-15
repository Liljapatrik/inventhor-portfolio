package com.group3.inventhor.repository;

import com.group3.inventhor.model.CustomerOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author Steewen Dennis Chanavi Holden
 *
 * The CustomerOrderRepository interface provides methods to interact with the CustomerOrder entity in the database.
 * It extends JpaRepository, which provides basic CRUD operations.
 */
@Repository
public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Integer> {
    List<CustomerOrder> findByOrderdateBetween(LocalDateTime startDate, LocalDateTime endDate);
}