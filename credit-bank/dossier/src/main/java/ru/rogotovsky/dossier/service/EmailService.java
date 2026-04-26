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

    public void sendFinishRegistration(EmailMessage message) {
        sendEmail(message, "Ваша заявка предварительно одобрена, завершите оформление");
    }

    public void sendCreateDocuments(EmailMessage message) {
        sendEmail(message, """
            Кредит одобрен.
            
            Для продолжения сформируйте документы:
            http://localhost:8082/deal/document/%s/send
            """.formatted(message.statementId()));
    }

    public void sendStatementDenied(EmailMessage message) {
        sendEmail(message, "Ваша заявка на кредит отклонена");
    }

    public void sendDocuments(EmailMessage message) {
        sendEmail(message, """
            Ваши документы готовы.

            Для подписания перейдите по ссылке:
            http://localhost:8082/deal/document/%s/sign
            """.formatted(message.statementId()));
    }

    public void sendSes(EmailMessage message) {
        sendEmail(message, """
            Вы подтвердили согласие с условиями.

            Ваш код подтверждения: %s

            Для завершения отправьте код:
            http://localhost:8082/deal/document/%s/code
            """.formatted(
                message.text(),
                message.statementId()
        ));
    }

    public void sendCreditIssued(EmailMessage message) {
        sendEmail(message, "Кредит успешно выдан. Поздравляем!");
    }

    private void sendEmail(EmailMessage emailMessage, String text) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(emailMessage.address());
        message.setSubject(emailMessage.theme().getSubject());
        message.setText(emailMessage.text());

        mailSender.send(message);

        log.info("Email sent to {}", emailMessage.address());
    }
}
