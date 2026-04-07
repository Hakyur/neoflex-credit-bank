package ru.rogotovsky.statement.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.rogotovsky.statement.dto.LoanOfferDto;
import ru.rogotovsky.statement.dto.LoanStatementRequestDto;
import ru.rogotovsky.statement.service.StatementService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/statement")
@RequiredArgsConstructor
public class StatementController {

    private final StatementService statementService;

    @PostMapping
    public ResponseEntity<List<LoanOfferDto>> applyForLoan(@Valid @RequestBody LoanStatementRequestDto requestDto) {
        log.info("Received /statement request: {}", requestDto);

        List<LoanOfferDto> offers = statementService.processLoanApplication(requestDto);

        log.info("Returning loan offers: {}", offers);
        return ResponseEntity.ok(offers);
    }

    @PostMapping("/offer")
    public ResponseEntity<Void> selectOffer(@RequestBody LoanOfferDto requestDto) {
        log.info("Received /statement/offer request: {}", requestDto);

        statementService.selectLoanOffer(requestDto);

        log.info("POST /statement/offer completed");
        return ResponseEntity.noContent().build();
    }
}
