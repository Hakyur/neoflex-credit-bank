package ru.rogotovsky.statement.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.rogotovsky.statement.dto.LoanOfferDto;
import ru.rogotovsky.statement.dto.LoanStatementRequestDto;

import java.util.List;

@RestController
@RequestMapping("/statement")
public class StatementController {

    @PostMapping
    public ResponseEntity<List<LoanOfferDto>> getLoanOffers(@Valid @RequestBody LoanStatementRequestDto requestDto) {
        return null;
    }

    @PostMapping("/offer")
    public ResponseEntity<Void> applyOffer(@RequestBody LoanOfferDto requestDto) {
        return null;
    }
}
