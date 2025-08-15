package com.group3.inventhor.mapper;

import com.group3.inventhor.dto.SellingHistoryDTO;
import com.group3.inventhor.model.SellingHistory;
import org.mapstruct.Mapper;
import java.util.List;

/**
 * @Author Steewen Dennis Chanavi Holden
 * The SellingHistoryMapper interface is used to map between SellingHistory and SellingHistoryDTO objects.
 */

@Mapper(componentModel = "spring")
public interface SellingHistoryMapper {
    SellingHistoryDTO toSellingHistoryDTO(SellingHistory entity);
    List<SellingHistoryDTO> toSellingHistoryDTOs(List<SellingHistory> entities);
}
