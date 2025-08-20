package com.group3.inventhor.service;


import com.group3.inventhor.dto.AddressDTO;
import com.group3.inventhor.dto.CustomerDTO;
import com.group3.inventhor.mapper.CustomerMapper;
import com.group3.inventhor.model.Address;
import com.group3.inventhor.model.Customer;
import com.group3.inventhor.repository.AddressRepository;
import com.group3.inventhor.repository.CustomerRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Tatiana Fløisbonn
 *
 * The CustomerService class provides methods to manage customers in the Inventhor application.
 * It includes methods to get, create, update, and delete customers.
 *
 * @Service indicates that this is a service class that contains business logic and interacts with the data access layer.
 * @RequiredArgsConstructor generates a constructor with all required dependencies.
 */
@Service
@RequiredArgsConstructor
public class CustomerService {

    // The CustomerRepository instance used to interact with the database for customer-related operations
    private final CustomerRepository customerRepository;
    // The AddressRepository instance used to interact with the database for address-related operations
    private final AddressRepository addressRepository;
    // The CustomerMapper instance used to convert between Customer and CustomerDTO objects
    private final CustomerMapper customerMapper;

    /**
     * Get all customers.
     *
     * @return List of CustomerDTO containing details of all customers.
     */
    public List<CustomerDTO> getAllCustomers() {
        // Fetch all customers from the repository
        List<Customer> customers = customerRepository.findAll();
        return customerMapper.toCustomerDTOs(customers);
    }

    /**
     * Get customer by nr.
     *
     * @param customernr the unique identifier for the customer
     * @return CustomerDTO containing customer details
     */
    public CustomerDTO getCustomerById(Integer customernr) {
        Customer customer = customerRepository.findById(customernr).orElseThrow(() -> new EntityNotFoundException("Customer not found"));
        return customerMapper.toCustomerDTO(customer);
    }

    /**
     * Get customer by email
     *
     * @param email the email of the customer
     * @return CustomerDTO containing customer details
     */
    public CustomerDTO getCustomerByEmail(String email) {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found with email: " + email));
        return customerMapper.toCustomerDTO(customer);
    }

    /**
     * Create a new customer.
     *
     * @param customerDTO the CustomerDTO containing customer details to be created
     * @return CustomerDTO containing the created customer details
     */
    public CustomerDTO createCustomer(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        customer.setEmail(customerDTO.getEmail());
        customer.setFirstname(customerDTO.getFirstname());
        customer.setLastname(customerDTO.getLastname());
        customer.setPhone(customerDTO.getPhone());

        AddressDTO addressDTO = customerDTO.getAddress();
        if (addressDTO != null && addressDTO.getAddressnr() != null) {
            Address address = addressRepository.findById(addressDTO.getAddressnr())
                    .orElseThrow(() -> new EntityNotFoundException("Address not found"));
            customer.setAddress(address);
        }

        Customer savedCustomer = customerRepository.save(customer);
        return customerMapper.toCustomerDTO(savedCustomer);
    }


    /**
     * Update an existing customer.
     *
     * @param customernr the unique identifier for the customer to be updated
     * @param customerDTO the CustomerDTO containing updated customer details
     * @return CustomerDTO containing updated customer details
     */
    public CustomerDTO updateCustomer(Integer customernr, CustomerDTO customerDTO) {
        Customer customer = customerRepository.findById(customernr).orElseThrow(() -> new EntityNotFoundException("Customer not found"));
        customer.setEmail(customerDTO.getEmail());
        customer.setFirstname(customerDTO.getFirstname());
        customer.setLastname(customerDTO.getLastname());
        customer.setPhone(customerDTO.getPhone());

        Customer updatedCustomer = customerRepository.save(customer);
        return customerMapper.toCustomerDTO(updatedCustomer);
    }

    /**
     * Delete a customer by customernr.
     *
     * @param customernr the unique identifier for the customer to be deleted
     */
    public void deleteCustomer(Integer customernr) {
        Customer customer = customerRepository.findById(customernr).orElseThrow(() -> new EntityNotFoundException("Customer not found"));
        customerRepository.delete(customer);
    }
}
