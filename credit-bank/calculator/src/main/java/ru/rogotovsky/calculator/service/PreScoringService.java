package ru.rogotovsky.calculator.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.rogotovsky.calculator.config.LoanProperties;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class PreScoringService {

    private final LoanProperties loanProperties;

    public BigDecimal calculatePrescoringRate(boolean isInsuranceEnabled, boolean isSalaryClient) {
        BigDecimal rate = loanProperties.getBaseRate();

        if (isInsuranceEnabled) {
            rate = rate.subtract(loanProperties.getInsuranceDiscount());
        }

        if (isSalaryClient) {
            rate = rate.subtract(loanProperties.getSalaryClientDiscount());
        }

        log.debug("PreScoringRate calculation -> insurance: {}, salaryClient: {}, rate: {}", isInsuranceEnabled, isSalaryClient, rate);
        return rate;
    }

    public BigDecimal calculatePrescoringAmount(BigDecimal amount, boolean isInsuranceEnabled) {
        BigDecimal totalAmount = isInsuranceEnabled ? amount.add(loanProperties.getInsuranceCost()) : amount;
        log.debug("PreScoringAmount calc -> base: {}, insurance: {}, total: {}", amount, isInsuranceEnabled, totalAmount);
        return totalAmount;
    }
}
