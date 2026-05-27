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
import ru.rogotovsky.dossier.util.EmailTemplates;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private EmailTemplates emailTemplates;

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

        String expected = "Ваша заявка предварительно одобрена, завершите оформление";

        when(emailTemplates.finishRegistration()).thenReturn(expected);

        emailService.sendFinishRegistration(message);

        verify(mailSender).send(captor.capture());

        String actual = captor.getValue().getText();

        assertEquals(expected, actual);
    }

    @Test
    void sendCreateDocumentsShouldSendCorrectText() {
        UUID id = UUID.randomUUID();
        EmailMessage message = new EmailMessage("test@mail.com", Theme.CREATE_DOCUMENTS, id, "");

        String expected = """
                Кредит одобрен.
                
                Для продолжения сформируйте документы:
                http://localhost:8082/deal/document/%s/send
                """.formatted(id);

        when(emailTemplates.createDocuments(id)).thenReturn(expected);

        emailService.sendCreateDocuments(message);

        verify(mailSender).send(captor.capture());

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

        String expected = "Ваша заявка на кредит отклонена";

        when(emailTemplates.statementDenied()).thenReturn(expected);

        emailService.sendStatementDenied(message);

        verify(mailSender).send(captor.capture());

        String actual = captor.getValue().getText();

        assertEquals(expected, actual);
    }

    @Test
    void sendDocumentsShouldSendCorrectText() {
        UUID id = UUID.randomUUID();
        EmailMessage message = new EmailMessage("test@mail.com", Theme.SEND_DOCUMENTS, id, "");

        String expected = """
                Ваши документы готовы.
                
                Для подписания перейдите по ссылке:
                http://localhost:8082/deal/document/%s/sign
                """.formatted(id);

        when(emailTemplates.sendDocuments(id)).thenReturn(expected);

        emailService.sendDocuments(message);

        verify(mailSender).send(captor.capture());

        String actual = captor.getValue().getText();

        assertEquals(expected, actual);
    }

    @Test
    void sendSesShouldSendCorrectText() {
        UUID id = UUID.randomUUID();
        String code = "123456";

        EmailMessage message = new EmailMessage("test@mail.com", Theme.SEND_SES, id, code);

        String expected = """
                Вы подтвердили согласие с условиями.
                
                Ваш код подтверждения: %s
                
                Для завершения отправьте код:
                http://localhost:8082/deal/document/%s/code
                """.formatted(code, id);

        when(emailTemplates.sendSes(code, id)).thenReturn(expected);

        emailService.sendSes(message);

        verify(mailSender).send(captor.capture());

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

        String expected = "Кредит успешно выдан. Поздравляем!";

        when(emailTemplates.creditIssued()).thenReturn(expected);

        emailService.sendCreditIssued(message);

        verify(mailSender).send(captor.capture());

        String actual = captor.getValue().getText();

        assertEquals(expected, actual);
    }
}
