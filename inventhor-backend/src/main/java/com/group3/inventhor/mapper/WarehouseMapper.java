package com.group3.inventhor.mapper;

import com.group3.inventhor.dto.WarehouseDTO;
import com.group3.inventhor.model.Warehouse;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

/**
 * @author Furo Muktar Eshetu
*/


@Mapper(
        componentModel= "spring",
        uses = AddressMapper.class,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)

public interface WarehouseMapper {

    WarehouseDTO toDTO(Warehouse warehouse); // entity to dto
    Warehouse toEntity(WarehouseDTO dto); // dto to entity
}

