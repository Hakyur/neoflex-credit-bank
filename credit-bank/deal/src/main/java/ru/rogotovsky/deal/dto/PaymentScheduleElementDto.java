package ru.rogotovsky.deal.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Element of the payment schedule")
public record PaymentScheduleElementDto (

        @Schema(description = "Payment number", example = "1")
        Integer number,

        @Schema(description = "Payment date", example = "2026-03-12")
        LocalDate date,

        @Schema(description = "Total payment amount", example = "46317.25")
        BigDecimal totalPayment,

        @Schema(description = "Interest payment part", example = "8333.33")
        BigDecimal interestPayment,

        @Schema(description = "Principal payment part", example = "37983.92")
        BigDecimal debtPayment,

        @Schema(description = "Remaining debt after payment", example = "462016.08")
        BigDecimal remainingDebt
) {}
