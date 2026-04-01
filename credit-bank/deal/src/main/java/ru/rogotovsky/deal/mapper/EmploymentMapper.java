package ru.rogotovsky.deal.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.rogotovsky.deal.dto.EmploymentDto;
import ru.rogotovsky.deal.entity.Employment;

@Mapper(componentModel = "spring")
public interface EmploymentMapper {

    @Mapping(target = "employmentId", ignore = true)
    @Mapping(target = "status", source = "employmentStatus")
    @Mapping(target = "employerInn", source = "employerINN")
    Employment toEmployment(EmploymentDto dto);
}
