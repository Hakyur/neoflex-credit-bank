package ru.rogotovsky.deal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Loan offer with calculated conditions")
public class LoanOfferDto {

        @Schema(description = "Unique identifier of loan statement", example = "4b3d9bc7-d9c2-416f-8158-5c8dbab41747")
        private UUID statementId;

        @Schema(description = "Requested loan amount", example = "500000")
        private BigDecimal requestedAmount;

        @Schema(description = "Total amount including insurance", example = "600000")
        private BigDecimal totalAmount;

        @Schema(description = "Loan term in month", example = "12")
        private Integer term;

        @Schema(description = "Monthly payment amount", example = "46317.25")
        private BigDecimal monthlyPayment;

        @Schema(description = "Interest rate", example = "20")
        private BigDecimal rate;

        @Schema(description = "Indicates if insurance is enabled", example = "true")
        private Boolean isInsuranceEnabled;

        @Schema(description = "Indicates if client is salary client", example = "true")
        private Boolean isSalaryClient;
}
