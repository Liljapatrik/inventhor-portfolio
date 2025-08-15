package com.group3.inventhor.mapper;

import com.group3.inventhor.dto.CustomerOrderDTO;
import com.group3.inventhor.model.CustomerOrder;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @Author Steewen Dennis Chanavi Holden
 * The CustomerOrderMapper interface is used to map between CustomerOrder and CustomerOrderDTO objects.
 */

@Mapper(componentModel = "spring")
public interface CustomerOrderMapper {
    CustomerOrderDTO toCustomerOrderDTO(CustomerOrder entity);
    CustomerOrder toCustomerOrderCreateDTO(CustomerOrderDTO dto);
    List<CustomerOrderDTO> toCustomerOrderDTOs(List<CustomerOrder> entities);
}

