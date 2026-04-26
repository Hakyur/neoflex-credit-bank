package ru.rogotovsky.dossier.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.rogotovsky.dossier.dto.EmailMessage;

import static ru.rogotovsky.dossier.util.KafkaTopics.CREATE_DOCUMENTS;
import static ru.rogotovsky.dossier.util.KafkaTopics.CREDIT_ISSUED;
import static ru.rogotovsky.dossier.util.KafkaTopics.FINISH_REGISTRATION;
import static ru.rogotovsky.dossier.util.KafkaTopics.SEND_DOCUMENTS;
import static ru.rogotovsky.dossier.util.KafkaTopics.SEND_SES;
import static ru.rogotovsky.dossier.util.KafkaTopics.STATEMENT_DENIED;

@Service
@RequiredArgsConstructor
@Slf4j
public class DossierKafkaConsumer {

    private final EmailService emailService;

    @KafkaListener(topics = FINISH_REGISTRATION)
    public void handleFinishRegistration(EmailMessage message) {
        logReceivedEmail(message);
        emailService.sendFinishRegistration(message);
    }

    @KafkaListener(topics = CREATE_DOCUMENTS)
    public void handleCreateDocuments(EmailMessage message) {
        logReceivedEmail(message);
        emailService.sendCreateDocuments(message);
    }

    @KafkaListener(topics = STATEMENT_DENIED)
    public void handleStatementDenied(EmailMessage message) {
        logReceivedEmail(message);
        emailService.sendStatementDenied(message);
    }

    @KafkaListener(topics = SEND_DOCUMENTS)
    public void handleSendDocuments(EmailMessage message) {
        logReceivedEmail(message);
        emailService.sendDocuments(message);
    }

    @KafkaListener(topics = SEND_SES)
    public void handleSendSessions(EmailMessage message) {
        logReceivedEmail(message);
        emailService.sendSes(message);
    }

    @KafkaListener(topics = CREDIT_ISSUED)
    public void handleCreditIssued(EmailMessage message) {
        logReceivedEmail(message);
        emailService.sendCreditIssued(message);
    }

    private void logReceivedEmail(EmailMessage message) {
        log.info("Received email event with theme={} for {}",
                message.theme(),
                message.address());
    }
}
