package ru.rogotovsky.deal.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.List;

@Schema(description = "Result of credit calculation")
public record CreditDto (

        @Schema(description = "Total loan amount including insurance", example = "500000")
        BigDecimal amount,

        @Schema(description = "Loan term in month", example = "12")
        Integer term,

        @Schema(description = "Monthly payment amount", example = "46317.25")
        BigDecimal monthlyPayment,

        @Schema(description = "Interest rate", example = "20")
        BigDecimal rate,

        @Schema(description = "Full cost of credit (PSK)", example = "11.16")
        BigDecimal psk,

        @Schema(description = "Indicates if insurance is enabled", example = "true")
        Boolean isInsuranceEnabled,

        @Schema(description = "Indicates if client is salary client", example = "true")
        Boolean isSalaryClient,

        @Schema(description = "Payment schedule")
        List<PaymentScheduleElementDto> paymentSchedule
) {}
