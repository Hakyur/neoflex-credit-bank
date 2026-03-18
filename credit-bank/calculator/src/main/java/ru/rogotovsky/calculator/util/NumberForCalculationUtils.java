package ru.rogotovsky.calculator.util;

import java.math.BigDecimal;

public class NumberForCalculationUtils {
    public static BigDecimal PERCENT_DIVISOR = BigDecimal.valueOf(100);
    public static BigDecimal PERCENT_MULTIPLICATOR = BigDecimal.valueOf(100);
    public static BigDecimal MONTH_IN_YEAR = BigDecimal.valueOf(12);

    public static final int MONEY_SCALE = 2;
    public static final int CALCULATION_SCALE = 10;
}
