package ru.rogotovsky.deal.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.rogotovsky.deal.dto.FinishRegistrationRequestDto;
import ru.rogotovsky.deal.dto.ScoringDataDto;
import ru.rogotovsky.deal.entity.Statement;

@Mapper(componentModel = "spring")
public interface ScoringMapper {

    @Mapping(target = "amount", source = "statement.appliedOffer.requestedAmount")
    @Mapping(target = "term", source = "statement.appliedOffer.term")
    @Mapping(target = "employment", source = "requestDto.employment")
    @Mapping(target = ".", source = "statement.client")
    @Mapping(target = ".", source = "requestDto")
    @Mapping(target = "birthdate", source = "statement.client.birthDate")
    @Mapping(target = "passportSeries", source = "statement.client.passport.series")
    @Mapping(target = "passportNumber", source = "statement.client.passport.number")
    @Mapping(target = "isInsuranceEnabled", source = "statement.appliedOffer.isInsuranceEnabled")
    @Mapping(target = "isSalaryClient", source = "statement.appliedOffer.isSalaryClient")
    ScoringDataDto toScoringDataDto(Statement statement, FinishRegistrationRequestDto requestDto);
}
