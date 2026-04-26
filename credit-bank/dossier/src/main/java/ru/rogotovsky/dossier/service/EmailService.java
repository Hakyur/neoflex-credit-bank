package ru.rogotovsky.dossier.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ru.rogotovsky.dossier.dto.EmailMessage;
import ru.rogotovsky.dossier.util.EmailTemplates;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendFinishRegistration(EmailMessage message) {
        sendEmail(message, EmailTemplates.finishRegistration());
    }

    public void sendCreateDocuments(EmailMessage message) {
        sendEmail(message, EmailTemplates.createDocuments(message.statementId()));
    }

    public void sendStatementDenied(EmailMessage message) {
        sendEmail(message, EmailTemplates.statementDenied());
    }

    public void sendDocuments(EmailMessage message) {
        sendEmail(message, EmailTemplates.sendDocuments(message.statementId()));
    }

    public void sendSes(EmailMessage message) {
        sendEmail(message, EmailTemplates.sendSes(message.text(), message.statementId()));
    }

    public void sendCreditIssued(EmailMessage message) {
        sendEmail(message, EmailTemplates.creditIssued());
    }

    private void sendEmail(EmailMessage emailMessage, String text) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(emailMessage.address());
        message.setSubject(emailMessage.theme().getSubject());
        message.setText(text);

        mailSender.send(message);

        log.info("Email sent to {}", emailMessage.address());
    }
}
