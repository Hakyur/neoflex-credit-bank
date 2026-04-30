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
    private final EmailTemplates emailTemplates;

    public void sendFinishRegistration(EmailMessage message) {
        sendEmail(message, emailTemplates.finishRegistration());
    }

    public void sendCreateDocuments(EmailMessage message) {
        sendEmail(message, emailTemplates.createDocuments(message.statementId()));
    }

    public void sendStatementDenied(EmailMessage message) {
        sendEmail(message, emailTemplates.statementDenied());
    }

    public void sendDocuments(EmailMessage message) {
        sendEmail(message, emailTemplates.sendDocuments(message.statementId()));
    }

    public void sendSes(EmailMessage message) {
        sendEmail(message, emailTemplates.sendSes(message.text(), message.statementId()));
    }

    public void sendCreditIssued(EmailMessage message) {
        sendEmail(message, emailTemplates.creditIssued());
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
