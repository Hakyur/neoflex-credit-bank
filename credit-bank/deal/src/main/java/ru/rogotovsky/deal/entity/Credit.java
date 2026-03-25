package ru.rogotovsky.deal.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.rogotovsky.deal.enums.CreditStatus;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "credit")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Credit {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID creditId;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "term")
    private Integer term;

    @Column(name = "monthly_payment")
    private BigDecimal monthlyPayment;

    @Column(name = "rate")
    private BigDecimal rate;

    @Column(name = "psk")
    private BigDecimal psk;

    @Column(name = "payment_schedule", columnDefinition = "jsonb")
    private String paymentSchedule;

    @Column(name = "insurance_enabled")
    private Boolean insuranceEnabled;

    @Column(name = "salary_client")
    private Boolean salaryClient;

    @Column(name = "credit_status")
    @Enumerated(EnumType.STRING)
    private CreditStatus creditStatus;
}
