package com.group3.inventhor.mapper;

import com.group3.inventhor.dto.AuthUserDTO;
import com.group3.inventhor.dto.EmployeeDTO;
import com.group3.inventhor.model.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

/**
 * @author Tatiana Fløisbonn
 *
 * The EmployeeMapper interface is used to map between Employee and EmployeeDTO objects.
 * It uses MapStruct to generate the implementation at compile time.
 * In this case it converts Employee entities to EmployeeDTOs and vice versa.
 * It also provides methods to convert lists of Employee entities to lists of EmployeeDTOs.
 *
 * @Mapper annotation indicates that this interface is a MapStruct mapper.
 *      The componentModel = "spring" allows it to be used as a Spring bean.
 *      nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE means that null values in the source object will not overwrite existing values in the target object.
 */
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface EmployeeMapper {
    @Mapping(source = "passwordhash", target = "password")
    @Mapping(source = "employeddate", target = "employeddate")
    EmployeeDTO toEmployeeDTO(Employee employee);
    Employee toEmployee(EmployeeDTO employeeDTO);

    List<EmployeeDTO> toEmployeeDTOs(List<Employee> employees);


    AuthUserDTO toAuthUserDTO(Employee employee);

    List<AuthUserDTO> toAuthUserDTOs(List<Employee> employees);
}
