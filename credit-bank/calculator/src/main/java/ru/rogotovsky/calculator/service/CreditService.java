package ru.rogotovsky.calculator.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.rogotovsky.calculator.dto.CreditDto;
import ru.rogotovsky.calculator.dto.PaymentScheduleElementDto;
import ru.rogotovsky.calculator.dto.ScoringDataDto;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreditService {

    private final ScoringService scoringService;
    private final PreScoringService preScoringService;
    private final LoanCalculator loanCalculator;

    public CreditDto calculateCredit(ScoringDataDto dto) {
        log.info("Calculating credit for client {} {}", dto.firstName(), dto.lastName());

        BigDecimal amount = preScoringService.calculatePrescoringAmount(dto.amount(), dto.isInsuranceEnabled());
        BigDecimal rate = scoringService.calculateRate(dto);
        Integer term = dto.term();

        BigDecimal monthlyPayment = loanCalculator.calculateMonthlyPayment(amount, rate, term);

        List<PaymentScheduleElementDto> schedule = loanCalculator.buildPaymentSchedule(amount, rate, term, monthlyPayment);

        BigDecimal psk = loanCalculator.calculatePSK(amount, rate, term);

        log.info("Credit calculation completed -> amount: {}, rate: {}, monthlyPayment: {}, psk: {}, schedule: {}",
                amount, rate, monthlyPayment, psk, schedule);
        return new CreditDto(
                amount,
                term,
                monthlyPayment,
                rate,
                psk,
                dto.isInsuranceEnabled(),
                dto.isSalaryClient(),
                schedule
        );
    }
}
