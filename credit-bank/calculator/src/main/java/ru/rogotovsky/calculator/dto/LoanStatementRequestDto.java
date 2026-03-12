package ru.rogotovsky.calculator.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Loan application request")
public record LoanStatementRequestDto(

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

        @Schema(description = "Client email", example = "drogotovsky@gmail.com")
        String email,

        @Schema(description = "Client birthdate", example = "2005-01-06")
        LocalDate birthdate,

        @Schema(description = "Passport series", example = "1234")
        String passportSeries,

        @Schema(description = "Passport number", example = "567890")
        String passportNumber
) {}
