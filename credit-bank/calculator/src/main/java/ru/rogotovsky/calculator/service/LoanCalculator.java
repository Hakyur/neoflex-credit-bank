package ru.rogotovsky.calculator.service;

import org.springframework.stereotype.Service;
import ru.rogotovsky.calculator.dto.PaymentScheduleElementDto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class LoanCalculator {

    public BigDecimal calculateMonthlyPayment(BigDecimal amount, BigDecimal rate, Integer term) {

        BigDecimal monthlyRate = rate
                .divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP)
                .divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP);

        BigDecimal pow = monthlyRate.add(BigDecimal.ONE).pow(term);

        BigDecimal numerator = amount.multiply(monthlyRate).multiply(pow);

        BigDecimal denominator = pow.subtract(BigDecimal.ONE);

        return numerator.divide(denominator, 2, RoundingMode.HALF_UP);
    }

    public List<PaymentScheduleElementDto> buildPaymentSchedule(
            BigDecimal amount,
            BigDecimal rate,
            Integer term,
            BigDecimal monthlyPayment
    ) {

        List<PaymentScheduleElementDto> schedule = new ArrayList<>();

        BigDecimal monthlyRate = rate
                .divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP)
                .divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP);

        BigDecimal remainingDebt = amount;

        for (int i = 1; i <= term; i++) {

            BigDecimal interestPayment =
                    remainingDebt.multiply(monthlyRate)
                            .setScale(2, RoundingMode.HALF_UP);

            BigDecimal debtPayment =
                    monthlyPayment.subtract(interestPayment)
                            .setScale(2, RoundingMode.HALF_UP);

            remainingDebt =
                    remainingDebt.subtract(debtPayment)
                            .setScale(2, RoundingMode.HALF_UP);

            if (remainingDebt.compareTo(BigDecimal.ZERO) < 0) {
                remainingDebt = BigDecimal.ZERO;
            }

            schedule.add(new PaymentScheduleElementDto(
                    i,
                    LocalDate.now().plusMonths(i),
                    monthlyPayment,
                    interestPayment,
                    debtPayment,
                    remainingDebt
            ));
        }

        return schedule;
    }

    public BigDecimal calculatePSK(BigDecimal amount, BigDecimal rate, Integer term) {
        BigDecimal monthlyPayment = calculateMonthlyPayment(amount, rate, term);
        BigDecimal paymentsAmount = monthlyPayment.multiply(BigDecimal.valueOf(term));
        BigDecimal ratio = paymentsAmount.divide(amount, 10, RoundingMode.HALF_UP);
        return ratio
                .subtract(BigDecimal.ONE)
                .multiply(BigDecimal.valueOf(100))
                .setScale(2, RoundingMode.HALF_UP);
    }
}
