package ru.rogotovsky.deal.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import ru.rogotovsky.deal.dto.FinishRegistrationRequestDto;
import ru.rogotovsky.deal.dto.LoanStatementRequestDto;
import ru.rogotovsky.deal.entity.Client;
import ru.rogotovsky.deal.entity.Passport;

@Mapper(componentModel = "spring", uses = {EmploymentMapper.class})
public interface ClientMapper {

    @Mapping(target = "passport", source = ".", qualifiedByName = "mapPassport")
    @Mapping(target = "birthDate", source = "birthdate")
    Client toClient(LoanStatementRequestDto dto);

    @Named("mapPassport")
    @Mapping(target = "series", source = "passportSeries")
    @Mapping(target = "number", source = "passportNumber")
    Passport toPassport(LoanStatementRequestDto dto);

    @Mapping(target = "passport.issueDate", source = "passportIssueDate")
    @Mapping(target = "passport.issueBranch", source = "passportIssueBranch")
    void updateClientFromDto(FinishRegistrationRequestDto dto, @MappingTarget Client client);
}
