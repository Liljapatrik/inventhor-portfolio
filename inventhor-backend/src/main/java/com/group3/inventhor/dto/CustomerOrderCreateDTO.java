package com.group3.inventhor.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Tatiana Fløisbonn
 *
 * The CustomerOrderCreateDTO class represents a Data Transfer Object for creating a customer order.
 * It is used to transfer order data between different layers of the application, such as from the service layer to the controller layer.
 *
 * @Data is a Lombok annotation that generates getter and setter methods for all fields in the class, also all arguments constructor
 * @NoArgsConstructor generates a no-argument constructor.
 */
@Data
@NoArgsConstructor
public class CustomerOrderCreateDTO {
    private CustomerDTO customer;
    private OrderStatusDTO status;
    private List<CustomerOrderProductCreateDTO> products;
    private CustomerPaymentDTO payment;

}
