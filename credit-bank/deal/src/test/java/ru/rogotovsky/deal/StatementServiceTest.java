package ru.rogotovsky.deal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.rogotovsky.deal.dto.StatementDto;
import ru.rogotovsky.deal.entity.Client;
import ru.rogotovsky.deal.entity.Statement;
import ru.rogotovsky.deal.entity.StatusHistory;
import ru.rogotovsky.deal.enums.ApplicationStatus;
import ru.rogotovsky.deal.enums.ChangeType;
import ru.rogotovsky.deal.exception.StatementNotFoundException;
import ru.rogotovsky.deal.mapper.StatementMapper;
import ru.rogotovsky.deal.repository.StatementRepository;
import ru.rogotovsky.deal.service.EmailEventProducer;
import ru.rogotovsky.deal.service.StatementService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StatementServiceTest {

    @Mock
    private StatementRepository repository;

    @Mock
    private EmailEventProducer emailEventProducer;

    @Mock
    private StatementMapper statementMapper;

    @InjectMocks
    private StatementService service;

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

        when(repository.findById(statement.getStatementId())).thenReturn(Optional.of(statement));
        when(repository.save(statement)).thenReturn(statement);
        doNothing().when(emailEventProducer).sendStatementDenied(statement);
        service.updateStatusToDenied(statement.getStatementId());

        assertEquals(ApplicationStatus.CC_DENIED, statement.getStatus());

        StatusHistory history = statement.getStatusHistory().getLast();
        assertEquals(ApplicationStatus.CC_DENIED, history.getStatus());
        assertEquals(ChangeType.AUTOMATIC, history.getChangeType());

        verify(repository).save(statement);
    }

    @Test
    void getStatementByIdSuccess() {
        UUID statementId = UUID.randomUUID();

        Statement statement = new Statement();
        statement.setStatementId(statementId);

        StatementDto expected = new StatementDto(
                statementId,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );

        when(repository.findById(statementId)).thenReturn(Optional.of(statement));
        when(statementMapper.toDto(statement)).thenReturn(expected);

        StatementDto actual = service.getStatementById(statementId);

        assertEquals(expected, actual);

        verify(repository).findById(statementId);
        verify(statementMapper).toDto(statement);
    }

    @Test
    void getStatementByIdWhenNotFound() {
        UUID statementId = UUID.randomUUID();

        when(repository.findById(statementId)).thenReturn(Optional.empty());

        StatementNotFoundException exception = assertThrows(
                StatementNotFoundException.class,
                () -> service.getStatementById(statementId)
        );

        assertEquals("Statement with id = %s not found".formatted(statementId), exception.getMessage());

        verify(repository).findById(statementId);
        verify(statementMapper, times(0)).toDto(any(Statement.class));
    }

    @Test
    void getAllStatementsSuccess() {
        Statement statement1 = new Statement();
        statement1.setStatementId(UUID.randomUUID());

        Statement statement2 = new Statement();
        statement2.setStatementId(UUID.randomUUID());

        List<Statement> statements = List.of(statement1, statement2);

        StatementDto dto1 = new StatementDto(
                statement1.getStatementId(),
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );

        StatementDto dto2 = new StatementDto(
                statement2.getStatementId(),
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );

        when(repository.findAll()).thenReturn(statements);

        when(statementMapper.toDto(statement1)).thenReturn(dto1);
        when(statementMapper.toDto(statement2)).thenReturn(dto2);

        List<StatementDto> result = service.getAllStatements();

        assertThat(result)
                .hasSize(2)
                .containsExactly(dto1, dto2);

        verify(repository).findAll();
        verify(statementMapper).toDto(statement1);
        verify(statementMapper).toDto(statement2);
    }
}
