package ru.rogotovsky.gateway.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.rogotovsky.gateway.enums.Gender;
import ru.rogotovsky.gateway.enums.MaritalStatus;

import java.time.LocalDate;

@Schema(description = "DTO containing full client data required to finalize the registration and calculate the credit")
public record FinishRegistrationRequestDto(

        @Schema(description = "Client gender")
        Gender gender,

        @Schema(description = "Marital status")
        MaritalStatus maritalStatus,

        @Schema(description = "Number of dependents", example = "2")
        Integer dependentAmount,

        @Schema(description = "Passport issue date", example = "2028-02-14")
        LocalDate passportIssueDate,

        @Schema(description = "Passport issue branch", example = "Ministry of Internal Affairs of Russia in the Voronezh region")
        String passportIssueBranch,

        @Schema(description = "Employment information")
        EmploymentDto employment,

        @Schema(description = "Bank account number", example = "53252325236548565471")
        String accountNumber
) {}
