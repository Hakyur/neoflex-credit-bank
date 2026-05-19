package ru.rogotovsky.gateway.dto;

import ru.rogotovsky.gateway.enums.ApplicationStatus;
import ru.rogotovsky.gateway.enums.ChangeType;

import java.time.LocalDateTime;

public record StatusHistory (
        ApplicationStatus status,
        LocalDateTime time,
        ChangeType changeType
) {}
