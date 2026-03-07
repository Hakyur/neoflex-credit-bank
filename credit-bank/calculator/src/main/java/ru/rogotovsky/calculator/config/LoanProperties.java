package ru.rogotovsky.calculator.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "loan")
public class LoanProperties {

    private BigDecimal baseRate;
    private BigDecimal insuranceCost;
    private BigDecimal insuranceDiscount;
    private BigDecimal salaryClientDiscount;

}
