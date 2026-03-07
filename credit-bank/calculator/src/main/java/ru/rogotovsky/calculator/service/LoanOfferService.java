package ru.rogotovsky.calculator.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.rogotovsky.calculator.config.LoanProperties;
import ru.rogotovsky.calculator.dto.LoanOfferDto;
import ru.rogotovsky.calculator.dto.LoanStatementRequestDto;
import ru.rogotovsky.calculator.service.utils.LoanCalculationUtils;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LoanOfferService {

    private final LoanProperties loanProperties;

    public List<LoanOfferDto> calculateLoanOffers(LoanStatementRequestDto requestDto) {
        List<LoanOfferDto> loanOffers = List.of(
                createLoanOffer(requestDto, false, false),
                createLoanOffer(requestDto, false, true),
                createLoanOffer(requestDto, true, false),
                createLoanOffer(requestDto, true, true)
        );

        return loanOffers.stream()
                .sorted(Comparator.comparing(LoanOfferDto::rate).reversed())
                .toList();
    }

    private LoanOfferDto createLoanOffer(
            LoanStatementRequestDto requestDto,
            boolean isInsuranceEnabled,
            boolean isSalaryClient) {

        BigDecimal rate = loanProperties.getBaseRate();
        BigDecimal totalAmount = requestDto.amount();

        if (isInsuranceEnabled) {
            totalAmount = totalAmount.add(loanProperties.getInsuranceCost());
            rate = rate.subtract(loanProperties.getInsuranceDiscount());
        }

        if (isSalaryClient) {
            rate = rate.subtract(loanProperties.getSalaryClientDiscount());
        }

        BigDecimal monthlyPayment =  LoanCalculationUtils.calculateMonthlyPayment(totalAmount, rate, requestDto.term());

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
