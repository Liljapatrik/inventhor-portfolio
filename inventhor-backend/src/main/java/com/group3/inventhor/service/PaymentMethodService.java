package com.group3.inventhor.service;


import com.group3.inventhor.dto.PaymentMethodDTO;
import com.group3.inventhor.mapper.PaymentMethodMapper;
import com.group3.inventhor.model.PaymentMethod;
import com.group3.inventhor.repository.PaymentMethodRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Tatiana Fløisbonn
 *
 * The PaymentMethodService class provides methods to manage payment methods in the Inventhor application.
 * It includes methods to get, create, and retrieve payment methods.
 *
 * @Service indicates that this is a service class that contains business logic and interacts with the data access layer.
 * @RequiredArgsConstructor generates a constructor with all required dependencies.
 *
 * It will possible only to create or get information about payment methods.
 * Update methods are not available since payment methods are predefined and should not be modified by users.
 * Delete methods are also not available since we do not want to remove payment methods from the system.
 *
 */
@Service
@RequiredArgsConstructor
public class PaymentMethodService {

    // This service class is responsible for handling business logic related to payment methods.
    private final PaymentMethodRepository paymentMethodRepository;
    // The PaymentMethodMapper instance is used to convert between PaymentMethod and PaymentMethodDTO objects.
    private final PaymentMethodMapper paymentMethodMapper;

    /**
     * Get payment method by payment method number.
     *
     * @param paymentmethodnr the unique identifier for the payment method.
     * @return PaymentMethodDTO containing payment method details.
     */
    public PaymentMethodDTO getPaymentMethodByPaymentMethodnr(Integer paymentmethodnr) {
        // Find payment method by nr, throw exception if not found
        PaymentMethod paymentMethod = paymentMethodRepository.findById(paymentmethodnr)
                .orElseThrow(() -> new EntityNotFoundException("Payment method not found"));
        return paymentMethodMapper.toPaymentMethodDTO(paymentMethod);
    }

    /**
     * Get all payment methods.
     *
     * @return List of PaymentMethodDTO containing all payment methods.
     */
    public List<PaymentMethodDTO> getAllPaymentMethods() {
        // Retrieve all payment methods from the repository and convert them to DTOs
        List<PaymentMethod> paymentMethods = paymentMethodRepository.findAll();
        return paymentMethodMapper.toPaymentMethodDTOs(paymentMethods);
    }

    /**
     * Create a new payment method.
     *
     * @param paymentMethodDTO the PaymentMethodDTO containing payment method details to be created.
     * @return PaymentMethodDTO containing the created payment method details.
     */
    public PaymentMethodDTO createPaymentMethod(PaymentMethodDTO paymentMethodDTO) {
        // Convert DTO to entity, save it, and convert back to DTO
        PaymentMethod paymentMethod = paymentMethodMapper.toPaymentMethod(paymentMethodDTO);
        PaymentMethod savedPaymentMethod = paymentMethodRepository.save(paymentMethod);
        return paymentMethodMapper.toPaymentMethodDTO(savedPaymentMethod);
    }



}
