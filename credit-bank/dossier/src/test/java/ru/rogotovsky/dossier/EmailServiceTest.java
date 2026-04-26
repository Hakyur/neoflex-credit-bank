package ru.rogotovsky.dossier;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import ru.rogotovsky.dossier.dto.EmailMessage;
import ru.rogotovsky.dossier.enums.Theme;
import ru.rogotovsky.dossier.service.EmailService;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    @Captor
    private ArgumentCaptor<SimpleMailMessage> captor;

    @Test
    void sendFinishRegistrationShouldSendCorrectText() {
        EmailMessage message = new EmailMessage(
                "test@gmail.com",
                Theme.FINISH_REGISTRATION,
                UUID.randomUUID(),
                ""
        );

        emailService.sendFinishRegistration(message);

        verify(mailSender).send(captor.capture());

        String expected = "Ваша заявка предварительно одобрена, завершите оформление";
        String actual = captor.getValue().getText();

        assertEquals(expected, actual);
    }

    @Test
    void sendCreateDocumentsShouldSendCorrectText() {
        UUID id = UUID.randomUUID();
        EmailMessage message = new EmailMessage("test@mail.com", Theme.CREATE_DOCUMENTS, id, "");

        emailService.sendCreateDocuments(message);

        verify(mailSender).send(captor.capture());

        String expected = """
                Кредит одобрен.
                
                Для продолжения сформируйте документы:
                http://localhost:8082/deal/document/%s/send
                """.formatted(id);
        String actual = captor.getValue().getText();

        assertEquals(expected, actual);
    }

    @Test
    void sendStatementDeniedShouldSendCorrectText() {
        EmailMessage message = new EmailMessage(
                "test@gmail.com",
                Theme.STATEMENT_DENIED,
                UUID.randomUUID(),
                "");

        emailService.sendStatementDenied(message);

        verify(mailSender).send(captor.capture());

        String expected = "Ваша заявка на кредит отклонена";
        String actual = captor.getValue().getText();

        assertEquals(expected, actual);
    }

    @Test
    void sendDocumentsShouldSendCorrectText() {
        UUID id = UUID.randomUUID();
        EmailMessage message = new EmailMessage("test@mail.com", Theme.SEND_DOCUMENTS, id, "");

        emailService.sendDocuments(message);

        verify(mailSender).send(captor.capture());

        String expected = """
                Ваши документы готовы.
                
                Для подписания перейдите по ссылке:
                http://localhost:8082/deal/document/%s/sign
                """.formatted(id);
        String actual = captor.getValue().getText();

        assertEquals(expected, actual);
    }

    @Test
    void sendSesShouldSendCorrectText() {
        UUID id = UUID.randomUUID();
        String code = "123456";

        EmailMessage message = new EmailMessage("test@mail.com", Theme.SEND_SES, id, code);

        emailService.sendSes(message);

        verify(mailSender).send(captor.capture());

        String expected = """
                Вы подтвердили согласие с условиями.
                
                Ваш код подтверждения: %s
                
                Для завершения отправьте код:
                http://localhost:8082/deal/document/%s/code
                """.formatted(code, id);
        String actual = captor.getValue().getText();

        assertEquals(expected, actual);
    }

    @Test
    void sendCreditIssuedShouldSendCorrectText() {
        EmailMessage message = new EmailMessage(
                "test@gmail.com",
                Theme.CREDIT_ISSUED,
                UUID.randomUUID(),
                ""
        );

        emailService.sendCreditIssued(message);

        verify(mailSender).send(captor.capture());

        String expected = "Кредит успешно выдан. Поздравляем!";
        String actual = captor.getValue().getText();

        assertEquals(expected, actual);
    }
}
