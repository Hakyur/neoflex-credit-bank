package ru.rogotovsky.deal.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.rogotovsky.deal.dto.EmailMessage;
import ru.rogotovsky.deal.entity.Statement;

import static ru.rogotovsky.deal.util.KafkaTopics.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailEventProducer {

    private final KafkaTemplate<String, EmailMessage> kafkaTemplate;
    private final EmailMessageFactory emailMessageFactory;

    public void sendFinishRegistration(Statement statement) {
        sendMessage(FINISH_REGISTRATION, emailMessageFactory.buildFinishRegistrationEmail(statement));
    }

    public void sendCreateDocuments(Statement statement) {
        sendMessage(CREATE_DOCUMENTS, emailMessageFactory.buildCreateDocumentsEmail(statement));
    }

    public void sendStatementDenied(Statement statement) {
        sendMessage(STATEMENT_DENIED, emailMessageFactory.buildStatementDeniedEmail(statement));
    }

    public void sendDocumentsEmail(Statement statement) {
        sendMessage(SEND_DOCUMENTS, emailMessageFactory.buildSendDocumentsEmail(statement));
    }


    private void sendMessage(String topic, EmailMessage message) {
        log.info("Sending message to topic {}: {}", topic, message);
        kafkaTemplate.send(topic, message);
    }
}
