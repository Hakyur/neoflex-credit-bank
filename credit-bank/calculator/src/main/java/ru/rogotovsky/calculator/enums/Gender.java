package ru.rogotovsky.calculator.enums;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Gender of the client")
public enum Gender {
    MALE(BigDecimal.valueOf(-3)),
    FEMALE(BigDecimal.valueOf(-3)),
    NON_BINARY(BigDecimal.valueOf(7));

    private final BigDecimal rateAdjustment;

    Gender(BigDecimal rateAdjustment) {
        this.rateAdjustment = rateAdjustment;
    }

    public BigDecimal applyRate(BigDecimal rate) {
        return rate.add(rateAdjustment);
    }
}
