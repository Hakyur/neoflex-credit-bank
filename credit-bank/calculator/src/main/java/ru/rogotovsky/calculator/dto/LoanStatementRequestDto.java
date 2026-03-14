package ru.rogotovsky.calculator.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import ru.rogotovsky.calculator.validator.Adult;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Loan application request")
public record LoanStatementRequestDto(

        @NotNull(message = "Amount must not be null")
        @DecimalMin(value = "20000", message = "Amount must be at least 20000")
        @Schema(description = "Requested loan amount", example = "500000")
        BigDecimal amount,

        @NotNull(message = "Term must not be null")
        @Min(value = 6, message = "Loan term must be at least 6 month")
        @Schema(description = "Loan term in month", example = "12")
        Integer term,

        @NotBlank(message = "First name must not be blank")
        @Pattern(
                regexp = "^[A-Za-z]{2,30}$",
                message = "First name must contain 2-30 Latin letters"
        )
        @Schema(description = "Client first name", example = "Dmitry")
        String firstName,

        @NotBlank(message = "Last name must not be blank")
        @Pattern(
                regexp = "^[A-Za-z]{2,30}$",
                message = "Last name must contain 2-30 Latin letters"
        )
        @Schema(description = "Client last name", example = "Rogotovsky")
        String lastName,

        @Pattern(
                regexp = "^[A-Za-z]{2,30}$",
                message = "Middle name must contain 2-30 Latin letters"
        )
        @Schema(description = "Client middle name", example = "Vladimirovich")
        String middleName,

        @NotBlank(message = "Email must not be blank")
        @Pattern(
                regexp = "^[a-z0-9A-Z_!#$%&'*+/=?`{|}~^.-]+@[a-z0-9A-Z.-]+$",
                message = "Email does not match required pattern"
        )
        @Schema(description = "Client email", example = "drogotovsky@gmail.com")
        String email,

        @NotNull(message = "Birthdate must not be null")
        @Past(message = "Birthdate must be in the past")
        @Schema(description = "Client birthdate", example = "2005-01-06")
        LocalDate birthdate,

        @NotBlank(message = "Passport series must not be blank")
        @Pattern(
                regexp = "^\\d{4}$",
                message = "Passport series must contain exactly 4 digits"
        )
        @Schema(description = "Passport series", example = "1234")
        String passportSeries,

        @NotBlank(message = "Passport number must not be blank")
        @Pattern(
                regexp = "^\\d{6}$",
                message = "Passport number must contain exactly 6 digits"
        )
        @Schema(description = "Passport number", example = "567890")
        String passportNumber
) {}
