package ru.rogotovsky.gateway.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.rogotovsky.gateway.enums.ApplicationStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Schema(description = "Loan statement information")
public record StatementDto(

        @Schema(description = "Unique identifier of loan statement", example = "4b3d9bc7-d9c2-416f-8158-5c8dbab41747")
        UUID statementId,

        @Schema(description = "Unique identifier of loan statement", example = "21c5468a-4701-4347-8ada-6135bf979c53")
        UUID clientId,

        @Schema(description = "Calculated credit information")
        CreditDto credit,

        @Schema(description = "Current statement status", example = "CC_APPROVED")
        ApplicationStatus status,

        @Schema(description = "Statement creation date and time", example = "2026-05-14T09:04:20")
        LocalDateTime creationDate,

        @Schema(description = "Applied loan offer")
        LoanOfferDto appliedOffer,

        @Schema(description = "Date and time when documents were signed", example = "2026-05-15T12:45:00")
        LocalDateTime signDate,

        @Schema(description = "SES verification code", example = "123456")
        String sesCode,

        @Schema(description = "Statement status history")
        List<StatusHistory> statusHistory
) {}
