package ru.rogotovsky.deal.handler;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.rogotovsky.deal.dto.ErrorResponse;
import ru.rogotovsky.deal.exception.CalculatorServiceException;
import ru.rogotovsky.deal.exception.InvalidSesCodeException;
import ru.rogotovsky.deal.exception.ScoringException;
import ru.rogotovsky.deal.exception.StatementNotFoundException;

import java.time.LocalDateTime;

@RestControllerAdvice
@Hidden
public class GlobalExceptionHandler {

    @ExceptionHandler(ScoringException.class)
    public ResponseEntity<ErrorResponse> handleScoringException(ScoringException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getError());
    }

    @ExceptionHandler(CalculatorServiceException.class)
    public ResponseEntity<ErrorResponse> handleCalculatorServiceException(CalculatorServiceException e) {
        if (e.getError() != null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getError());
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(
                    e.getMessage(),
                    "INTERNAL_SERVER_ERROR",
                    LocalDateTime.now()
            ));
        }
    }

    @ExceptionHandler(StatementNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleStatementNotFoundException(StatementNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ErrorResponse(
                        e.getMessage(),
                        "STATEMENT_NOT_FOUND",
                        LocalDateTime.now()
                ));
    }

    @ExceptionHandler(InvalidSesCodeException.class)
    public ResponseEntity<ErrorResponse> handleInvalidSesCodeException(InvalidSesCodeException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ErrorResponse(
                        e.getMessage(),
                        "INVALID_SES_CODE",
                        LocalDateTime.now()
                )
        );
    }
}
