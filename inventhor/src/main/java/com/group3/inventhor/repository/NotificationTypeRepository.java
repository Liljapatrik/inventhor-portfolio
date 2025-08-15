package com.group3.inventhor.repository;


import com.group3.inventhor.model.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * @author Tatiana Fløisbonn
 *
 * The NotificationsTypeRepository interface provides methods to interact with the NotificationType entity in the database.
 * It extends JpaRepository, which provides basic CRUD operations.
 *
 * The @Repository annotation indicates that this interface is a Spring Data repository.
 */
@Repository
public interface NotificationTypeRepository extends JpaRepository<NotificationType, Integer> {
}
