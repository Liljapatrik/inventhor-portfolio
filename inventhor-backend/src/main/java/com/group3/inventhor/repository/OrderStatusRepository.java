package com.group3.inventhor.repository;


import com.group3.inventhor.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * @author Tatiana Fløisbonn
 *
 * The OrderStatusRepository interface provides methods to interact with the OrderStatus entity in the database.
 * It extends JpaRepository, which provides basic CRUD operations.
 *
 * The @Repository annotation indicates that this interface is a Spring Data repository.
 */
@Repository
public interface OrderStatusRepository extends JpaRepository<OrderStatus, Integer> {
    Optional<OrderStatus> findByNameIgnoreCase(String name);
}
