package ru.rogotovsky.statement.handler;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.rogotovsky.statement.dto.ErrorResponse;
import ru.rogotovsky.statement.exception.DealServiceException;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
@Hidden
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException e) {

        String message = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));

        ErrorResponse errorResponse = new ErrorResponse(
                message,
                "VALIDATION_ERROR",
                LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(DealServiceException.class)
    public ResponseEntity<ErrorResponse> handleDealServiceException(DealServiceException e) {
        if (e.getStatus() != null) {
            if (e.getError() != null) {
                return ResponseEntity.status(e.getStatus()).body(e.getError());
            } else {
                return ResponseEntity.status(e.getStatus()).body(new ErrorResponse(
                        e.getMessage(),
                        e.getStatus().toString(),
                        LocalDateTime.now()
                ));
            }
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(
                    e.getMessage(),
                    "INTERNAL_SERVER_ERROR",
                    LocalDateTime.now()
            ));
        }
    }
}
