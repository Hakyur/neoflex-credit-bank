package ru.rogotovsky.statement.dto;

import jakarta.validation.constraints.*;
import ru.rogotovsky.statement.validator.AgeVerification;

import java.math.BigDecimal;
import java.time.LocalDate;

public record LoanStatementRequestDto (

        @NotNull(message = "Amount must not be null")
        @DecimalMin(value = "20000", message = "Amount must be at least 20000")
        BigDecimal amount,

        @NotNull(message = "Term must not be null")
        @Min(value = 6, message = "Loan term must be at least 6 month")
        Integer term,

        @NotBlank(message = "First name must not be blank")
        @Pattern(
                regexp = "^[A-Za-z]{2,30}$",
                message = "First name must contain 2-30 Latin letters"
        )
        String firstName,

        @NotBlank(message = "Last name must not be blank")
        @Pattern(
                regexp = "^[A-Za-z]{2,30}$",
                message = "Last name must contain 2-30 Latin letters"
        )
        String lastName,

        @Pattern(
                regexp = "^[A-Za-z]{2,30}$",
                message = "Middle name must contain 2-30 Latin letters"
        )
        String middleName,

        @NotBlank(message = "Email must not be blank")
        @Pattern(
                regexp = "^[a-z0-9A-Z_!#$%&'*+/=?`{|}~^.-]+@[a-z0-9A-Z.-]+$",
                message = "Email does not match required pattern"
        )
        String email,

        @NotNull(message = "Birthdate must not be null")
        @Past(message = "Birthdate must be in the past")
        @AgeVerification
        LocalDate birthdate,

        @NotBlank(message = "Passport series must not be blank")
        @Pattern(
                regexp = "^\\d{4}$",
                message = "Passport series must contain exactly 4 digits"
        )
        String passportSeries,

        @NotBlank(message = "Passport number must not be blank")
        @Pattern(
                regexp = "^\\d{6}$",
                message = "Passport number must contain exactly 6 digits"
        )
        String passportNumber
) {}
