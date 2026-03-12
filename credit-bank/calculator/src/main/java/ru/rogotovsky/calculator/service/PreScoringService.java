package ru.rogotovsky.calculator.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.rogotovsky.calculator.config.LoanProperties;

import java.math.BigDecimal;

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

        return rate;
    }

    public BigDecimal calculatePrescoringAmount(BigDecimal amount, boolean isInsuranceEnabled) {
        if (isInsuranceEnabled) {
            return amount.add(loanProperties.getInsuranceCost());
        }
        return amount;
    }
}
