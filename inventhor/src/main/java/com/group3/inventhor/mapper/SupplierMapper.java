package com.group3.inventhor.mapper;

import com.group3.inventhor.dto.SupplierDTO;
import com.group3.inventhor.model.Supplier;
import org.mapstruct.Mapper;

import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.Mapping;

/**
 * Mapper interface for converting between link Supplier entities and link SupplierDTO data transfer objects.
 *
 * Uses MapStruct to automatically generate mapping implementations.
 *
 * Configured to use link AddressMapper for nested address mapping.
 * Ignores null properties during mapping to avoid overwriting existing values with null.
 *
 * Maps the 'contactperson' property from the Supplier entity to the 'contact' field in SupplierDTO.
 *
 * Author: Nils Patrik Lilja
 */
@Mapper(
        componentModel = "spring",
        uses = AddressMapper.class,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface SupplierMapper {

    @Mapping(source = "contactperson", target = "contact")
    @Mapping(source = "address", target = "address")
    SupplierDTO toDTO(Supplier supplier);
}
