package ru.rogotovsky.dossier.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class EmailTemplates {

    @Value("${deal.document.base-url}")
    private String dealBaseUrl;

    public String finishRegistration() {
        return "Ваша заявка предварительно одобрена, завершите оформление";
    }

    public String createDocuments(UUID id) {
        return """
                Кредит одобрен.
                
                Для продолжения сформируйте документы:
                %s/%s/send
                """.formatted(dealBaseUrl, id);
    }

    public String statementDenied() {
        return "Ваша заявка на кредит отклонена";
    }

    public String sendDocuments(UUID id) {
        return """
                Ваши документы готовы.
                
                Для подписания перейдите по ссылке:
                %s/%s/sign
                """.formatted(dealBaseUrl, id);
    }

    public String sendSes(String ses, UUID id) {
        return """
                Вы подтвердили согласие с условиями.
                
                Ваш код подтверждения: %s
                
                Для завершения отправьте код:
                %s/%s/code
                """.formatted(ses, dealBaseUrl, id);
    }

    public String creditIssued() {
        return "Кредит успешно выдан. Поздравляем!";
    }
}
