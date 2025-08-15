package com.group3.inventhor.repository;

import com.group3.inventhor.dto.CustomerPaymentDTO;
import com.group3.inventhor.model.CustomerPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author Steewen Dennis Chanavi Holden
 * The CustomerPaymentRepository interface provides methods to interact with the CustomerPayment entity.
 */
@Repository
public interface CustomerPaymentRepository extends JpaRepository<CustomerPayment, Integer> {
    List<CustomerPayment> findByOrdernr(Integer ordernr);
    List<CustomerPayment> findByPaymentdateIsNull();
}