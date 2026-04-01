package ru.rogotovsky.deal.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Error response returned when request processing fails")
public record ErrorResponse (

        @Schema(description = "Error message", example = "Age must be between 20 and 65")
        String message,

        @Schema(description = "Error type", example = "SCORING_ERROR")
        String error,

        @Schema(description = "Timestamp when the error occurred", example = "2026-01-16T15:21:21")
        LocalDateTime timestamp
) {}
