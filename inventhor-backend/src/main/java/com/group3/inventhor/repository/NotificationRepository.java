package com.group3.inventhor.repository;


import com.group3.inventhor.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * @author Tatiana Fløisbonn
 *
 * The NotificationRepository interface provides methods to interact with the Notification entity in the database.
 * It extends JpaRepository, which provides basic CRUD operations.
 *
 * The @Repository annotation indicates that this interface is a Spring Data repository.
 */
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {

    @Query("SELECT n FROM Notification n WHERE n.employee.employeenr = :employeenr")
    List<Notification> findByEmployeeEmployeenr(Integer employeenr);
}
