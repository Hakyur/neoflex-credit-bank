package ru.rogotovsky.statement.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.rogotovsky.statement.client.DealClient;
import ru.rogotovsky.statement.dto.LoanOfferDto;
import ru.rogotovsky.statement.dto.LoanStatementRequestDto;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatementService {

    private final DealClient dealClient;

    public List<LoanOfferDto> processLoanApplication(LoanStatementRequestDto requestDto) {
        log.debug("Processing loan application");

        List<LoanOfferDto> offers = dealClient.requestLoanOffers(requestDto);

        log.debug("Received {} offers from deal service", offers.size());
        return offers;
    }

    public void selectLoanOffer(LoanOfferDto requestDto) {
        log.debug("Processing loan offer selection for statementId={}", requestDto.statementId());

        dealClient.requestOfferSelection(requestDto);

        log.debug("Loan offer selection request sent to deal service");
    }
}
