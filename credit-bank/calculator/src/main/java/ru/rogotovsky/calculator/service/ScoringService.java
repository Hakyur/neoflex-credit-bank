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

import static ru.rogotovsky.calculator.util.NumberForScoringUtils.*;
import static ru.rogotovsky.calculator.util.StringForExceptionsUtils.*;

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

    public void validate(ScoringDataDto requestDto) {
        int age = Period.between(requestDto.birthdate(), LocalDate.now()).getYears();

        if (age < MIN_VALID_AGE || age > MAX_VALID_AGE) {
            log.warn("Scoring validation failed: age {} is outside allowed range", age);
            throw new ScoringException(AGE_INVALID);
        }

        if (requestDto.employment().employmentStatus() == EmploymentStatus.UNEMPLOYED) {
            log.warn("Scoring validation failed: client is unemployed");
            throw new ScoringException(CLIENT_UNEMPLOYED);
        }

        if (requestDto.amount().compareTo(requestDto.employment().salary().multiply(BigDecimal.valueOf(MAX_SALARY_MULTIPLIER))) > 0) {
            log.warn("Scoring validation failed: requested amount {} is too large for salary {}",
                    requestDto.amount(), requestDto.employment().salary());
            throw new ScoringException(AMOUNT_TOO_LARGE);
        }

        if (requestDto.employment().workExperienceTotal() < MIN_TOTAL_WORK_EXPERIENCE) {
            log.warn("Scoring validation failed: total work experience {} months",
                    requestDto.employment().workExperienceTotal());
            throw new ScoringException(TOTAL_WORK_EXPERIENCE_INVALID);
        }

        if (requestDto.employment().workExperienceCurrent() < MIN_CURRENT_WORK_EXPERIENCE) {
            log.warn("Scoring validation failed: current work experience {} months",
                    requestDto.employment().workExperienceCurrent());
            throw new ScoringException(CURRENT_WORK_EXPERIENCE_INVALID);
        }
    }

    public BigDecimal applyEmploymentScoring(ScoringDataDto requestDto, BigDecimal rate) {
        BigDecimal newRate = requestDto.employment().employmentStatus().applyRate(rate);
        log.debug("Rate after employment scoring: {}", newRate);
        return newRate;
    }

    public BigDecimal applyPositionScoring(ScoringDataDto requestDto, BigDecimal rate) {
        BigDecimal newRate = requestDto.employment().position().applyRate(rate);

        log.debug("Rate after position scoring: {}", newRate);
        return newRate;
    }

    public BigDecimal applyMaritalStatusScoring(ScoringDataDto requestDto, BigDecimal rate) {
        BigDecimal newRate = requestDto.maritalStatus().applyRate(rate);

        log.debug("Rate after marital status scoring: {}", newRate);
        return newRate;
    }

    public BigDecimal applyGenderScoring(ScoringDataDto requestDto, BigDecimal rate) {
        int age = Period.between(requestDto.birthdate(), LocalDate.now()).getYears();
        BigDecimal newRate = rate;

        if (requestDto.gender() == Gender.FEMALE && age >= FEMALE_MIN_AGE && age <= FEMALE_MAX_AGE) {
            newRate = requestDto.gender().applyRate(rate);
        } else if (requestDto.gender() == Gender.MALE && age >= MALE_MIN_AGE && age <= MALE_MAX_AGE) {
            newRate = requestDto.gender().applyRate(rate);
        } else if (requestDto.gender() == Gender.NON_BINARY) {
            newRate = requestDto.gender().applyRate(rate);
        }

        log.debug("Rate after gender scoring: {}", newRate);
        return newRate;
    }
}
