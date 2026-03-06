package ru.rogotovsky.calculator.dto;

import ru.rogotovsky.calculator.enums.EmploymentStatus;
import ru.rogotovsky.calculator.enums.Position;

import java.math.BigDecimal;

public record EmploymentDto(
        EmploymentStatus employmentStatus,
        String employerINN,
        BigDecimal salary,
        Position position,
        Integer workExperienceTotal,
        Integer workExperienceCurrent
) {}
