package ru.rogotovsky.calculator.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.rogotovsky.calculator.dto.PaymentScheduleElementDto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static ru.rogotovsky.calculator.util.NumberForCalculationUtils.*;

@Slf4j
@Service
public class LoanCalculator {

    public BigDecimal calculateMonthlyPayment(BigDecimal amount, BigDecimal rate, Integer term) {

        BigDecimal monthlyRate = toMonthlyRate(rate);

        BigDecimal pow = monthlyRate.add(BigDecimal.ONE).pow(term);
        BigDecimal numerator = amount.multiply(monthlyRate).multiply(pow);
        BigDecimal denominator = pow.subtract(BigDecimal.ONE);

        BigDecimal monthlyPayment = numerator.divide(denominator, MONEY_SCALE, RoundingMode.HALF_UP);
        log.debug("MonthlyPayment calculation -> amount: {}, rate: {}, term: {}, monthlyPayment: {}",
                amount, rate, term, monthlyPayment);

        return monthlyPayment;
    }

    public List<PaymentScheduleElementDto> buildPaymentSchedule(
            BigDecimal amount,
            BigDecimal rate,
            Integer term,
            BigDecimal monthlyPayment
    ) {

        List<PaymentScheduleElementDto> schedule = new ArrayList<>();

        BigDecimal monthlyRate = toMonthlyRate(rate);

        BigDecimal remainingDebt = amount;

        for (int i = 1; i <= term; i++) {

            BigDecimal interestPayment = remainingDebt.multiply(monthlyRate)
                    .setScale(MONEY_SCALE, RoundingMode.HALF_UP);

            BigDecimal debtPayment = monthlyPayment.subtract(interestPayment)
                            .setScale(MONEY_SCALE, RoundingMode.HALF_UP);

            remainingDebt = remainingDebt.subtract(debtPayment).max(BigDecimal.ZERO);

            schedule.add(new PaymentScheduleElementDto(
                    i,
                    LocalDate.now().plusMonths(i),
                    monthlyPayment,
                    interestPayment,
                    debtPayment,
                    remainingDebt
            ));
            log.debug("Schedule month {} -> interest: {}, debt: {}, remaining: {}", i, interestPayment, debtPayment, remainingDebt);
        }

        log.info("Built payment schedule: {}", schedule);
        return schedule;
    }

    public BigDecimal calculatePSK(BigDecimal amount, BigDecimal rate, Integer term) {
        BigDecimal monthlyPayment = calculateMonthlyPayment(amount, rate, term);
        BigDecimal paymentsAmount = monthlyPayment.multiply(BigDecimal.valueOf(term));
        BigDecimal ratio = paymentsAmount.divide(amount, CALCULATION_SCALE, RoundingMode.HALF_UP);

        log.debug("PSK calculation -> amount: {}, rate: {}, term: {}, monthlyPayment: {}, paymentsAmount: {}",
                amount, rate, term, monthlyPayment, paymentsAmount);

        BigDecimal psk = ratio
                .subtract(BigDecimal.ONE)
                .multiply(PERCENT_MULTIPLICATOR)
                .setScale(MONEY_SCALE, RoundingMode.HALF_UP);

        log.info("PSK: {}", psk);
        return psk;
    }

    private BigDecimal toMonthlyRate(BigDecimal yearlyRate) {
        return yearlyRate
                .divide(PERCENT_DIVISOR, CALCULATION_SCALE, RoundingMode.HALF_UP)
                .divide(MONTH_IN_YEAR, CALCULATION_SCALE, RoundingMode.HALF_UP);
    }
}

