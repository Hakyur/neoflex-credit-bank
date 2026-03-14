package ru.rogotovsky.calculator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.rogotovsky.calculator.dto.PaymentScheduleElementDto;
import ru.rogotovsky.calculator.service.LoanCalculator;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LoanCalculatorTests {

    private LoanCalculator loanCalculator;

    @BeforeEach
    void setUp() {
        loanCalculator = new LoanCalculator();
    }

    @Test
    void calculateMonthlyPaymentShouldReturnCorrectValue() {
        BigDecimal amount = BigDecimal.valueOf(100000);
        BigDecimal rate = BigDecimal.valueOf(20);
        int term = 12;

        BigDecimal expected = BigDecimal.valueOf(9263.45);
        BigDecimal result = loanCalculator.calculateMonthlyPayment(amount, rate, term);

        assertEquals(0, result.compareTo(expected));
    }

    @Test
    void calculatePskShouldReturnCorrectValue() {

        BigDecimal amount = BigDecimal.valueOf(100000);
        BigDecimal rate = BigDecimal.valueOf(20);
        int term = 12;

        BigDecimal expected = BigDecimal.valueOf(11.16);
        BigDecimal result = loanCalculator.calculatePSK(amount, rate, term);

        assertEquals(0, result.compareTo(expected));
    }

    @Test
    void buildPaymentScheduleShouldCreateScheduleWithCorrectSize() {

        BigDecimal amount = BigDecimal.valueOf(100000);
        BigDecimal rate = BigDecimal.valueOf(20);
        int term = 12;

        BigDecimal monthlyPayment =
                loanCalculator.calculateMonthlyPayment(amount, rate, term);

        List<PaymentScheduleElementDto> schedule =
                loanCalculator.buildPaymentSchedule(amount, rate, term, monthlyPayment);

        assertEquals(term, schedule.size());
    }

    @Test
    void buildPaymentScheduleLastRemainingDebtShouldBeZero() {

        BigDecimal amount = BigDecimal.valueOf(100000);
        BigDecimal rate = BigDecimal.valueOf(10);
        int term = 6;

        BigDecimal monthlyPayment =
                loanCalculator.calculateMonthlyPayment(amount, rate, term);

        List<PaymentScheduleElementDto> schedule =
                loanCalculator.buildPaymentSchedule(amount, rate, term, monthlyPayment);

        PaymentScheduleElementDto last = schedule.getLast();

        assertEquals(BigDecimal.ZERO, last.remainingDebt());
    }
}
