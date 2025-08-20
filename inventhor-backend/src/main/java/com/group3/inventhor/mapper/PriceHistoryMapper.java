package com.group3.inventhor.mapper;

import com.group3.inventhor.dto.PriceHistoryDTO;
import com.group3.inventhor.model.PriceHistory;
import org.mapstruct.Mapper;
import java.util.List;

/**
 * @Author Steewen Dennis Chanavi Holden
 * The PriceHistoryMapper interface is used to map between PriceHistory and PriceHistoryDTO objects.
 */

@Mapper(componentModel = "spring")
public interface PriceHistoryMapper {
    PriceHistoryDTO toPriceHistoryDTO(PriceHistory entity);
    List<PriceHistoryDTO> toPriceHistoryDTOs(List<PriceHistory> entities);
}
