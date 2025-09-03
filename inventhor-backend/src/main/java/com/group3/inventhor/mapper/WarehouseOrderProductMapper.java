package com.group3.inventhor.mapper;

import com.group3.inventhor.dto.WarehouseOrderProductDTO;
import com.group3.inventhor.model.WarehouseOrderProduct;
import org.mapstruct.Mapper;

import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.Mapping;

/**
 * Mapper interface for converting between link WarehouseOrderProduct
 * entities and link WarehouseOrderProductDTOdata transfer objects.
 *
 * Uses MapStruct to generate mapping implementations for nested properties.
 *
 * Author: Nils Patrik Lilja
 */
@Mapper(
        componentModel = "spring",
        uses = AddressMapper.class,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface WarehouseOrderProductMapper {


    @Mapping(source = "warehouseOrder.ordernr", target = "ordernr")
    @Mapping(source = "product.productnr", target = "productnr")
    @Mapping(source = "product.name", target = "name")


    WarehouseOrderProductDTO toDTO(WarehouseOrderProduct warehouseOrderProduct);
}
