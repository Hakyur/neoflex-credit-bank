package ru.rogotovsky.statement.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import ru.rogotovsky.statement.dto.ErrorResponse;

@Getter
public class DealServiceException extends RuntimeException {

    private HttpStatus status;
    private ErrorResponse error;

    public DealServiceException(HttpStatus status, ErrorResponse error) {
        super(error.message());
        this.status = status;
        this.error = error;
    }

    public DealServiceException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }
}
