package com.group3.inventhor.service;

import com.group3.inventhor.dto.CustomerPaymentDTO;
import com.group3.inventhor.mapper.CustomerPaymentMapper;
import com.group3.inventhor.model.CustomerPayment;
import com.group3.inventhor.repository.CustomerPaymentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * @Author Steewen Dennis Chanavi Holden
 * The CustomerPaymentService class provides methods to manage payments for the Inventhor application.
 */

@Service
@RequiredArgsConstructor
public class CustomerPaymentService {

    private final CustomerPaymentRepository customerPaymentRepository;
    private final CustomerPaymentMapper customerPaymentMapper;

    /**
     * Get all payments.
     */
    public List<CustomerPaymentDTO> getAllPayments() {
        List<CustomerPayment> payments = customerPaymentRepository.findAll();
        return customerPaymentMapper.toCustomerPaymentDTOs(payments);
    }

    /**
     * Get payment by payment number.
     */
    public CustomerPaymentDTO getPaymentById(Integer paymentnr) {
        CustomerPayment payment = customerPaymentRepository.findById(paymentnr)
                .orElseThrow(() -> new EntityNotFoundException("Payment not found with nr: " + paymentnr));
        return customerPaymentMapper.toCustomerPaymentDTO(payment);
    }

    /**
     * Update a payment.
     */
    public CustomerPaymentDTO updatePayment(Integer paymentnr, CustomerPaymentDTO paymentDTO) {
        CustomerPayment payment = customerPaymentRepository.findById(paymentnr)
                .orElseThrow(() -> new EntityNotFoundException("Payment not found with nr: " + paymentnr));

        payment.setOrdernr(paymentDTO.getOrdernr());
        payment.setPaymentdate(paymentDTO.getPaymentdate());
        payment.setPaymentmethod(paymentDTO.getPaymentmethod());
        payment.setAmount(paymentDTO.getAmount());

        CustomerPayment updatedPayment = customerPaymentRepository.save(payment);
        return customerPaymentMapper.toCustomerPaymentDTO(updatedPayment);
    }

    /**
     * Delete a payment.
     */
    public void deletePayment(Integer paymentnr) {
        if (!customerPaymentRepository.existsById(paymentnr)) {
            throw new EntityNotFoundException("Payment not found with nr: " + paymentnr);
        }
        customerPaymentRepository.deleteById(paymentnr);
    }

    /**
     * Get payments for a specific order.
     */
    public List<CustomerPaymentDTO> getPaymentsByOrder(Integer ordernr) {
        List<CustomerPayment> payments = customerPaymentRepository.findByOrdernr(ordernr);
        return customerPaymentMapper.toCustomerPaymentDTOs(payments);
    }

    /**
     * Get all unpaid payments (Where paymentdate is null).
     */
    public List<CustomerPaymentDTO> getUnpaidPayments() {
        List<CustomerPayment> payments = customerPaymentRepository.findByPaymentdateIsNull();
        return customerPaymentMapper.toCustomerPaymentDTOs(payments);
    }

    /**
     * @author Tatiana Fløisbonn
     *
     * Delete all payments associated with a specific order.
     *
     * @param ordernr the order number for which payments should be deleted
     * @throws EntityNotFoundException if no payments are found for the given order number
     */
    public void deletePaymentByOrder(Integer ordernr) {
        List<CustomerPayment> payments = customerPaymentRepository.findByOrdernr(ordernr);
        if (payments.isEmpty()) {
            throw new EntityNotFoundException("No payments found for order nr: " + ordernr);
        }
        customerPaymentRepository.deleteAll(payments);
    }

    /**
     * @author Tatiana Fløisbonn
     *
     * Create a new payment.
     * @param paymentDTO the CustomerPaymentDTO object containing payment details
     * @return the created CustomerPaymentDTO object
     */
    public CustomerPaymentDTO createPayment(CustomerPaymentDTO paymentDTO) {
        CustomerPayment payment = customerPaymentMapper.toCustomerPayment(paymentDTO);
        CustomerPayment savedPayment = customerPaymentRepository.save(payment);
        return customerPaymentMapper.toCustomerPaymentDTO(savedPayment);
    }
}
