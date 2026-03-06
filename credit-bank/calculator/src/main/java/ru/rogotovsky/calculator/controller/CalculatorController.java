package ru.rogotovsky.calculator.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.rogotovsky.calculator.dto.CreditDto;
import ru.rogotovsky.calculator.dto.LoanOfferDto;
import ru.rogotovsky.calculator.dto.LoanStatementRequestDto;
import ru.rogotovsky.calculator.dto.ScoringDataDto;

import java.util.List;

@RestController
@RequestMapping("/calculator")
public class CalculatorController {

    @PostMapping("/offers")
    public ResponseEntity<List<LoanOfferDto>> calculateLoanOffers(@RequestBody LoanStatementRequestDto requestDto) {
        return null;
    }

    @PostMapping("/calc")
    public ResponseEntity<CreditDto> calculateCredit(@RequestBody ScoringDataDto requestDto) {
        return null;
    }
}
