package ru.rogotovsky.deal.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.rogotovsky.deal.entity.Client;
import ru.rogotovsky.deal.entity.Statement;
import ru.rogotovsky.deal.entity.StatusHistory;
import ru.rogotovsky.deal.enums.ApplicationStatus;
import ru.rogotovsky.deal.enums.ChangeType;
import ru.rogotovsky.deal.repository.StatementRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StatementService {

    private final StatementRepository repository;

    public Statement getById(UUID id) {
        return repository.findById(id).orElseThrow(
                () -> new RuntimeException("Statement not found")
        );
    }

    public Statement createStatement(Client client) {
        LocalDateTime time = LocalDateTime.now();

        Statement statement = new Statement();

        statement.setClient(client);
        statement.setStatus(ApplicationStatus.PREAPPROVAL);
        statement.setCreationDate(time);
        statement.setStatusHistory(List.of(
                new StatusHistory(ApplicationStatus.PREAPPROVAL, time, ChangeType.AUTOMATIC)
        ));

        return statement;
    }

    public Statement save(Statement statement) {
        return repository.save(statement);
    }

    public Statement updateStatus(Statement statement, ApplicationStatus status, ChangeType changeType) {
        statement.setStatus(status);
        statement.getStatusHistory().add(
                new StatusHistory(status, LocalDateTime.now(), changeType)
        );
        return statement;
    }
}
