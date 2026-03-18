package ru.rogotovsky.calculator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.rogotovsky.calculator.dto.LoanOfferDto;
import ru.rogotovsky.calculator.dto.LoanStatementRequestDto;
import ru.rogotovsky.calculator.service.LoanCalculator;
import ru.rogotovsky.calculator.service.LoanOfferService;
import ru.rogotovsky.calculator.service.PreScoringService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LoanOfferServiceTests {

    @Mock
    private LoanCalculator loanCalculator;

    @Mock
    private PreScoringService preScoringService;

    private LoanOfferService loanOfferService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        loanOfferService = new LoanOfferService(loanCalculator, preScoringService);
    }

    private LoanStatementRequestDto buildRequest() {
        return new LoanStatementRequestDto(
                BigDecimal.valueOf(100000),
                6,
                "Dmitry",
                "Rogotovsky",
                "Vladimirovich",
                "test@mail.com",
                LocalDate.now().minusYears(25),
                "1234",
                "123456"
        );
    }

    @Test
    void calculateLoanOffersShouldReturn4Offers() {

        LoanStatementRequestDto request = buildRequest();

        when(preScoringService.calculatePrescoringRate(true,true)).thenReturn(BigDecimal.valueOf(16));
        when(preScoringService.calculatePrescoringRate(true,false)).thenReturn(BigDecimal.valueOf(17));
        when(preScoringService.calculatePrescoringRate(false,true)).thenReturn(BigDecimal.valueOf(19));
        when(preScoringService.calculatePrescoringRate(false,false)).thenReturn(BigDecimal.valueOf(20));

        when(preScoringService.calculatePrescoringAmount(request.amount(),true))
                .thenReturn(request.amount().add(BigDecimal.valueOf(100000)));

        when(preScoringService.calculatePrescoringAmount(request.amount(),false))
                .thenReturn(request.amount());

        when(loanCalculator.calculateMonthlyPayment(request.amount(), BigDecimal.valueOf(20), request.term()))
                .thenReturn(BigDecimal.valueOf(17652.28));
        when(loanCalculator.calculateMonthlyPayment(request.amount(), BigDecimal.valueOf(19), request.term()))
                .thenReturn(BigDecimal.valueOf(17602.37));
        when(loanCalculator.calculateMonthlyPayment(BigDecimal.valueOf(200000), BigDecimal.valueOf(17), request.term()))
                .thenReturn(BigDecimal.valueOf(35005.48));
        when(loanCalculator.calculateMonthlyPayment(BigDecimal.valueOf(200000), BigDecimal.valueOf(16), request.term()))
                .thenReturn(BigDecimal.valueOf(34906.06));

        List<LoanOfferDto> offers = loanOfferService.calculateLoanOffers(buildRequest());

        assertEquals(4, offers.size());
    }
}
