package com.group3.inventhor.mapper;

import com.group3.inventhor.dto.AddressDTO;
import com.group3.inventhor.model.Address;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

/**
 * @author Tatiana Fløisbonn
 *
 * The AddressMapper interface is used to map between Address and AddressDTO objects.
 * It uses MapStruct to generate the implementation at compile time.
 * In this case it converts Address entities to AddressDTOs and vice versa.
 * It also provides methods to convert lists of Address entities to lists of AddressDTOs.
 *
 * @Mapper annotation indicates that this interface is a MapStruct mapper.
 *      The componentModel = "spring" allows it to be used as a Spring bean.
 *      nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE means that null values in the source object will not overwrite existing values in the target object.
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface AddressMapper {
    AddressDTO toAddressDTO(Address address);
    Address toAddress(AddressDTO addressDTO);

    List<AddressDTO> toAddressDTOs(List<Address> addresses);
}
