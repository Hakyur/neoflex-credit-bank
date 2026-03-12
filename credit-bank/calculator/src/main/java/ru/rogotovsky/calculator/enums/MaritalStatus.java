package ru.rogotovsky.calculator.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Marital status of the client")
public enum MaritalStatus {
    UNMARRIED,
    MARRIED,
    DIVORCED
}
