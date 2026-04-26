package ru.rogotovsky.deal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.rogotovsky.deal.entity.Credit;
import ru.rogotovsky.deal.entity.Statement;
import ru.rogotovsky.deal.exception.InvalidSesCodeException;
import ru.rogotovsky.deal.service.DealDocumentsService;
import ru.rogotovsky.deal.service.EmailEventProducer;
import ru.rogotovsky.deal.service.StatementService;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DealDocumentsServiceTest {

    @Mock
    private StatementService statementService;

    @Mock
    private EmailEventProducer emailEventProducer;

    private DealDocumentsService dealDocumentsService;

    @BeforeEach
    public void setUp() {
        dealDocumentsService = new DealDocumentsService(statementService, emailEventProducer);
    }

    @Test
    void sendDocumentsSuccess() {
        UUID id = UUID.randomUUID();
        Statement statement = new Statement();

        when(statementService.getById(id)).thenReturn(statement);
        when(statementService.updateStatus(any(), any(), any())).thenReturn(statement);
        when(statementService.save(any())).thenReturn(statement);

        dealDocumentsService.sendDocuments(id);

        verify(statementService).getById(id);
        verify(statementService, times(2)).updateStatus(eq(statement), any(), any());
        verify(statementService, times(2)).save(statement);
        verify(emailEventProducer).sendDocumentsEmail(statement);
    }

    @Test
    void processSigningDecisionAccepted() {
        UUID id = UUID.randomUUID();
        Statement statement = new Statement();

        when(statementService.getById(id)).thenReturn(statement);
        when(statementService.save(any())).thenReturn(statement);

        dealDocumentsService.processSigningDecision(id, true);

        verify(statementService).getById(id);
        verify(statementService).save(statement);
        verify(emailEventProducer).sendSesEmail(statement);
        assertNotNull(statement.getSesCode());
    }

    @Test
    void processSigningDecisionRejected() {
        UUID id = UUID.randomUUID();
        Statement statement = new Statement();

        when(statementService.getById(id)).thenReturn(statement);
        when(statementService.updateStatus(any(), any(), any())).thenReturn(statement);
        when(statementService.save(any())).thenReturn(statement);

        dealDocumentsService.processSigningDecision(id, false);

        verify(statementService).getById(id);
        verify(statementService).updateStatus(eq(statement), any(), any());
        verify(statementService).save(statement);
        verify(emailEventProducer, never()).sendSesEmail(any());
    }

    @Test
    void confirmSesCodeSuccess() {
        UUID id = UUID.randomUUID();

        Statement statement = new Statement();
        statement.setSesCode("123456");
        statement.setCredit(new Credit());

        when(statementService.getById(id)).thenReturn(statement);
        when(statementService.updateStatus(any(), any(), any())).thenReturn(statement);
        when(statementService.save(any())).thenReturn(statement);

        dealDocumentsService.confirmSesCode(id, "123456");

        verify(statementService, times(2)).updateStatus(eq(statement), any(), any());
        verify(statementService, times(2)).save(statement);
        verify(emailEventProducer).sendCreditIssuedEmail(statement);
    }

    @Test
    void confirmSesCodeInvalidCode() {
        UUID id = UUID.randomUUID();

        Statement statement = new Statement();
        statement.setSesCode("111111");

        when(statementService.getById(id)).thenReturn(statement);

        assertThrows(InvalidSesCodeException.class,
                () -> dealDocumentsService.confirmSesCode(id, "000000"));

        verify(emailEventProducer, never()).sendCreditIssuedEmail(any());
    }
}
