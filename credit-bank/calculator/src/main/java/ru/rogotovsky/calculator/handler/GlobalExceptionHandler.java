package ru.rogotovsky.calculator.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.rogotovsky.calculator.dto.ErrorResponse;
import ru.rogotovsky.calculator.exception.ScoringException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ScoringException.class)
    public ResponseEntity<ErrorResponse> handleScoringException(ScoringException e) {
        ErrorResponse errorResponse = new ErrorResponse(
                e.getMessage(),
                "SCORING_ERROR",
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        ErrorResponse errorResponse = new ErrorResponse(
                e.getMessage(),
                "INTERNAL_SERVER_ERROR",
                LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
