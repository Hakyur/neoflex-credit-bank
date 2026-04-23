package ru.rogotovsky.dossier.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.rogotovsky.dossier.dto.EmailMessage;

import static ru.rogotovsky.dossier.util.KafkaTopics.FINISH_REGISTRATION;

@Service
@RequiredArgsConstructor
@Slf4j
public class DossierKafkaConsumer {

    private final EmailService emailService;

    @KafkaListener(topics = FINISH_REGISTRATION)
    public void listenFinishRegistration(EmailMessage message) {
        log.info("Received FINISH_REGISTRATION email: {}", message);
        emailService.sendEmail(message);
    }
}
