package ru.rogotovsky.calculator.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Employment status of the client")
public enum EmploymentStatus {
    UNEMPLOYED,
    SELF_EMPLOYED,
    BUSINESS_OWNER,
    EMPLOYED
}
