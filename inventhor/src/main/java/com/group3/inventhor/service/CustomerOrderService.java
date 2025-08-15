package com.group3.inventhor.service;

import com.group3.inventhor.dto.*;
import com.group3.inventhor.mapper.CustomerOrderMapper;
import com.group3.inventhor.model.*;
import com.group3.inventhor.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author Steewen Dennis Chanavi Holden
 * The CustomerOrderService class provides methods to manage customer orders for the Inventhor application.
 */

@Service
@RequiredArgsConstructor
public class CustomerOrderService {
    private final CustomerOrderRepository orderRepo;
    private final CustomerOrderMapper customerOrderMapper;
    private final CustomerRepository customerRepository;
    private final OrderStatusRepository orderStatusRepository;

    private final CustomerPaymentService customerPaymentService;
    private final CustomerOrderProductService customerOrderProductService;
    private final AddressRepository addressRepository;
    private final ProductRepository productRepository;
    private final CustomerOrderProductRepository customerOrderProductRepository;
    private final CustomerPaymentRepository customerPaymentRepository;
    private final WarehouseRepository warehouseRepository;
    private final LocationProductRepository locationProductRepository;

    /**
     * Get all Orders
     */
    public List<CustomerOrderDTO> getAllOrders() {
        return customerOrderMapper.toCustomerOrderDTOs(orderRepo.findAll());
    }

    /**
     * Get order by ID
     */
    public CustomerOrderDTO getOrderById(Integer ordernr) {
        return customerOrderMapper.toCustomerOrderDTO(
                orderRepo.findById(ordernr).orElseThrow(() -> new EntityNotFoundException("Order not found")));
    }

    /*
    /**
     * Create an order

    public CustomerOrderDTO createOrder(CustomerOrderDTO dto) {
        CustomerOrder order = customerOrderMapper.toCustomerOrder(dto);
        order.setOrderdate(LocalDateTime.now());
        return customerOrderMapper.toCustomerOrderDTO(orderRepo.save(order));
    }
    */

    /**
     * Update an order
     */
    public CustomerOrderDTO updateOrder(Integer ordernr, CustomerOrderDTO dto) {
        CustomerOrder order = orderRepo.findById(ordernr).orElseThrow(() -> new EntityNotFoundException("Order not found"));

        CustomerDTO customerDTO = dto.getCustomer();
        if (customerDTO != null && customerDTO.getCustomernr() != null) {
            Customer customer = customerRepository.findById(customerDTO.getCustomernr())
                    .orElseThrow(() -> new EntityNotFoundException("Customer not found"));
            order.setCustomer(customer);
        } else {
            throw new EntityNotFoundException("Customer information is required for updating the order.");
        }

        OrderStatusDTO statusDTO = dto.getStatus();
        if (statusDTO != null && statusDTO.getStatusnr() != null) {
            OrderStatus status = orderStatusRepository.findById(statusDTO.getStatusnr())
                    .orElseThrow(() -> new EntityNotFoundException("Order status not found"));
            order.setStatus(status);
        } else {
            throw new EntityNotFoundException("Order status information is required for updating the order.");
        }
        order.setDeliverydate(dto.getDeliverydate());
        return customerOrderMapper.toCustomerOrderDTO(orderRepo.save(order));
    }

    /**
     * @author Steewen Dennis Chanavi Holden and Tatiana Fløisbonn
     * Delete an order
     */
    public void deleteOrder(Integer ordernr) {
        CustomerOrder order = orderRepo.findById(ordernr)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        customerPaymentService.deletePaymentByOrder(ordernr);
        customerOrderProductService.deleteOrderProductsByOrder(ordernr);
        orderRepo.delete(order);

    }

