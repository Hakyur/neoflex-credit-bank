package ru.rogotovsky.deal.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.rogotovsky.deal.enums.ApplicationStatus;
import ru.rogotovsky.deal.enums.ChangeType;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class StatusHistory {
    private ApplicationStatus status;
    private LocalDateTime time;
    private ChangeType changeType;
}
