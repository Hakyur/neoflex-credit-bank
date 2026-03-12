package ru.rogotovsky.calculator.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Client position at work")
public enum Position {
    MID_MANAGER,
    TOP_MANAGER,
    OTHER
}
