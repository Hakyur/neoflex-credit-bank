package ru.rogotovsky.gateway.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.rogotovsky.gateway.client.StatementClient;
import ru.rogotovsky.gateway.dto.LoanOfferDto;
import ru.rogotovsky.gateway.dto.LoanStatementRequestDto;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GatewayService {

    private final StatementClient statementClient;

    public List<LoanOfferDto> applyForLoan(LoanStatementRequestDto requestDto) {
        log.debug("Processing loan application through gateway");
        return statementClient.requestLoanOffers(requestDto);
    }

    public void selectOffer(LoanOfferDto requestDto) {
        log.debug("Processing select offer through gateway");
        statementClient.requestOfferSelection(requestDto);
    }
}
