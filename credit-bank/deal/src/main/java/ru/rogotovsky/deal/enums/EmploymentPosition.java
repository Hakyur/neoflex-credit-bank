package ru.rogotovsky.deal.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Client position at work")
public enum EmploymentPosition {
    WORKER,
    MID_MANAGER,
    TOP_MANAGER,
    OWNER
}
