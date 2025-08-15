package com.group3.inventhor.mapper;

import com.group3.inventhor.dto.CustomerDTO;
import com.group3.inventhor.model.Customer;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @author Tatiana Fløisbonn
 *
 * The CustomerMapper interface is used to map between Customer and CustomerDTO objects.
 * It uses MapStruct to generate the implementation at compile time.
 * In this case it converts Customer entities to CustomerDTOs and vice versa.
 * It also provides methods to convert lists of Customer entities to lists of CustomerDTOs.
 *
 * @Mapper annotation indicates that this interface is a MapStruct mapper.
 *      The componentModel = "spring" allows it to be used as a Spring bean.
 *      nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE means that null values in the source object will not overwrite existing values in the target object.
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
public interface CustomerMapper {
    CustomerDTO toCustomerDTO(Customer customer);

    Customer toCustomer(CustomerDTO customerDTO);

    List<CustomerDTO> toCustomerDTOs(List<Customer> customers);
}
