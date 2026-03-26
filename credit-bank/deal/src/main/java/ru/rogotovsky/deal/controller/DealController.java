package ru.rogotovsky.deal.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.rogotovsky.deal.dto.FinishRegistrationRequestDto;
import ru.rogotovsky.deal.dto.LoanOfferDto;
import ru.rogotovsky.deal.dto.LoanStatementRequestDto;
import ru.rogotovsky.deal.service.DealService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/deal")
@RequiredArgsConstructor
public class DealController {

    private final DealService dealService;

    @PostMapping("/statement")
    public List<LoanOfferDto> getLoanOffers(@RequestBody LoanStatementRequestDto requestDto) {
        return dealService.getLoanOffers(requestDto);
    }

    @PostMapping("/offer/select")
    public void selectOffer(@RequestBody LoanOfferDto requestDto) {
        dealService.applyLoanOffer(requestDto);
    }

    @PostMapping("/calculate/{statementId}")
    public void calculateCredit(@RequestBody FinishRegistrationRequestDto requestDto, @PathVariable String statementId) {
        dealService.calculateCredit(requestDto, UUID.fromString(statementId));
    }
}
