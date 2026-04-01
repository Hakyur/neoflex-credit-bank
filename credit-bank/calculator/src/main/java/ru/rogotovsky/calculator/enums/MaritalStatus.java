package ru.rogotovsky.calculator.enums;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Marital status of the client")
public enum MaritalStatus {
    SINGLE(BigDecimal.ZERO),
    MARRIED(BigDecimal.valueOf(-3)),
    DIVORCED(BigDecimal.ONE),
    WIDOW_WIDOWER(BigDecimal.ZERO);

    private final BigDecimal rateAdjustment;

    MaritalStatus(BigDecimal rateAdjustment) {
        this.rateAdjustment = rateAdjustment;
    }

    public BigDecimal applyRate(BigDecimal rate) {
        return rate.add(rateAdjustment);
    }
}
