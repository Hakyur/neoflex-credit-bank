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
import ru.rogotovsky.deal.exception.InvalidSesCodeException;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.UUID;

import static ru.rogotovsky.deal.util.ExceptionMessages.INVALID_SES_CODE;

@Service
@RequiredArgsConstructor
@Slf4j
public class DealDocumentsService {

    private final StatementService statementService;
    private final EmailEventProducer emailEventProducer;

    @Transactional
    public void sendDocuments(UUID statementId) {
        log.debug("Start sendDocuments statementId={}", statementId);

        Statement statement = statementService.getById(statementId);

        statement = statementService.updateStatus(statement, ApplicationStatus.PREPARE_DOCUMENTS, ChangeType.AUTOMATIC);
        statement = statementService.save(statement);

        statement = statementService.updateStatus(statement, ApplicationStatus.DOCUMENT_CREATED, ChangeType.AUTOMATIC);
        statement = statementService.save(statement);

        emailEventProducer.sendDocumentsEmail(statement);

        log.debug("Documents email sent statementId={}", statementId);
    }

    @Transactional
    public void processSigningDecision(UUID statementId, Boolean accepted) {
        log.debug("Start processSigningDecision statementId={}, accepted={}", statementId, accepted);

        Statement statement = statementService.getById(statementId);

        if (!accepted) {
            log.debug("Client rejected conditions statementId={}", statementId);

            statement = statementService.updateStatus(statement, ApplicationStatus.CLIENT_DENIED, ChangeType.AUTOMATIC);
            statementService.save(statement);
            return;
        }

        String sesCode = generateSesCode();
        log.debug("Generated SES code for statementId={}", statementId);

        statement.setSesCode(sesCode);
        statement = statementService.save(statement);

        emailEventProducer.sendSesEmail(statement);

        log.debug("SES email sent statementId={}", statementId);
    }

    @Transactional
    public void confirmSesCode(UUID statementId, String code) {
        log.debug("Start confirmSesCode statementId={}", statementId);

        Statement statement = statementService.getById(statementId);

        if (!code.equals(statement.getSesCode())) {
            log.warn("Invalid SES code statementId={}", statementId);
            throw new InvalidSesCodeException(INVALID_SES_CODE);
        }

        statement = statementService.updateStatus(statement, ApplicationStatus.DOCUMENT_SIGNED, ChangeType.AUTOMATIC);
        statement.setSignDate(LocalDateTime.now());
        statement = statementService.save(statement);

        log.debug("Document signed statementId={}", statementId);

        Credit credit = statement.getCredit();
        credit.setCreditStatus(CreditStatus.ISSUED);

        statement = statementService.updateStatus(statement, ApplicationStatus.CREDIT_ISSUED, ChangeType.AUTOMATIC);
        statement = statementService.save(statement);

        log.info("Credit issued statementId={}", statementId);

        emailEventProducer.sendCreditIssuedEmail(statement);
    }

    private String generateSesCode() {
        SecureRandom random = new SecureRandom();
        return String.format("%06d", random.nextInt(1_000_000));
    }
}
