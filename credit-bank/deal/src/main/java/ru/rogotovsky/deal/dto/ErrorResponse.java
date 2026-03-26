package ru.rogotovsky.deal.dto;

import java.time.LocalDateTime;

public record ErrorResponse(
        String message,
        String error,
        LocalDateTime timestamp
) {}
