package com.group3.inventhor.mapper;


import com.group3.inventhor.dto.WarehouseOrderDTO;
import com.group3.inventhor.model.WarehouseOrder;
import org.mapstruct.Mapper;

import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.Mapping;

/**
 * Mapper interface for converting between link WarehouseOrder entities and link WarehouseOrderDTO data transfer objects.
 *
 * Uses MapStruct to generate implementation for mapping nested properties from related entities.
 *
 * Configured with:
 * - componentModel = "spring" to enable Spring dependency injection
 * - Uses link AddressMapper for mapping nested address properties
 * - Null value properties are ignored to avoid overwriting existing values with null
 *
 * Maps the properties
 *
 * Author: Nils Patrik Lilja
 */
@Mapper(
        componentModel = "spring",
        uses = AddressMapper.class,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface WarehouseOrderMapper {

    @Mapping(source = "warehouse.warehousenr", target = "warehousenr")
    @Mapping(source = "warehouse.name", target = "name")
    @Mapping(source = "supplier.suppliernr", target = "suppliernr")
    @Mapping(source = "supplier.name", target = "suppliername")
    @Mapping(source = "status.name", target = "orderstatusname")
    @Mapping(source = "status.statusnr", target = "statusnr")

    WarehouseOrderDTO toDTO(WarehouseOrder warehouseOrder);
}
