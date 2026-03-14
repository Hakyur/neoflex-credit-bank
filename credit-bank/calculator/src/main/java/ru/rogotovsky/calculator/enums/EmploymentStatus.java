package ru.rogotovsky.calculator.enums;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "Employment status of the client")
public enum EmploymentStatus {
    UNEMPLOYED(BigDecimal.ZERO),
    SELF_EMPLOYED(BigDecimal.valueOf(2)),
    BUSINESS_OWNER(BigDecimal.ONE),
    EMPLOYED(BigDecimal.ZERO);

    private final BigDecimal rateAdjustment;

    EmploymentStatus(BigDecimal rateAdjustment) {
        this.rateAdjustment = rateAdjustment;
    }

    public BigDecimal applyRate(BigDecimal rate) {
        return rate.add(rateAdjustment);
    }
}
