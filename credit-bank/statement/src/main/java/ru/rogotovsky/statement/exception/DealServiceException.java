package ru.rogotovsky.statement.exception;

import lombok.Getter;
import ru.rogotovsky.statement.dto.ErrorResponse;

@Getter
public class DealServiceException extends RuntimeException {

    private ErrorResponse error;

    public DealServiceException(ErrorResponse error) {
        super(error.message());
        this.error = error;
    }

    public DealServiceException(String message) {
        super(message);
    }
}
