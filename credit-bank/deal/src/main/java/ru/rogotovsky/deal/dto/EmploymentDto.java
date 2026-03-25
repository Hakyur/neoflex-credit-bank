package ru.rogotovsky.deal.dto;

import ru.rogotovsky.deal.enums.EmploymentPosition;
import ru.rogotovsky.deal.enums.EmploymentStatus;

import java.math.BigDecimal;

public record EmploymentDto(
        EmploymentStatus employmentStatus,
        String employerINN,
        BigDecimal salary,
        EmploymentPosition position,
        Integer workExperienceTotal,
        Integer workExperienceCurrent
) {
}
