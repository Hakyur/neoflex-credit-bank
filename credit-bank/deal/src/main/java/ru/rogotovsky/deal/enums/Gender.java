package ru.rogotovsky.deal.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Gender of the client")
public enum Gender {
    MALE,
    FEMALE,
    NON_BINARY
}
