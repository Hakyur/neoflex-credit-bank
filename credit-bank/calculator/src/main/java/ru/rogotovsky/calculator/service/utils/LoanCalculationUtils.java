package ru.rogotovsky.calculator.service.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class LoanCalculationUtils {

    public static BigDecimal calculateMonthlyPayment(BigDecimal amount, BigDecimal rate, Integer term) {

        BigDecimal monthlyRate = rate
                .divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP)
                .divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP);

        BigDecimal pow = monthlyRate.add(BigDecimal.ONE).pow(term);

        BigDecimal numerator = amount.multiply(monthlyRate).multiply(pow);

        BigDecimal denominator = pow.subtract(BigDecimal.ONE);

        return numerator.divide(denominator, 2, RoundingMode.HALF_UP);
    }
}
