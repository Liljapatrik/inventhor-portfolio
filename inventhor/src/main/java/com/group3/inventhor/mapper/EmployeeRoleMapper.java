package com.group3.inventhor.mapper;


import com.group3.inventhor.dto.EmployeeRoleDTO;
import com.group3.inventhor.model.EmployeeRole;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @author Tatiana Fløisbonn
 *
 * The EmployeeRoleMapper interface is used to map between EmployeeRole and EmployeeRoleDTO objects.
 * It uses MapStruct to generate the implementation at compile time.
 * This interface provides methods to convert EmployeeRole entities to EmployeeRoleDTOs and vice versa,
 * as well as methods to convert lists of EmployeeRole entities to lists of EmployeeRoleDTOs.
 *
 * @Mapper annotation indicates that this interface is a MapStruct mapper.
 *     The componentModel = "spring" allows it to be used as a Spring bean.
 *     nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE means that null values in the source object will not overwrite existing values in the target object.
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = org.mapstruct.NullValuePropertyMappingStrategy.IGNORE)
public interface EmployeeRoleMapper {

    EmployeeRoleDTO toEmployeeRoleDTO(EmployeeRole employeeRole);

    EmployeeRole toEmployeeRole(EmployeeRoleDTO employeeRoleDTO);

    List<EmployeeRoleDTO> toEmployeeRoleDTOs(List<EmployeeRole> employeeRoles);
}
