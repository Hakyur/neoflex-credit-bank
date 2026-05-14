package ru.rogotovsky.deal.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.rogotovsky.deal.dto.CreditDto;
import ru.rogotovsky.deal.entity.Credit;

@Mapper(componentModel = "spring")
public interface CreditMapper {

    @Mapping(target = "insuranceEnabled", source = "isInsuranceEnabled")
    @Mapping(target = "salaryClient", source = "isSalaryClient")
    @Mapping(target = "creditStatus", constant = "CALCULATED")
    Credit toCredit(CreditDto dto);

    @Mapping(target = "isInsuranceEnabled", source = "insuranceEnabled")
    @Mapping(target = "isSalaryClient", source = "salaryClient")
    CreditDto toDto(Credit credit);
}
