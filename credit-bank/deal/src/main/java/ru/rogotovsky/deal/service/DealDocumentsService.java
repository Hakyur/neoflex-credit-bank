package ru.rogotovsky.deal.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rogotovsky.deal.entity.Credit;
import ru.rogotovsky.deal.entity.Statement;
import ru.rogotovsky.deal.enums.ApplicationStatus;
import ru.rogotovsky.deal.enums.ChangeType;
import ru.rogotovsky.deal.enums.CreditStatus;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class DealDocumentsService {

    private final StatementService statementService;
    private final EmailEventProducer emailEventProducer;

    @Transactional
    public void sendDocuments(UUID statementId) {
        Statement statement = statementService.getById(statementId);

        statement = statementService.updateStatus(statement, ApplicationStatus.PREPARE_DOCUMENTS, ChangeType.AUTOMATIC);
        statement = statementService.save(statement);

        statement = statementService.updateStatus(statement, ApplicationStatus.DOCUMENT_CREATED, ChangeType.AUTOMATIC);
        statement = statementService.save(statement);

        emailEventProducer.sendDocumentsEmail(statement);
    }

    @Transactional
    public void processSigningDecision(UUID statementId, Boolean accepted) {
        Statement statement = statementService.getById(statementId);

        if (!accepted) {
            statement = statementService.updateStatus(statement, ApplicationStatus.CLIENT_DENIED, ChangeType.AUTOMATIC);
            statementService.save(statement);
            return;
        }

        String sesCode = generateSesCode();
        statement.setSesCode(sesCode);
        statement = statementService.save(statement);

        emailEventProducer.sendSesEmail(statement);
    }


    private String generateSesCode() {
        SecureRandom random = new SecureRandom();
        return String.format("%06d", random.nextInt(1_000_000));
    }
}
