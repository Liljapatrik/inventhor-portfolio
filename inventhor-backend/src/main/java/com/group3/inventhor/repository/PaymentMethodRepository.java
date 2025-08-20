package com.group3.inventhor.repository;


import com.group3.inventhor.model.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * @author Tatiana Fløisbonn
 *
 * The PaymentMethodRepository interface provides methods to interact with the PaymentMethod entity in the database.
 * It extends JpaRepository, which provides basic CRUD operations.
 *
 * The @Repository annotation indicates that this interface is a Spring Data repository.
 */
@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Integer> {

}
