package ru.rogotovsky.calculator.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.rogotovsky.calculator.dto.LoanOfferDto;
import ru.rogotovsky.calculator.dto.LoanStatementRequestDto;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoanOfferService {

    private final LoanCalculator loanCalculator;
    private final PreScoringService preScoringService;

    public List<LoanOfferDto> calculateLoanOffers(LoanStatementRequestDto requestDto) {
        log.info("Calculating loan offers for request: {}", requestDto);

        List<LoanOfferDto> loanOffers = List.of(
                createLoanOffer(requestDto, false, false),
                createLoanOffer(requestDto, false, true),
                createLoanOffer(requestDto, true, false),
                createLoanOffer(requestDto, true, true)
        );

        loanOffers = loanOffers.stream()
                .sorted(Comparator.comparing(LoanOfferDto::rate).reversed())
                .toList();

        log.info("Returning loan offers: {}", loanOffers);

        return loanOffers;
    }

    private LoanOfferDto createLoanOffer(
            LoanStatementRequestDto requestDto,
            boolean isInsuranceEnabled,
            boolean isSalaryClient) {

        BigDecimal rate = preScoringService.calculatePrescoringRate(isInsuranceEnabled, isSalaryClient);
        BigDecimal totalAmount = preScoringService.calculatePrescoringAmount(requestDto.amount(), isInsuranceEnabled);
        BigDecimal monthlyPayment =  loanCalculator.calculateMonthlyPayment(totalAmount, rate, requestDto.term());

        log.debug("LoanOffer calculation -> insurance: {}, salaryClient: {}, rate: {}, totalAmount: {}, monthlyPayment: {}",
                isInsuranceEnabled, isSalaryClient, rate, totalAmount, monthlyPayment);

        return new LoanOfferDto(
                UUID.randomUUID(),
                requestDto.amount(),
                totalAmount,
                requestDto.term(),
                monthlyPayment,
                rate,
                isInsuranceEnabled,
                isSalaryClient
        );
    }
}
