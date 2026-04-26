package ru.rogotovsky.deal.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.rogotovsky.deal.dto.EmailMessage;
import ru.rogotovsky.deal.entity.Statement;
import ru.rogotovsky.deal.exception.KafkaMessageSendException;

import static ru.rogotovsky.deal.util.KafkaTopics.CREATE_DOCUMENTS;
import static ru.rogotovsky.deal.util.KafkaTopics.CREDIT_ISSUED;
import static ru.rogotovsky.deal.util.KafkaTopics.FINISH_REGISTRATION;
import static ru.rogotovsky.deal.util.KafkaTopics.SEND_DOCUMENTS;
import static ru.rogotovsky.deal.util.KafkaTopics.SEND_SES;
import static ru.rogotovsky.deal.util.KafkaTopics.STATEMENT_DENIED;

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

    public void sendSesEmail(Statement statement) {
        sendMessage(SEND_SES, emailMessageFactory.buildSendSesEmail(statement));
    }

    public void sendCreditIssuedEmail(Statement statement) {
        sendMessage(CREDIT_ISSUED, emailMessageFactory.buildCreditIssuedEmail(statement));
    }

    private void sendMessage(String topic, EmailMessage message) {
        String key = message.statementId().toString();

        try {
            kafkaTemplate.send(topic, key, message).get();
            log.info("Message sent to topic={}, key={}", topic, key);
        } catch (Exception ex) {
            log.error("Failed to send message to topic={}, key={}", topic, key, ex);
            throw new KafkaMessageSendException("Failed to send Kafka message");
        }
    }
}
