package ru.rogotovsky.gateway.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Employment status of the client")
public enum EmploymentStatus {
    UNEMPLOYED,
    SELF_EMPLOYED,
    EMPLOYED,
    BUSINESS_OWNER
}
