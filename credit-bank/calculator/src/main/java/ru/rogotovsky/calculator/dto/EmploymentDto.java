package ru.rogotovsky.calculator.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.rogotovsky.calculator.enums.EmploymentStatus;
import ru.rogotovsky.calculator.enums.Position;

import java.math.BigDecimal;

@Schema(description = "Employment information of the client")
public record EmploymentDto(

        @Schema(description = "Employment status")
        EmploymentStatus employmentStatus,

        @Schema(description = "Employer tax identification number", example = "362212345678")
        String employerINN,

        @Schema(description = "Monthly salary", example = "80000")
        BigDecimal salary,

        @Schema(description = "Position at work")
        Position position,

        @Schema(description = "Total work experience in month", example = "72")
        Integer workExperienceTotal,

        @Schema(description = "Current work experience in month", example = "24")
        Integer workExperienceCurrent
) {}
