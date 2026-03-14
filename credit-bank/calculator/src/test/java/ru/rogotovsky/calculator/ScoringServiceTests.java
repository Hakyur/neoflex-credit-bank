package ru.rogotovsky.calculator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.rogotovsky.calculator.dto.EmploymentDto;
import ru.rogotovsky.calculator.dto.ScoringDataDto;
import ru.rogotovsky.calculator.enums.EmploymentStatus;
import ru.rogotovsky.calculator.enums.Gender;
import ru.rogotovsky.calculator.enums.MaritalStatus;
import ru.rogotovsky.calculator.enums.Position;
import ru.rogotovsky.calculator.exception.ScoringException;
import ru.rogotovsky.calculator.service.PreScoringService;
import ru.rogotovsky.calculator.service.ScoringService;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ScoringServiceTests {

    private final BigDecimal BASE_RATE = BigDecimal.valueOf(20);

    @Mock
    private PreScoringService preScoringService;

    private ScoringService scoringService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        scoringService = new ScoringService(preScoringService);
    }

    private ScoringDataDto buildValidDto() {

        EmploymentDto employment = new EmploymentDto(
                EmploymentStatus.EMPLOYED,
                "1234567890",
                BigDecimal.valueOf(50000),
                Position.OTHER,
                24,
                6
        );

        return new ScoringDataDto(
                BigDecimal.valueOf(200000),
                12,
                "Dmitry",
                "Rogotovsky",
                "Vladimirovich",
                Gender.MALE,
                LocalDate.now().minusYears(30),
                "1234",
                "123456",
                LocalDate.now().plusYears(4),
                "UFMS",
                MaritalStatus.UNMARRIED,
                0,
                employment,
                "123456789",
                false,
                false
        );
    }

    @Test
    void applyEmploymentScoringWhenUnemployed() {
        EmploymentDto employment = new EmploymentDto(
                EmploymentStatus.UNEMPLOYED,
                "123",
                BigDecimal.valueOf(50000),
                Position.OTHER,
                24,
                6
        );

        ScoringDataDto dto = buildValidDto();

        ScoringDataDto valid = new ScoringDataDto(
                dto.amount(),
                dto.term(),
                dto.firstName(),
                dto.lastName(),
                dto.middleName(),
                dto.gender(),
                dto.birthdate(),
                dto.passportSeries(),
                dto.passportNumber(),
                dto.passportIssueDate(),
                dto.passportIssueBranch(),
                dto.maritalStatus(),
                dto.dependentAmount(),
                employment,
                dto.accountNumber(),
                dto.isInsuranceEnabled(),
                dto.isSalaryClient()
        );

        BigDecimal expected = EmploymentStatus.UNEMPLOYED.applyRate(BASE_RATE);
        BigDecimal actual = scoringService.applyEmploymentScoring(valid, BASE_RATE);
        assertEquals(expected, actual);
    }

    @Test
    void applyEmploymentScoringWhenEmployed() {
        EmploymentDto employment = new EmploymentDto(
                EmploymentStatus.EMPLOYED,
                "123",
                BigDecimal.valueOf(50000),
                Position.OTHER,
                24,
                6
        );

        ScoringDataDto dto = buildValidDto();

        ScoringDataDto valid = new ScoringDataDto(
                dto.amount(),
                dto.term(),
                dto.firstName(),
                dto.lastName(),
                dto.middleName(),
                dto.gender(),
                dto.birthdate(),
                dto.passportSeries(),
                dto.passportNumber(),
                dto.passportIssueDate(),
                dto.passportIssueBranch(),
                dto.maritalStatus(),
                dto.dependentAmount(),
                employment,
                dto.accountNumber(),
                dto.isInsuranceEnabled(),
                dto.isSalaryClient()
        );

        BigDecimal expected = EmploymentStatus.EMPLOYED.applyRate(BASE_RATE);
        BigDecimal actual = scoringService.applyEmploymentScoring(valid, BASE_RATE);
        assertEquals(expected, actual);
    }

    @Test
    void applyEmploymentScoringWhenBusinessOwner() {
        EmploymentDto employment = new EmploymentDto(
                EmploymentStatus.BUSINESS_OWNER,
                "123",
                BigDecimal.valueOf(50000),
                Position.OTHER,
                24,
                6
        );

        ScoringDataDto dto = buildValidDto();

        ScoringDataDto valid = new ScoringDataDto(
                dto.amount(),
                dto.term(),
                dto.firstName(),
                dto.lastName(),
                dto.middleName(),
                dto.gender(),
                dto.birthdate(),
                dto.passportSeries(),
                dto.passportNumber(),
                dto.passportIssueDate(),
                dto.passportIssueBranch(),
                dto.maritalStatus(),
                dto.dependentAmount(),
                employment,
                dto.accountNumber(),
                dto.isInsuranceEnabled(),
                dto.isSalaryClient()
        );

        BigDecimal expected = EmploymentStatus.BUSINESS_OWNER.applyRate(BASE_RATE);
        BigDecimal actual = scoringService.applyEmploymentScoring(valid, BASE_RATE);
        assertEquals(expected, actual);
    }

    @Test
    void applyEmploymentScoringWhenSelfEmployed() {
        EmploymentDto employment = new EmploymentDto(
                EmploymentStatus.SELF_EMPLOYED,
                "123",
                BigDecimal.valueOf(50000),
                Position.OTHER,
                24,
                6
        );

        ScoringDataDto dto = buildValidDto();

        ScoringDataDto valid = new ScoringDataDto(
                dto.amount(),
                dto.term(),
                dto.firstName(),
                dto.lastName(),
                dto.middleName(),
                dto.gender(),
                dto.birthdate(),
                dto.passportSeries(),
                dto.passportNumber(),
                dto.passportIssueDate(),
                dto.passportIssueBranch(),
                dto.maritalStatus(),
                dto.dependentAmount(),
                employment,
                dto.accountNumber(),
                dto.isInsuranceEnabled(),
                dto.isSalaryClient()
        );

        BigDecimal expected = EmploymentStatus.SELF_EMPLOYED.applyRate(BASE_RATE);
        BigDecimal actual = scoringService.applyEmploymentScoring(valid, BASE_RATE);
        assertEquals(expected, actual);
    }

    @Test
    void applyPositionScoringWhenMidManager() {
        EmploymentDto employment = new EmploymentDto(
                EmploymentStatus.EMPLOYED,
                "123",
                BigDecimal.valueOf(50000),
                Position.MID_MANAGER,
                24,
                6
        );

        ScoringDataDto dto = buildValidDto();

        ScoringDataDto valid = new ScoringDataDto(
                dto.amount(),
                dto.term(),
                dto.firstName(),
                dto.lastName(),
                dto.middleName(),
                dto.gender(),
                dto.birthdate(),
                dto.passportSeries(),
                dto.passportNumber(),
                dto.passportIssueDate(),
                dto.passportIssueBranch(),
                dto.maritalStatus(),
                dto.dependentAmount(),
                employment,
                dto.accountNumber(),
                dto.isInsuranceEnabled(),
                dto.isSalaryClient()
        );

        BigDecimal expected = Position.MID_MANAGER.applyRate(BASE_RATE);
        BigDecimal actual = scoringService.applyPositionScoring(valid, BASE_RATE);
        assertEquals(expected, actual);
    }

    @Test
    void applyPositionScoringWhenTopManager() {
        EmploymentDto employment = new EmploymentDto(
                EmploymentStatus.EMPLOYED,
                "123",
                BigDecimal.valueOf(50000),
                Position.TOP_MANAGER,
                24,
                6
        );

        ScoringDataDto dto = buildValidDto();

        ScoringDataDto valid = new ScoringDataDto(
                dto.amount(),
                dto.term(),
                dto.firstName(),
                dto.lastName(),
                dto.middleName(),
                dto.gender(),
                dto.birthdate(),
                dto.passportSeries(),
                dto.passportNumber(),
                dto.passportIssueDate(),
                dto.passportIssueBranch(),
                dto.maritalStatus(),
                dto.dependentAmount(),
                employment,
                dto.accountNumber(),
                dto.isInsuranceEnabled(),
                dto.isSalaryClient()
        );

        BigDecimal expected = Position.TOP_MANAGER.applyRate(BASE_RATE);
        BigDecimal actual = scoringService.applyPositionScoring(valid, BASE_RATE);
        assertEquals(expected, actual);
    }

    @Test
    void applyPositionScoringWhenOther() {
        EmploymentDto employment = new EmploymentDto(
                EmploymentStatus.EMPLOYED,
                "123",
                BigDecimal.valueOf(50000),
                Position.OTHER,
                24,
                6
        );

        ScoringDataDto dto = buildValidDto();

        ScoringDataDto valid = new ScoringDataDto(
                dto.amount(),
                dto.term(),
                dto.firstName(),
                dto.lastName(),
                dto.middleName(),
                dto.gender(),
                dto.birthdate(),
                dto.passportSeries(),
                dto.passportNumber(),
                dto.passportIssueDate(),
                dto.passportIssueBranch(),
                dto.maritalStatus(),
                dto.dependentAmount(),
                employment,
                dto.accountNumber(),
                dto.isInsuranceEnabled(),
                dto.isSalaryClient()
        );

        BigDecimal expected = Position.OTHER.applyRate(BASE_RATE);
        BigDecimal actual = scoringService.applyPositionScoring(valid, BASE_RATE);
        assertEquals(expected, actual);
    }

    @Test
    void applyMaritalScoringWhenUnmarried() {
        ScoringDataDto dto = buildValidDto();

        ScoringDataDto valid = new ScoringDataDto(
                dto.amount(),
                dto.term(),
                dto.firstName(),
                dto.lastName(),
                dto.middleName(),
                dto.gender(),
                dto.birthdate(),
                dto.passportSeries(),
                dto.passportNumber(),
                dto.passportIssueDate(),
                dto.passportIssueBranch(),
                MaritalStatus.UNMARRIED,
                dto.dependentAmount(),
                dto.employment(),
                dto.accountNumber(),
                dto.isInsuranceEnabled(),
                dto.isSalaryClient()
        );

        BigDecimal expected = MaritalStatus.UNMARRIED.applyRate(BASE_RATE);
        BigDecimal actual = scoringService.applyMaritalStatusScoring(valid, BASE_RATE);
        assertEquals(expected, actual);
    }

    @Test
    void applyMaritalScoringWhenMarried() {
        ScoringDataDto dto = buildValidDto();

        ScoringDataDto valid = new ScoringDataDto(
                dto.amount(),
                dto.term(),
                dto.firstName(),
                dto.lastName(),
                dto.middleName(),
                dto.gender(),
                dto.birthdate(),
                dto.passportSeries(),
                dto.passportNumber(),
                dto.passportIssueDate(),
                dto.passportIssueBranch(),
                MaritalStatus.MARRIED,
                dto.dependentAmount(),
                dto.employment(),
                dto.accountNumber(),
                dto.isInsuranceEnabled(),
                dto.isSalaryClient()
        );

        BigDecimal expected = MaritalStatus.MARRIED.applyRate(BASE_RATE);
        BigDecimal actual = scoringService.applyMaritalStatusScoring(valid, BASE_RATE);
        assertEquals(expected, actual);
    }

    @Test
    void applyMaritalScoringWhenDivorced() {
        ScoringDataDto dto = buildValidDto();

        ScoringDataDto valid = new ScoringDataDto(
                dto.amount(),
                dto.term(),
                dto.firstName(),
                dto.lastName(),
                dto.middleName(),
                dto.gender(),
                dto.birthdate(),
                dto.passportSeries(),
                dto.passportNumber(),
                dto.passportIssueDate(),
                dto.passportIssueBranch(),
                MaritalStatus.DIVORCED,
                dto.dependentAmount(),
                dto.employment(),
                dto.accountNumber(),
                dto.isInsuranceEnabled(),
                dto.isSalaryClient()
        );

        BigDecimal expected = MaritalStatus.DIVORCED.applyRate(BASE_RATE);
        BigDecimal actual = scoringService.applyMaritalStatusScoring(valid, BASE_RATE);
        assertEquals(expected, actual);
    }

    @Test
    void applyGenderScoringWhenMaleAge29() {
        ScoringDataDto dto = buildValidDto();

        ScoringDataDto valid = new ScoringDataDto(
                dto.amount(),
                dto.term(),
                dto.firstName(),
                dto.lastName(),
                dto.middleName(),
                Gender.MALE,
                LocalDate.now().minusYears(29),
                dto.passportSeries(),
                dto.passportNumber(),
                dto.passportIssueDate(),
                dto.passportIssueBranch(),
                dto.maritalStatus(),
                dto.dependentAmount(),
                dto.employment(),
                dto.accountNumber(),
                dto.isInsuranceEnabled(),
                dto.isSalaryClient()
        );

        BigDecimal expected = BASE_RATE;
        BigDecimal actual = scoringService.applyGenderScoring(valid, BASE_RATE);
        assertEquals(expected, actual);
    }

    @Test
    void applyGenderScoringWhenMaleAge30() {
        ScoringDataDto dto = buildValidDto();

        ScoringDataDto valid = new ScoringDataDto(
                dto.amount(),
                dto.term(),
                dto.firstName(),
                dto.lastName(),
                dto.middleName(),
                Gender.MALE,
                LocalDate.now().minusYears(30),
                dto.passportSeries(),
                dto.passportNumber(),
                dto.passportIssueDate(),
                dto.passportIssueBranch(),
                dto.maritalStatus(),
                dto.dependentAmount(),
                dto.employment(),
                dto.accountNumber(),
                dto.isInsuranceEnabled(),
                dto.isSalaryClient()
        );

        BigDecimal expected = BASE_RATE.subtract(BigDecimal.valueOf(3));
        BigDecimal actual = scoringService.applyGenderScoring(valid, BASE_RATE);
        assertEquals(expected, actual);
    }

    @Test
    void applyGenderScoringWhenMaleAge55() {
        ScoringDataDto dto = buildValidDto();

        ScoringDataDto valid = new ScoringDataDto(
                dto.amount(),
                dto.term(),
                dto.firstName(),
                dto.lastName(),
                dto.middleName(),
                Gender.MALE,
                LocalDate.now().minusYears(55),
                dto.passportSeries(),
                dto.passportNumber(),
                dto.passportIssueDate(),
                dto.passportIssueBranch(),
                dto.maritalStatus(),
                dto.dependentAmount(),
                dto.employment(),
                dto.accountNumber(),
                dto.isInsuranceEnabled(),
                dto.isSalaryClient()
        );

        BigDecimal expected = BASE_RATE.subtract(BigDecimal.valueOf(3));
        BigDecimal actual = scoringService.applyGenderScoring(valid, BASE_RATE);
        assertEquals(expected, actual);
    }

    @Test
    void applyGenderScoringWhenMaleAge56() {
        ScoringDataDto dto = buildValidDto();

        ScoringDataDto valid = new ScoringDataDto(
                dto.amount(),
                dto.term(),
                dto.firstName(),
                dto.lastName(),
                dto.middleName(),
                Gender.MALE,
                LocalDate.now().minusYears(56),
                dto.passportSeries(),
                dto.passportNumber(),
                dto.passportIssueDate(),
                dto.passportIssueBranch(),
                dto.maritalStatus(),
                dto.dependentAmount(),
                dto.employment(),
                dto.accountNumber(),
                dto.isInsuranceEnabled(),
                dto.isSalaryClient()
        );

        BigDecimal expected = BASE_RATE;
        BigDecimal actual = scoringService.applyGenderScoring(valid, BASE_RATE);
        assertEquals(expected, actual);
    }

    @Test
    void applyGenderScoringWhenFemaleAge31() {
        ScoringDataDto dto = buildValidDto();

        ScoringDataDto valid = new ScoringDataDto(
                dto.amount(),
                dto.term(),
                dto.firstName(),
                dto.lastName(),
                dto.middleName(),
                Gender.FEMALE,
                LocalDate.now().minusYears(31),
                dto.passportSeries(),
                dto.passportNumber(),
                dto.passportIssueDate(),
                dto.passportIssueBranch(),
                dto.maritalStatus(),
                dto.dependentAmount(),
                dto.employment(),
                dto.accountNumber(),
                dto.isInsuranceEnabled(),
                dto.isSalaryClient()
        );

        BigDecimal expected = BASE_RATE;
        BigDecimal actual = scoringService.applyGenderScoring(valid, BASE_RATE);
        assertEquals(expected, actual);
    }

    @Test
    void applyGenderScoringWhenFemaleAge32() {
        ScoringDataDto dto = buildValidDto();

        ScoringDataDto valid = new ScoringDataDto(
                dto.amount(),
                dto.term(),
                dto.firstName(),
                dto.lastName(),
                dto.middleName(),
                Gender.FEMALE,
                LocalDate.now().minusYears(32),
                dto.passportSeries(),
                dto.passportNumber(),
                dto.passportIssueDate(),
                dto.passportIssueBranch(),
                dto.maritalStatus(),
                dto.dependentAmount(),
                dto.employment(),
                dto.accountNumber(),
                dto.isInsuranceEnabled(),
                dto.isSalaryClient()
        );

        BigDecimal expected = BASE_RATE.subtract(BigDecimal.valueOf(3));
        BigDecimal actual = scoringService.applyGenderScoring(valid, BASE_RATE);
        assertEquals(expected, actual);
    }

    @Test
    void applyGenderScoringWhenFemaleAge60() {
        ScoringDataDto dto = buildValidDto();

        ScoringDataDto valid = new ScoringDataDto(
                dto.amount(),
                dto.term(),
                dto.firstName(),
                dto.lastName(),
                dto.middleName(),
                Gender.FEMALE,
                LocalDate.now().minusYears(60),
                dto.passportSeries(),
                dto.passportNumber(),
                dto.passportIssueDate(),
                dto.passportIssueBranch(),
                dto.maritalStatus(),
                dto.dependentAmount(),
                dto.employment(),
                dto.accountNumber(),
                dto.isInsuranceEnabled(),
                dto.isSalaryClient()
        );

        BigDecimal expected = BASE_RATE.subtract(BigDecimal.valueOf(3));
        BigDecimal actual = scoringService.applyGenderScoring(valid, BASE_RATE);
        assertEquals(expected, actual);
    }

    @Test
    void applyGenderScoringWhenFemaleAge61() {
        ScoringDataDto dto = buildValidDto();

        ScoringDataDto valid = new ScoringDataDto(
                dto.amount(),
                dto.term(),
                dto.firstName(),
                dto.lastName(),
                dto.middleName(),
                Gender.FEMALE,
                LocalDate.now().minusYears(61),
                dto.passportSeries(),
                dto.passportNumber(),
                dto.passportIssueDate(),
                dto.passportIssueBranch(),
                dto.maritalStatus(),
                dto.dependentAmount(),
                dto.employment(),
                dto.accountNumber(),
                dto.isInsuranceEnabled(),
                dto.isSalaryClient()
        );

        BigDecimal expected = BASE_RATE;
        BigDecimal actual = scoringService.applyGenderScoring(valid, BASE_RATE);
        assertEquals(expected, actual);
    }

    @Test
    void applyGenderScoringWhenNonBinary() {
        ScoringDataDto dto = buildValidDto();

        ScoringDataDto valid = new ScoringDataDto(
                dto.amount(),
                dto.term(),
                dto.firstName(),
                dto.lastName(),
                dto.middleName(),
                Gender.NON_BINARY,
                dto.birthdate(),
                dto.passportSeries(),
                dto.passportNumber(),
                dto.passportIssueDate(),
                dto.passportIssueBranch(),
                dto.maritalStatus(),
                dto.dependentAmount(),
                dto.employment(),
                dto.accountNumber(),
                dto.isInsuranceEnabled(),
                dto.isSalaryClient()
        );

        BigDecimal expected = BASE_RATE.add(BigDecimal.valueOf(7));
        BigDecimal actual = scoringService.applyGenderScoring(valid, BASE_RATE);
        assertEquals(expected, actual);
    }

    @Test
    void calculateRateSuccess() {

        when(preScoringService.calculatePrescoringRate(false,false))
                .thenReturn(BigDecimal.valueOf(20));

        BigDecimal expected = BigDecimal.valueOf(17);
        BigDecimal result = scoringService.calculateRate(buildValidDto());

        assertEquals(0, result.compareTo(expected));
    }

    @Test
    void validateShouldThrowExceptionWhenAgeInvalid() {
        ScoringDataDto dto = buildValidDto();

        ScoringDataDto invalid = new ScoringDataDto(
                dto.amount(),
                dto.term(),
                dto.firstName(),
                dto.lastName(),
                dto.middleName(),
                dto.gender(),
                LocalDate.now().minusYears(10),
                dto.passportSeries(),
                dto.passportNumber(),
                dto.passportIssueDate(),
                dto.passportIssueBranch(),
                dto.maritalStatus(),
                dto.dependentAmount(),
                dto.employment(),
                dto.accountNumber(),
                dto.isInsuranceEnabled(),
                dto.isSalaryClient()
        );

        assertThrows(ScoringException.class,
                () -> scoringService.calculateRate(invalid));
    }


    @Test
    void validateShouldThrowExceptionWhenUnemployed() {
        EmploymentDto employment = new EmploymentDto(
                EmploymentStatus.UNEMPLOYED,
                "123",
                BigDecimal.valueOf(50000),
                Position.OTHER,
                24,
                6
        );

        ScoringDataDto dto = buildValidDto();

        ScoringDataDto invalid = new ScoringDataDto(
                dto.amount(),
                dto.term(),
                dto.firstName(),
                dto.lastName(),
                dto.middleName(),
                dto.gender(),
                dto.birthdate(),
                dto.passportSeries(),
                dto.passportNumber(),
                dto.passportIssueDate(),
                dto.passportIssueBranch(),
                dto.maritalStatus(),
                dto.dependentAmount(),
                employment,
                dto.accountNumber(),
                dto.isInsuranceEnabled(),
                dto.isSalaryClient()
        );

        assertThrows(ScoringException.class,
                () -> scoringService.calculateRate(invalid));
    }

    @Test
    void validateShouldThrowExceptionWhenAmountTooLarge() {
        ScoringDataDto dto = buildValidDto();

        ScoringDataDto invalid = new ScoringDataDto(
                BigDecimal.valueOf(10000000),
                dto.term(),
                dto.firstName(),
                dto.lastName(),
                dto.middleName(),
                dto.gender(),
                dto.birthdate(),
                dto.passportSeries(),
                dto.passportNumber(),
                dto.passportIssueDate(),
                dto.passportIssueBranch(),
                dto.maritalStatus(),
                dto.dependentAmount(),
                dto.employment(),
                dto.accountNumber(),
                dto.isInsuranceEnabled(),
                dto.isSalaryClient()
        );

        assertThrows(ScoringException.class,
                () -> scoringService.calculateRate(invalid));
    }

    @Test
    void validateShouldThrowExceptionWhenTotalWorkExperienceInvalid() {
        EmploymentDto employment = new EmploymentDto(
                EmploymentStatus.EMPLOYED,
                "123",
                BigDecimal.valueOf(50000),
                Position.OTHER,
                17,
                6
        );

        ScoringDataDto dto = buildValidDto();

        ScoringDataDto invalid = new ScoringDataDto(
                dto.amount(),
                dto.term(),
                dto.firstName(),
                dto.lastName(),
                dto.middleName(),
                dto.gender(),
                dto.birthdate(),
                dto.passportSeries(),
                dto.passportNumber(),
                dto.passportIssueDate(),
                dto.passportIssueBranch(),
                dto.maritalStatus(),
                dto.dependentAmount(),
                employment,
                dto.accountNumber(),
                dto.isInsuranceEnabled(),
                dto.isSalaryClient()
        );

        assertThrows(ScoringException.class,
                () -> scoringService.calculateRate(invalid));
    }

    @Test
    void validateShouldThrowExceptionWhenCurrentWorkExperienceInvalid() {
        EmploymentDto employment = new EmploymentDto(
                EmploymentStatus.EMPLOYED,
                "123",
                BigDecimal.valueOf(50000),
                Position.OTHER,
                20,
                2
        );

        ScoringDataDto dto = buildValidDto();

        ScoringDataDto invalid = new ScoringDataDto(
                dto.amount(),
                dto.term(),
                dto.firstName(),
                dto.lastName(),
                dto.middleName(),
                dto.gender(),
                dto.birthdate(),
                dto.passportSeries(),
                dto.passportNumber(),
                dto.passportIssueDate(),
                dto.passportIssueBranch(),
                dto.maritalStatus(),
                dto.dependentAmount(),
                employment,
                dto.accountNumber(),
                dto.isInsuranceEnabled(),
                dto.isSalaryClient()
        );

        assertThrows(ScoringException.class,
                () -> scoringService.calculateRate(invalid));
    }

    @Test
    void calculateRateShouldThrowExceptionWhenAgeInvalid() {

        ScoringDataDto dto = buildValidDto();

        ScoringDataDto invalid = new ScoringDataDto(
                dto.amount(),
                dto.term(),
                dto.firstName(),
                dto.lastName(),
                dto.middleName(),
                dto.gender(),
                LocalDate.now().minusYears(10),
                dto.passportSeries(),
                dto.passportNumber(),
                dto.passportIssueDate(),
                dto.passportIssueBranch(),
                dto.maritalStatus(),
                dto.dependentAmount(),
                dto.employment(),
                dto.accountNumber(),
                dto.isInsuranceEnabled(),
                dto.isSalaryClient()
        );

        assertThrows(ScoringException.class,
                () -> scoringService.calculateRate(invalid));
    }

    @Test
    void calculateRateShouldThrowExceptionWhenUnemployed() {

        EmploymentDto employment = new EmploymentDto(
                EmploymentStatus.UNEMPLOYED,
                "123",
                BigDecimal.valueOf(50000),
                Position.OTHER,
                24,
                6
        );

        ScoringDataDto dto = buildValidDto();

        ScoringDataDto invalid = new ScoringDataDto(
                dto.amount(),
                dto.term(),
                dto.firstName(),
                dto.lastName(),
                dto.middleName(),
                dto.gender(),
                dto.birthdate(),
                dto.passportSeries(),
                dto.passportNumber(),
                dto.passportIssueDate(),
                dto.passportIssueBranch(),
                dto.maritalStatus(),
                dto.dependentAmount(),
                employment,
                dto.accountNumber(),
                dto.isInsuranceEnabled(),
                dto.isSalaryClient()
        );

        assertThrows(ScoringException.class,
                () -> scoringService.calculateRate(invalid));
    }

}
