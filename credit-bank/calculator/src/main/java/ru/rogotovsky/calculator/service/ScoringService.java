package ru.rogotovsky.calculator.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.rogotovsky.calculator.dto.ScoringDataDto;
import ru.rogotovsky.calculator.enums.EmploymentStatus;
import ru.rogotovsky.calculator.enums.Gender;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

@Service
@RequiredArgsConstructor
public class ScoringService {

    private final PreScoringService preScoringService;

    public BigDecimal calculateRate(ScoringDataDto requestDto) {
        validate(requestDto);

        BigDecimal rate = preScoringService
                .calculatePrescoringRate(requestDto.isInsuranceEnabled(), requestDto.isSalaryClient());

        rate = applyEmploymentScoring(requestDto, rate);
        rate = applyPositionScoring(requestDto, rate);
        rate = applyMaritalStatusScoring(requestDto, rate);
        rate = applyGenderScoring(requestDto, rate);

        return rate;
    }

    private void validate(ScoringDataDto requestDto) {
        int age = Period.between(requestDto.birthdate(), LocalDate.now()).getYears();

        if (age < 20 || age > 65) {
            throw new IllegalArgumentException("Age restriction");
        }

        if (requestDto.employment().employmentStatus() == EmploymentStatus.UNEMPLOYED) {
            throw new IllegalArgumentException("EmploymentStatus is unemployed");
        }

        if (requestDto.amount().compareTo(requestDto.employment().salary().multiply(BigDecimal.valueOf(24))) > 0) {
            throw new IllegalArgumentException("Loan amount too large");
        }

        if (requestDto.employment().workExperienceTotal() < 18) {
            throw new IllegalArgumentException("Not enough total work experience");
        }

        if (requestDto.employment().workExperienceCurrent() < 3) {
            throw new IllegalArgumentException("Not enough current work experience");
        }
    }

    private BigDecimal applyEmploymentScoring(ScoringDataDto requestDto, BigDecimal rate) {
        return switch (requestDto.employment().employmentStatus()) {
            case SELF_EMPLOYED -> rate.add(BigDecimal.valueOf(2));
            case BUSINESS_OWNER ->  rate.add(BigDecimal.ONE);
            default -> rate;
        };
    }

    private BigDecimal applyPositionScoring(ScoringDataDto requestDto, BigDecimal rate) {
        return switch (requestDto.employment().position()) {
            case MID_MANAGER -> rate.subtract(BigDecimal.valueOf(2));
            case TOP_MANAGER -> rate.subtract(BigDecimal.valueOf(3));
            default -> rate;
        };
    }

    private BigDecimal applyMaritalStatusScoring(ScoringDataDto requestDto, BigDecimal rate) {
        return switch (requestDto.maritalStatus()) {
            case MARRIED -> rate.subtract(BigDecimal.valueOf(3));
            case DIVORCED -> rate.add(BigDecimal.ONE);
            default -> rate;
        };
    }

    private BigDecimal applyGenderScoring(ScoringDataDto requestDto, BigDecimal rate) {
        int age = Period.between(requestDto.birthdate(), LocalDate.now()).getYears();

        if (requestDto.gender() == Gender.FEMALE && age >= 32 && age <= 60) {
            return rate.subtract(BigDecimal.valueOf(3));
        }

        if (requestDto.gender() == Gender.MALE && age >= 30 && age <= 55) {
            return rate.subtract(BigDecimal.valueOf(3));
        }

        if (requestDto.gender() == Gender.NON_BINARY) {
            return rate.add(BigDecimal.valueOf(7));
        }

        return rate;
    }
}
