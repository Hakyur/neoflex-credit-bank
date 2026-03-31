package ru.rogotovsky.calculator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.rogotovsky.calculator.dto.CreditDto;
import ru.rogotovsky.calculator.dto.EmploymentDto;
import ru.rogotovsky.calculator.dto.PaymentScheduleElementDto;
import ru.rogotovsky.calculator.dto.ScoringDataDto;
import ru.rogotovsky.calculator.enums.EmploymentStatus;
import ru.rogotovsky.calculator.enums.Gender;
import ru.rogotovsky.calculator.enums.MaritalStatus;
import ru.rogotovsky.calculator.enums.Position;
import ru.rogotovsky.calculator.service.CreditService;
import ru.rogotovsky.calculator.service.LoanCalculator;
import ru.rogotovsky.calculator.service.PreScoringService;
import ru.rogotovsky.calculator.service.ScoringService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

public class CreditServiceTests {

    @Mock
    private ScoringService scoringService;

    @Mock
    private PreScoringService preScoringService;

    @Mock
    private LoanCalculator loanCalculator;

    private CreditService creditService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        creditService = new CreditService(scoringService, preScoringService, loanCalculator);
    }

    private ScoringDataDto buildDto() {

        EmploymentDto employment = new EmploymentDto(
                EmploymentStatus.EMPLOYED,
                "123",
                BigDecimal.valueOf(50000),
                Position.WORKER,
                24,
                6
        );

        return new ScoringDataDto(
                BigDecimal.valueOf(100000),
                6,
                "Dmitry",
                "Rogotovsky",
                "Vladimirovich",
                Gender.MALE,
                LocalDate.now().minusYears(29),
                "1234",
                "123456",
                LocalDate.now().minusYears(5),
                "UFMS",
                MaritalStatus.SINGLE,
                0,
                employment,
                "123456",
                false,
                false
        );
    }

    @Test
    void calculateCreditSuccess() {
        ScoringDataDto requestDto = buildDto();

        List<PaymentScheduleElementDto> paymentScheduleElementDtoList = List.of(
                new PaymentScheduleElementDto(1, LocalDate.now(),
                        BigDecimal.valueOf(17652.28), BigDecimal.valueOf(1666.67),
                        BigDecimal.valueOf(15985.61), BigDecimal.valueOf(84014.39)),
                new PaymentScheduleElementDto(2, LocalDate.now().plusMonths(1),
                        BigDecimal.valueOf(17652.28), BigDecimal.valueOf(1400.24),
                        BigDecimal.valueOf(16252.04), BigDecimal.valueOf(67762.35)),
                new PaymentScheduleElementDto(3, LocalDate.now().plusMonths(2),
                        BigDecimal.valueOf(17652.28), BigDecimal.valueOf(1129.37),
                        BigDecimal.valueOf(16522.91), BigDecimal.valueOf(51239.44)),
                new PaymentScheduleElementDto(4, LocalDate.now().plusMonths(3),
                        BigDecimal.valueOf(17652.28), BigDecimal.valueOf(853.99),
                        BigDecimal.valueOf(16798.29), BigDecimal.valueOf(34441.15)),
                new PaymentScheduleElementDto(5, LocalDate.now().plusMonths(4),
                        BigDecimal.valueOf(17652.28), BigDecimal.valueOf(574.02),
                        BigDecimal.valueOf(17078.26), BigDecimal.valueOf(17362.89)),
                new PaymentScheduleElementDto(6, LocalDate.now().plusMonths(6),
                        BigDecimal.valueOf(17652.28), BigDecimal.valueOf(289.38),
                        BigDecimal.valueOf(17362.90), BigDecimal.valueOf(0))
        );

        when(preScoringService.calculatePrescoringAmount(
                BigDecimal.valueOf(100000), false))
                .thenReturn(BigDecimal.valueOf(100000));

        when(scoringService.calculateRate(buildDto()))
                .thenReturn(BigDecimal.valueOf(20));

        when(loanCalculator.calculateMonthlyPayment(
                BigDecimal.valueOf(100000),
                BigDecimal.valueOf(20),
                6))
                .thenReturn(BigDecimal.valueOf(17652.28));

        when(loanCalculator.buildPaymentSchedule(
                BigDecimal.valueOf(100000),
                BigDecimal.valueOf(20),
                6,
                BigDecimal.valueOf(17652.28)))
                .thenReturn(paymentScheduleElementDtoList);

        when(loanCalculator.calculatePSK(
                BigDecimal.valueOf(100000),
                BigDecimal.valueOf(20),
                6))
                .thenReturn(BigDecimal.valueOf(5.91));

        CreditDto expected = new CreditDto(requestDto.amount(), requestDto.term(),
                BigDecimal.valueOf(17652.28), BigDecimal.valueOf(20), BigDecimal.valueOf(5.91),
                requestDto.isInsuranceEnabled(), requestDto.isSalaryClient(), paymentScheduleElementDtoList);
        CreditDto credit = creditService.calculateCredit(requestDto);

        assertNotNull(credit);
        assertEquals(expected, credit);
    }
}
