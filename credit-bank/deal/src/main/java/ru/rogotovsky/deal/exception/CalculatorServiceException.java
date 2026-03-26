package ru.rogotovsky.deal.exception;

import lombok.Getter;
import ru.rogotovsky.deal.dto.ErrorResponse;

@Getter
public class CalculatorServiceException extends RuntimeException {

    private ErrorResponse error;

    public CalculatorServiceException(ErrorResponse error) {
        super(error.message());
        this.error = error;
    }

    public CalculatorServiceException(String message) {
        super(message);
    }

}
