package ru.rogotovsky.calculator.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.rogotovsky.calculator.enums.Gender;
import ru.rogotovsky.calculator.enums.MaritalStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Full scoring request data")
public record ScoringDataDto(

        @Schema(description = "Requested loan amount", example = "500000")
        BigDecimal amount,

        @Schema(description = "Loan term in month", example = "12")
        Integer term,

        @Schema(description = "Client first name", example = "Dmitry")
        String firstName,

        @Schema(description = "Client last name", example = "Rogotovsky")
        String lastName,

        @Schema(description = "Client middle name", example = "Vladimirovich")
        String middleName,

        @Schema(description = "Client gender")
        Gender gender,

        @Schema(description = "Client birthdate", example = "2005-01-06")
        LocalDate birthdate,

        @Schema(description = "Passport series", example = "1234")
        String passportSeries,

        @Schema(description = "Passport number", example = "567890")
        String passportNumber,

        @Schema(description = "Passport issue date", example = "2028-02-14")
        LocalDate passportIssueDate,

        @Schema(description = "Passport issue branch", example = "Ministry of Internal Affairs of Russia in the Voronezh region")
        String passportIssueBranch,

        @Schema(description = "Marital status")
        MaritalStatus maritalStatus,

        @Schema(description = "?")
        Integer dependentAmount,

        @Schema(description = "Employment information")
        EmploymentDto employment,

        @Schema(description = "Bank account number", example = "53252325236548565471")
        String accountNumber,

        @Schema(description = "Indicates if insurance is enabled", example = "true")
        Boolean isInsuranceEnabled,

        @Schema(description = "Indicates if client is salary client", example = "true")
        Boolean isSalaryClient
) {}
