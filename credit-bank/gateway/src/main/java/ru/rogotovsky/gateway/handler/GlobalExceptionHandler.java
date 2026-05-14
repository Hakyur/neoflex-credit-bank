package ru.rogotovsky.gateway.handler;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.rogotovsky.gateway.dto.ErrorResponse;
import ru.rogotovsky.gateway.exception.GatewayServiceException;

import java.time.LocalDateTime;

@RestControllerAdvice
@Hidden
public class GlobalExceptionHandler {

    @ExceptionHandler(GatewayServiceException.class)
    public ResponseEntity<ErrorResponse> handleGatewayServiceException(GatewayServiceException e) {
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
