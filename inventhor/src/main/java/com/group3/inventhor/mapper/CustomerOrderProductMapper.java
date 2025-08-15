package com.group3.inventhor.mapper;

import com.group3.inventhor.dto.CustomerOrderProductDTO;
import com.group3.inventhor.model.CustomerOrderProduct;
import org.mapstruct.Mapper;

import java.util.List;


/**
 * @Author Steewen Dennis Chanavi Holden
 * The CustomerOrderProductMapper interface is used to map between CustomerOrderProduct and CustomerOrderProductDTO objects.
 */
@Mapper(componentModel = "spring")
public interface CustomerOrderProductMapper {
    CustomerOrderProductDTO toCustomerOrderProductDTO(CustomerOrderProduct entity);
    CustomerOrderProduct toCustomerOrderProduct(CustomerOrderProductDTO dto);
    List<CustomerOrderProductDTO> toCustomerOrderProductDTOs(List<CustomerOrderProduct> entities);
}
