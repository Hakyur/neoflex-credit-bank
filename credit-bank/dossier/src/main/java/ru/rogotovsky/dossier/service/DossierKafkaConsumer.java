package ru.rogotovsky.dossier.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.rogotovsky.dossier.dto.EmailMessage;

import static ru.rogotovsky.dossier.util.KafkaTopics.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class DossierKafkaConsumer {

    private final EmailService emailService;

    @KafkaListener(topics = {
            FINISH_REGISTRATION,
            CREATE_DOCUMENTS,
            STATEMENT_DENIED,
            SEND_DOCUMENTS,
            SEND_SES,
            CREDIT_ISSUED
    })
    public void consumeEmailEvent(EmailMessage message) {
        log.info("Received email event with theme={} for {}",
                message.theme(),
                message.address());

        emailService.sendEmail(message);
    }
}