    /**
     * @author Tatiana Fløisbonn
     *
     * Create a new order together with its products and payment. Customer can be also created if he/she not exists.
     *
     * @Transactional ensures that all operations are performed atomically.
     *
     * @param customerOrderCreateDTO the DTO containing order details, customer information, products, and payment
     * @return the created CustomerOrderCreateDTO object
     */
    /*@Transactional*/
    public CustomerOrderDTO createOrder (CustomerOrderCreateDTO customerOrderCreateDTO) {

        // Check if customer exists by email, if not create a new customer with address
        // If customer exists, retrieve the customer by email
        CustomerDTO customerDTO = customerOrderCreateDTO.getCustomer();
        Customer customer = null;

        if (customerDTO != null && customerDTO.getEmail() != null) {
            customer = customerRepository.findByEmail(customerDTO.getEmail()).orElse(null);
        }

        if (customer == null) {
            // Create a new Customer entity from DTO
            customer = new Customer();
            customer.setFirstname(customerDTO.getFirstname());
            customer.setLastname(customerDTO.getLastname());
            customer.setEmail(customerDTO.getEmail());
            // Set the address if provided
            AddressDTO addressDTO = customerDTO.getAddress();
            if (addressDTO != null && addressDTO.getAddressnr() != null) {
                Address address = addressRepository.findById(addressDTO.getAddressnr())
                        .orElseThrow(() -> new EntityNotFoundException("Address not found"));
                customer.setAddress(address);
            }
            customer = customerRepository.save(customer);
        }

        // Create a new CustomerOrder
        CustomerOrder customerOrder = new CustomerOrder();

        customerOrder.setCustomer(customer);
        customerOrder.setOrderdate(LocalDateTime.now());
        customerOrder.setDeliverydate(null);

        // Check if status exists
        OrderStatusDTO orderStatusDTO = customerOrderCreateDTO.getStatus();

        if (orderStatusDTO != null && orderStatusDTO.getStatusnr() != null) {
            OrderStatus orderStatus = orderStatusRepository.findById(orderStatusDTO.getStatusnr())
                    .orElseThrow(() -> new EntityNotFoundException("Order status not found"));
            customerOrder.setStatus(orderStatus);
        } else {
            throw new IllegalArgumentException("Order status must be provided");
        }

        // save the order to ensure it has an ID
        customerOrder = orderRepo.save(customerOrder);

        // Create and save CustomerOrderProducts for the new order
        List<CustomerOrderProductCreateDTO> orderProducts = customerOrderCreateDTO.getProducts();

        BigDecimal amount = BigDecimal.ZERO;

        if (orderProducts != null && !orderProducts.isEmpty()) {
            for (CustomerOrderProductCreateDTO orderProductCreateDTO : orderProducts) {

                Product product = productRepository.findById(orderProductCreateDTO.getProductnr()).orElseThrow(() -> new EntityNotFoundException("Product not found"));
                Warehouse warehouse = warehouseRepository.findById(orderProductCreateDTO.getWarehousenr()).orElseThrow(() -> new EntityNotFoundException("Warehouse not found"));

                CustomerOrderProduct customerOrderProduct = new CustomerOrderProduct();
                customerOrderProduct.setCustomerOrder(customerOrder);
                customerOrderProduct.setOrdernr(customerOrder.getOrdernr());
                customerOrderProduct.setProduct(product);
                customerOrderProduct.setProductnr(product.getProductnr());
                customerOrderProduct.setWarehousenr(orderProductCreateDTO.getWarehousenr());
                customerOrderProduct.setWarehouse(warehouse);
                customerOrderProduct.setWarehousenr(orderProductCreateDTO.getWarehousenr());
                customerOrderProduct.setQuantity(orderProductCreateDTO.getQuantity());

                customerOrderProductRepository.save(customerOrderProduct);

                // Calculate the total amount for the order
                amount = amount.add(product.getSellprice().multiply(BigDecimal.valueOf(orderProductCreateDTO.getQuantity())));
            }

        } else {
            throw new IllegalArgumentException("At least one product must be provided for the order.");
        }

        CustomerPayment payment = new CustomerPayment();
        payment.setOrdernr(customerOrder.getOrdernr());
        payment.setAmount(amount);
        payment.setPaymentdate(customerOrderCreateDTO.getPayment().getPaymentdate());
        payment.setPaymentmethod(customerOrderCreateDTO.getPayment().getPaymentmethod());

        customerPaymentRepository.save(payment);

        // Decrease the stock for each product in the order
        for (CustomerOrderProductCreateDTO orderProductCreateDTO : orderProducts) {

            List<LocationProduct> locationProducts = locationProductRepository.findByWarehousenrAndProductnr(
                            orderProductCreateDTO.getWarehousenr(), orderProductCreateDTO.getProductnr())
                    .orElseThrow(() -> new EntityNotFoundException("LocationProduct not found"));

            Integer quantityleft = orderProductCreateDTO.getQuantity();

            for (LocationProduct locationProduct : locationProducts) {

                if (locationProduct.getQuantity().compareTo(BigDecimal.ZERO) <= 0) {
                    continue;
                }

                Integer availableQuantity = locationProduct.getQuantity().intValue();
                if (availableQuantity >= quantityleft) {
                    locationProduct.setQuantity(locationProduct.getQuantity().subtract(BigDecimal.valueOf(quantityleft)));
                    quantityleft = 0;
                    locationProductRepository.save(locationProduct);
                } else {
                    locationProduct.setQuantity(BigDecimal.ZERO);
                    quantityleft -= availableQuantity;
                    locationProductRepository.delete(locationProduct);
                }

                if (quantityleft <= 0) {
                    break;
                }
            }
        }

        return customerOrderMapper.toCustomerOrderDTO(customerOrder);
    }

}