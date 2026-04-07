package ru.rogotovsky.statement.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.rogotovsky.statement.client.DealClient;
import ru.rogotovsky.statement.dto.LoanOfferDto;
import ru.rogotovsky.statement.dto.LoanStatementRequestDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StatementService {

    private final DealClient dealClient;

    public List<LoanOfferDto> processLoanApplication(LoanStatementRequestDto requestDto) {
        return dealClient.requestLoanOffers(requestDto);
    }

    public void selectLoanOffer(LoanOfferDto requestDto) {
        dealClient.requestOfferSelection(requestDto);
    }
}
