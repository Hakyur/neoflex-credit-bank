package ru.rogotovsky.deal.mapper;

import org.springframework.stereotype.Component;
import ru.rogotovsky.deal.dto.FinishRegistrationRequestDto;
import ru.rogotovsky.deal.dto.LoanOfferDto;
import ru.rogotovsky.deal.dto.ScoringDataDto;
import ru.rogotovsky.deal.entity.Client;
import ru.rogotovsky.deal.entity.Passport;
import ru.rogotovsky.deal.entity.Statement;

@Component
public class ScoringMapper {

    public ScoringDataDto toScoringDataDto(Statement statement, FinishRegistrationRequestDto requestDto) {

        Client client = statement.getClient();
        Passport passport = client.getPassport();
        LoanOfferDto offer = statement.getAppliedOffer();

        return new ScoringDataDto(
                offer.getRequestedAmount(), offer.getTerm(),
                client.getFirstName(), client.getLastName(),
                client.getMiddleName(), requestDto.gender(),
                client.getBirthDate(), passport.getSeries(),
                passport.getNumber(), requestDto.passportIssueDate(),
                requestDto.passportIssueBranch(), requestDto.maritalStatus(),
                requestDto.dependentAmount(), requestDto.employment(),
                requestDto.accountNumber(), offer.getIsInsuranceEnabled(),
                offer.getIsSalaryClient()
        );
    }
}
