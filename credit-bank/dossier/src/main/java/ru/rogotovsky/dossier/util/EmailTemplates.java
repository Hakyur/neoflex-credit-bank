package ru.rogotovsky.dossier.util;

import java.util.UUID;

public final class EmailTemplates {

    public static String finishRegistration() {
        return "Ваша заявка предварительно одобрена, завершите оформление";
    }

    public static String createDocuments(UUID id) {
        return """
                Кредит одобрен.
                
                Для продолжения сформируйте документы:
                http://localhost:8082/deal/document/%s/send
                """.formatted(id);
    }

    public static String statementDenied() {
        return "Ваша заявка на кредит отклонена";
    }

    public static String sendDocuments(UUID id) {
        return """
                Ваши документы готовы.
                
                Для подписания перейдите по ссылке:
                http://localhost:8082/deal/document/%s/sign
                """.formatted(id);
    }

    public static String sendSes(String ses, UUID id) {
        return """
                Вы подтвердили согласие с условиями.
                
                Ваш код подтверждения: %s
                
                Для завершения отправьте код:
                http://localhost:8082/deal/document/%s/code
                """.formatted(ses, id);
    }

    public static String creditIssued() {
        return "Кредит успешно выдан. Поздравляем!";
    }
}
