package ru.rogotovsky.gateway.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import ru.rogotovsky.gateway.dto.ErrorResponse;

@Getter
public class GatewayServiceException extends RuntimeException {

    private HttpStatus status;
    private ErrorResponse error;

    public GatewayServiceException(HttpStatus status, ErrorResponse error) {
        super(error.message());
        this.status = status;
        this.error = error;
    }

    public GatewayServiceException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }
}
