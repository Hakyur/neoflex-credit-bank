package ru.rogotovsky.calculator.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.rogotovsky.calculator.dto.CreditDto;
import ru.rogotovsky.calculator.dto.LoanOfferDto;
import ru.rogotovsky.calculator.dto.LoanStatementRequestDto;
import ru.rogotovsky.calculator.dto.ScoringDataDto;
import ru.rogotovsky.calculator.service.CreditService;
import ru.rogotovsky.calculator.service.LoanOfferService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/calculator")
@RequiredArgsConstructor
public class CalculatorController {

    private final LoanOfferService loanOfferService;
    private final CreditService creditService;

    @PostMapping("/offers")
    public ResponseEntity<List<LoanOfferDto>> calculateLoanOffers(@RequestBody LoanStatementRequestDto requestDto) {
        log.info("Received /offers request: {}", requestDto);

        List<LoanOfferDto> offers = loanOfferService.calculateLoanOffers(requestDto);

        log.info("Returning loan offers: {}", offers);
        return ResponseEntity.ok(offers);
    }

    @PostMapping("/calc")
    public ResponseEntity<CreditDto> calculateCredit(@RequestBody ScoringDataDto requestDto) {
        log.info("Received /calc request: {}", requestDto);

        CreditDto creditDto = creditService.calculateCredit(requestDto);

        log.info("Credit calculation result: {}", creditDto);
        return ResponseEntity.ok(creditDto);
    }
}
