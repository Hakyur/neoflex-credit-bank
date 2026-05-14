package ru.rogotovsky.gateway.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Marital status of the client")
public enum MaritalStatus {
    MARRIED,
    DIVORCED,
    SINGLE,
    WIDOW_WIDOWER
}
