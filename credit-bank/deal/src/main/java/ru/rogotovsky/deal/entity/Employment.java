package ru.rogotovsky.deal.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.rogotovsky.deal.enums.EmploymentPosition;
import ru.rogotovsky.deal.enums.EmploymentStatus;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "employment")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employment {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID employmentId;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private EmploymentStatus status;

    @Column(name = "employer_inn")
    private String employerInn;

    @Column(name = "salary")
    private BigDecimal salary;

    @Column(name = "position")
    @Enumerated(EnumType.STRING)
    private EmploymentPosition position;

    @Column(name = "work_experience_total")
    private Integer workExperienceTotal;

    @Column(name = "work_experience_current")
    private Integer workExperienceCurrent;
}
