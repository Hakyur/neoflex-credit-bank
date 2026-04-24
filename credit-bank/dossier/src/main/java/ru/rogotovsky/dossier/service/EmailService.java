package ru.rogotovsky.dossier.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ru.rogotovsky.dossier.dto.EmailMessage;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendEmail(EmailMessage emailMessage) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(emailMessage.address());
        message.setSubject(emailMessage.theme().getSubject());
        message.setText(emailMessage.text());

        mailSender.send(message);

        log.info("Email sent to {}", emailMessage.address());
    }
}
