package ru.rogotovsky.deal.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.rogotovsky.deal.dto.EmailMessage;
import ru.rogotovsky.deal.util.KafkaTopics;

import static ru.rogotovsky.deal.util.KafkaTopics.FINISH_REGISTRATION;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailEventProducer {

    private final KafkaTemplate<String, EmailMessage> kafkaTemplate;

    public void sendFinishRegistration(EmailMessage message) {
        log.info("Sending message to topic finish-registration: {}", message);
        kafkaTemplate.send(FINISH_REGISTRATION, message);
    }
}
