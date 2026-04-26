package ru.rogotovsky.deal.service;

import org.springframework.stereotype.Component;
import ru.rogotovsky.deal.dto.EmailMessage;
import ru.rogotovsky.deal.entity.Statement;
import ru.rogotovsky.deal.enums.Theme;

@Component
public class EmailMessageFactory {

    public EmailMessage buildFinishRegistrationEmail(Statement statement) {
        return build(statement, Theme.FINISH_REGISTRATION, "");
    }

    public EmailMessage buildCreateDocumentsEmail(Statement statement) {
        return build(statement, Theme.CREATE_DOCUMENTS, "");
    }

    public EmailMessage buildStatementDeniedEmail(Statement statement) {
        return build(statement, Theme.STATEMENT_DENIED, "");
    }

    public EmailMessage buildSendDocumentsEmail(Statement statement) {
        return build(statement, Theme.SEND_DOCUMENTS, "");
    }

    public EmailMessage buildSendSesEmail(Statement statement) {
        return build(statement, Theme.SEND_SES, statement.getSesCode());
    }

    public EmailMessage buildCreditIssuedEmail(Statement statement) {
        return build(statement, Theme.CREDIT_ISSUED, "");
    }

    private EmailMessage build(Statement statement, Theme theme, String text) {
        return new EmailMessage(
                statement.getClient().getEmail(),
                theme,
                statement.getStatementId(),
                text
        );
    }
}
