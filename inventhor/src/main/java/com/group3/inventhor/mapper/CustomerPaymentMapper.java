package com.group3.inventhor.mapper;

import com.group3.inventhor.dto.CustomerPaymentDTO;
import com.group3.inventhor.model.CustomerPayment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * @Author Steewen Dennis Chanavi Holden
 * The CustomerPaymentMapper interface is used to map between CustomerPayment and CustomerPaymentDTO objects.
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
public interface CustomerPaymentMapper {
    CustomerPaymentDTO toCustomerPaymentDTO(CustomerPayment customerPayment);
    CustomerPayment toCustomerPayment(CustomerPaymentDTO customerPaymentDTO);
    List<CustomerPaymentDTO> toCustomerPaymentDTOs(List<CustomerPayment> payments);
}