package ru.rogotovsky.calculator.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.rogotovsky.calculator.dto.CreditDto;
import ru.rogotovsky.calculator.dto.LoanOfferDto;
import ru.rogotovsky.calculator.dto.LoanStatementRequestDto;
import ru.rogotovsky.calculator.dto.ScoringDataDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CalculatorService {

    private final LoanOfferService loanOfferService;

    public List<LoanOfferDto> calculateLoanOffers(LoanStatementRequestDto requestDto) {
        return loanOfferService.calculateLoanOffers(requestDto);
    }

    public CreditDto calculateCredit(ScoringDataDto requestDto) {
        return null;
    }
}
