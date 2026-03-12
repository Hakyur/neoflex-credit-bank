package ru.rogotovsky.calculator.dto;

import java.time.LocalDateTime;

public record ErrorResponse (
        String message,
        String error,
        LocalDateTime timestamp
) {}
