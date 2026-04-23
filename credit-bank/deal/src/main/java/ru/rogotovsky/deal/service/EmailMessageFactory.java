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
}
