package ru.rogotovsky.calculator.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.UUID;

@Schema(description = "Loan offer with calculated conditions")
public record LoanOfferDto(

        @Schema(description = "Unique identifier of loan statement", example = "4b3d9bc7-d9c2-416f-8158-5c8dbab41747")
        UUID statementId,

        @Schema(description = "Requested loan amount", example = "500000")
        BigDecimal requestedAmount,

        @Schema(description = "Total amount including insurance", example = "600000")
        BigDecimal totalAmount,

        @Schema(description = "Loan term in month", example = "12")
        Integer term,

        @Schema(description = "Monthly payment amount", example = "46317.25")
        BigDecimal monthlyPayment,

        @Schema(description = "Interest rate", example = "20")
        BigDecimal rate,

        @Schema(description = "Indicates if insurance is enabled", example = "true")
        Boolean isInsuranceEnabled,

        @Schema(description = "Indicates if client is salary client", example = "true")
        Boolean isSalaryClient
) {}
