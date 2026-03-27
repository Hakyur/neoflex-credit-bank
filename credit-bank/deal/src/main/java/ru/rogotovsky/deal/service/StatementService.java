package ru.rogotovsky.deal.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.rogotovsky.deal.entity.Client;
import ru.rogotovsky.deal.entity.Statement;
import ru.rogotovsky.deal.entity.StatusHistory;
import ru.rogotovsky.deal.enums.ApplicationStatus;
import ru.rogotovsky.deal.enums.ChangeType;
import ru.rogotovsky.deal.exception.StatementNotFoundException;
import ru.rogotovsky.deal.repository.StatementRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static ru.rogotovsky.deal.util.StringForExceptionsUtils.STATEMENT_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatementService {

    private final StatementRepository repository;

    public Statement getById(UUID id) {
        return repository.findById(id).orElseThrow(
                () -> new StatementNotFoundException(STATEMENT_NOT_FOUND.formatted(id))
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
        log.debug("Saving statement id={}", statement.getStatementId());
        return repository.save(statement);
    }

    public Statement updateStatus(Statement statement, ApplicationStatus status, ChangeType changeType) {
        log.debug("Updating statement status to {}", status);

        statement.setStatus(status);
        statement.getStatusHistory().add(
                new StatusHistory(status, LocalDateTime.now(), changeType)
        );
        return statement;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateStatusToDenied(Statement statement) {
        log.debug("Updating statement status to CC_DENIED id={}", statement.getStatementId());
        statement.setStatus(ApplicationStatus.CC_DENIED);
        statement.getStatusHistory().add(
                new StatusHistory(ApplicationStatus.CC_DENIED, LocalDateTime.now(), ChangeType.AUTOMATIC)
        );
        repository.save(statement);
    }
}
