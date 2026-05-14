package ru.rogotovsky.gateway.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Error response returned when request processing fails")
public record ErrorResponse(

        @Schema(description = "Error message", example = "Deal service unavailable")
        String message,

        @Schema(description = "Error type", example = "INTERNAL_SERVER_ERROR")
        String error,

        @Schema(description = "Timestamp when the error occurred", example = "2026-01-16T15:21:21")
        LocalDateTime timestamp
) {}
