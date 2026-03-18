package ru.rogotovsky.calculator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.rogotovsky.calculator.config.LoanProperties;
import ru.rogotovsky.calculator.service.PreScoringService;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PreScoringServiceTests {

    @Mock
    private LoanProperties loanProperties;
    private PreScoringService preScoringService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        preScoringService = new PreScoringService(loanProperties);

        when(loanProperties.getBaseRate()).thenReturn(BigDecimal.valueOf(20));
        when(loanProperties.getInsuranceDiscount()).thenReturn(BigDecimal.valueOf(3));
        when(loanProperties.getSalaryClientDiscount()).thenReturn(BigDecimal.valueOf(1));
        when(loanProperties.getInsuranceCost()).thenReturn(BigDecimal.valueOf(100000));
    }

    @Test
    void calculatePrescoringRateNoDiscounts() {
        BigDecimal expected = BigDecimal.valueOf(20);
        BigDecimal result = preScoringService.calculatePrescoringRate(false, false);

        assertEquals(expected, result);
    }


    @Test
    void calculatePrescoringRateWithInsurance() {
        BigDecimal expected = BigDecimal.valueOf(17);
        BigDecimal result = preScoringService.calculatePrescoringRate(true, false);

        assertEquals(expected, result);
    }

    @Test
    void calculatePrescoringRateWithSalaryClient() {
        BigDecimal expected = BigDecimal.valueOf(19);
        BigDecimal result = preScoringService.calculatePrescoringRate(false, true);

        assertEquals(expected, result);
    }

    @Test
    void calculatePrescoringRateWithAllDiscounts() {
        BigDecimal expected = BigDecimal.valueOf(16);
        BigDecimal result = preScoringService.calculatePrescoringRate(true, true);

        assertEquals(expected, result);
    }

    @Test
    void calculatePrescoringAmountWithoutInsurance() {
        BigDecimal expected = BigDecimal.valueOf(500000);
        BigDecimal result = preScoringService.calculatePrescoringAmount(
                BigDecimal.valueOf(500000),
                false
        );

        assertEquals(expected, result);
    }

    @Test
    void calculatePrescoringAmountWithInsurance() {
        BigDecimal expected = BigDecimal.valueOf(600000);
        BigDecimal result = preScoringService.calculatePrescoringAmount(
                BigDecimal.valueOf(500000),
                true
        );

        assertEquals(expected, result);
    }
}
