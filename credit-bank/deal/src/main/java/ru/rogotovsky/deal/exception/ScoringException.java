package ru.rogotovsky.deal.exception;

import lombok.Getter;
import ru.rogotovsky.deal.dto.ErrorResponse;

@Getter
public class ScoringException extends RuntimeException {

    private ErrorResponse error;

    public ScoringException(ErrorResponse error) {
        super(error.message());
        this.error = error;
    }

    public ScoringException(String message) {
        super(message);
    }

}
