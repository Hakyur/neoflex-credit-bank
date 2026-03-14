package ru.rogotovsky.calculator.enums;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Client position at work")
public enum Position {
    MID_MANAGER(BigDecimal.valueOf(-2)),
    TOP_MANAGER(BigDecimal.valueOf(-3)),
    OTHER(BigDecimal.ZERO);

    private final BigDecimal rateAdjustment;

    Position(BigDecimal rateAdjustment) {
        this.rateAdjustment = rateAdjustment;
    }

    public BigDecimal applyRate(BigDecimal rate) {
        return rate.add(rateAdjustment);
    }
}
