package ru.rogotovsky.deal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.rogotovsky.deal.entity.Client;
import ru.rogotovsky.deal.entity.Statement;
import ru.rogotovsky.deal.entity.StatusHistory;
import ru.rogotovsky.deal.enums.ApplicationStatus;
import ru.rogotovsky.deal.enums.ChangeType;
import ru.rogotovsky.deal.exception.StatementNotFoundException;
import ru.rogotovsky.deal.repository.StatementRepository;
import ru.rogotovsky.deal.service.StatementService;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StatementServiceTest {

    @Mock
    private StatementRepository repository;

    private StatementService service;

    @BeforeEach
    public void setup() {
        service = new StatementService(repository);
    }

    @Test
    void getByIdWhenExists() {
        UUID id = UUID.randomUUID();

        Statement expected = new Statement();
        expected.setStatementId(id);

        when(repository.findById(id)).thenReturn(Optional.of(expected));

        Statement actual = service.getById(id);

        assertEquals(expected, actual);
        verify(repository).findById(id);
    }

    @Test
    void getByIdWhenNotFound() {
        UUID id = UUID.randomUUID();

        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getById(id))
                .isInstanceOf(StatementNotFoundException.class)
                .hasMessageContaining(id.toString());

        verify(repository).findById(id);
    }

    @Test
    void createStatementSuccess() {
        Client client = new Client();
        Statement actual = service.createStatement(client);

        assertEquals(client, actual.getClient());
        assertEquals(ApplicationStatus.PREAPPROVAL, actual.getStatus());
        assertThat(actual.getCreationDate()).isNotNull();
        assertThat(actual.getStatusHistory()).hasSize(1);

        StatusHistory history = actual.getStatusHistory().getFirst();
        assertEquals(ApplicationStatus.PREAPPROVAL, history.getStatus());
        assertEquals(ChangeType.AUTOMATIC, history.getChangeType());
        assertThat(history.getTime()).isNotNull();
    }

    @Test
    void saveStatementSuccess() {
        Statement expected = new Statement();
        when(repository.save(expected)).thenReturn(expected);
        Statement actual = service.save(expected);

        assertEquals(expected, actual);
        verify(repository).save(expected);
    }

    @Test
    void updateStatusSuccess() {
        Statement statement = new Statement();
        statement.setStatusHistory(new ArrayList<>());

        Statement actual = service.updateStatus(
                statement,
                ApplicationStatus.APPROVED,
                ChangeType.AUTOMATIC
        );

        assertEquals(ApplicationStatus.APPROVED, actual.getStatus());
        assertThat(actual.getStatusHistory()).hasSize(1);

        StatusHistory history = actual.getStatusHistory().getFirst();
        assertEquals(ApplicationStatus.APPROVED, history.getStatus());
        assertEquals(ChangeType.AUTOMATIC, history.getChangeType());
    }

    @Test
    void updateStatusToDenied() {
        Statement statement = new Statement();
        statement.setStatementId(UUID.randomUUID());
        statement.setStatusHistory(new ArrayList<>());

        when(repository.save(statement)).thenReturn(statement);
        service.updateStatusToDenied(statement);

        assertEquals(ApplicationStatus.CC_DENIED, statement.getStatus());

        StatusHistory history = statement.getStatusHistory().getLast();
        assertEquals(ApplicationStatus.CC_DENIED, history.getStatus());
        assertEquals(ChangeType.AUTOMATIC, history.getChangeType());

        verify(repository).save(statement);
    }
}
