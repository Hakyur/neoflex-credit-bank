package ru.rogotovsky.deal.service;

import org.springframework.stereotype.Component;
import ru.rogotovsky.deal.dto.EmailMessage;
import ru.rogotovsky.deal.entity.Statement;
import ru.rogotovsky.deal.enums.Theme;

@Component
public class EmailMessageFactory {

    public EmailMessage buildFinishRegistrationEmail(Statement statement) {
        return new EmailMessage(
                statement.getClient().getEmail(),
                Theme.FINISH_REGISTRATION,
                statement.getStatementId(),
                "Ваша заявка предварительно одобрена, завершите оформление"
        );
    }

    public EmailMessage buildCreateDocumentsEmail(Statement statement) {
        String text = """
            Кредит одобрен.
            
            Для продолжения сформируйте документы:
            http://localhost:8082/deal/document/%s/send
            """.formatted(statement.getStatementId());

        return new EmailMessage(
                statement.getClient().getEmail(),
                Theme.CREATE_DOCUMENTS,
                statement.getStatementId(),
                text
        );
    }

    public EmailMessage buildStatementDeniedEmail(Statement statement) {
        return new EmailMessage(
                statement.getClient().getEmail(),
                Theme.STATEMENT_DENIED,
                statement.getStatementId(),
                "Ваша заявка на кредит отклонена"
        );
    }

    public EmailMessage buildSendDocumentsEmail(Statement statement) {
        String text = """
            Ваши документы готовы.

            Для подписания перейдите по ссылке:
            http://localhost:8082/deal/document/%s/sign
            """.formatted(statement.getStatementId());

        return new EmailMessage(
                statement.getClient().getEmail(),
                Theme.SEND_DOCUMENTS,
                statement.getStatementId(),
                text
        );
    }

    public EmailMessage buildSendSesEmail(Statement statement) {
        String text = """
            Вы подтвердили согласие с условиями.

            Ваш код подтверждения: %s

            Для завершения отправьте код:
            http://localhost:8082/deal/document/%s/code
            """.formatted(
                statement.getSesCode(),
                statement.getStatementId()
        );

        return new EmailMessage(
                statement.getClient().getEmail(),
                Theme.SEND_SES,
                statement.getStatementId(),
                text
        );
    }

    public EmailMessage buildCreditIssuedEmail(Statement statement) {
        return new EmailMessage(
                statement.getClient().getEmail(),
                Theme.CREDIT_ISSUED,
                statement.getStatementId(),
                "Кредит успешно выдан. Поздравляем!"
        );
    }
}
