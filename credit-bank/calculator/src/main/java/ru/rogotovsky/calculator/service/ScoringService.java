package ru.rogotovsky.calculator.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.rogotovsky.calculator.dto.ScoringDataDto;
import ru.rogotovsky.calculator.enums.EmploymentStatus;
import ru.rogotovsky.calculator.enums.Gender;
import ru.rogotovsky.calculator.exception.ScoringException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScoringService {

    private final PreScoringService preScoringService;

    public BigDecimal calculateRate(ScoringDataDto requestDto) {
        log.info("Starting scoring calculation for client: {} {}",
                requestDto.firstName(), requestDto.lastName());

        validate(requestDto);

        BigDecimal rate = preScoringService
                .calculatePrescoringRate(requestDto.isInsuranceEnabled(), requestDto.isSalaryClient());

        log.debug("Rate after prescoring: {}", rate);

        rate = applyEmploymentScoring(requestDto, rate);
        rate = applyPositionScoring(requestDto, rate);
        rate = applyMaritalStatusScoring(requestDto, rate);
        rate = applyGenderScoring(requestDto, rate);

        log.info("Final calculated rate: {}", rate);
        return rate;
    }

    private void validate(ScoringDataDto requestDto) {
        int age = Period.between(requestDto.birthdate(), LocalDate.now()).getYears();

        if (age < 20 || age > 65) {
            log.warn("Scoring validation failed: age {} is outside allowed range", age);
            throw new ScoringException("Age must be between 20 and 65");
        }

        if (requestDto.employment().employmentStatus() == EmploymentStatus.UNEMPLOYED) {
            log.warn("Scoring validation failed: client is unemployed");
            throw new ScoringException("Client is unemployed");
        }

        if (requestDto.amount().compareTo(requestDto.employment().salary().multiply(BigDecimal.valueOf(24))) > 0) {
            log.warn("Scoring validation failed: requested amount {} is too large for salary {}",
                    requestDto.amount(), requestDto.employment().salary());
            throw new ScoringException("Requested amount is too large");
        }

        if (requestDto.employment().workExperienceTotal() < 18) {
            log.warn("Scoring validation failed: total work experience {} months",
                    requestDto.employment().workExperienceTotal());
            throw new ScoringException("Total work experience must be at least 18 months");
        }

        if (requestDto.employment().workExperienceCurrent() < 3) {
            log.warn("Scoring validation failed: current work experience {} months",
                    requestDto.employment().workExperienceCurrent());
            throw new ScoringException("Current work experience must be at least 3 months");
        }
    }

    private BigDecimal applyEmploymentScoring(ScoringDataDto requestDto, BigDecimal rate) {
        BigDecimal newRate = requestDto.employment().employmentStatus().applyRate(rate);
        log.debug("Rate after employment scoring: {}", newRate);
        return newRate;
    }

    private BigDecimal applyPositionScoring(ScoringDataDto requestDto, BigDecimal rate) {
        BigDecimal newRate = requestDto.employment().position().applyRate(rate);

        log.debug("Rate after position scoring: {}", newRate);
        return newRate;
    }

    private BigDecimal applyMaritalStatusScoring(ScoringDataDto requestDto, BigDecimal rate) {
        BigDecimal newRate = requestDto.maritalStatus().applyRate(rate);

        log.debug("Rate after marital status scoring: {}", newRate);
        return newRate;
    }

    private BigDecimal applyGenderScoring(ScoringDataDto requestDto, BigDecimal rate) {
        int age = Period.between(requestDto.birthdate(), LocalDate.now()).getYears();
        BigDecimal newRate = rate;

        if (requestDto.gender() == Gender.FEMALE && age >= 32 && age <= 60) {
            newRate = requestDto.gender().applyRate(rate);
        } else if (requestDto.gender() == Gender.MALE && age >= 30 && age <= 55) {
            newRate = requestDto.gender().applyRate(rate);
        } else if (requestDto.gender() == Gender.NON_BINARY) {
            newRate = requestDto.gender().applyRate(rate);
        }

        log.debug("Rate after gender scoring: {}", newRate);
        return newRate;
    }
}
