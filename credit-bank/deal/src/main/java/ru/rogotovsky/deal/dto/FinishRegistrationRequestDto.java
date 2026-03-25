package ru.rogotovsky.deal.dto;

import ru.rogotovsky.deal.enums.Gender;
import ru.rogotovsky.deal.enums.MaritalStatus;

import java.time.LocalDate;

public record FinishRegistrationRequestDto (
        Gender gender,
        MaritalStatus maritalStatus,
        Integer dependentAmount,
        LocalDate passportIssueDate,
        String passportIssueBranch,
        EmploymentDto employment,
        String accountNumber
) {}
