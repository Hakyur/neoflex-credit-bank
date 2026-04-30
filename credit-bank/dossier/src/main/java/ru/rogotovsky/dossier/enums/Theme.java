package ru.rogotovsky.dossier.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Theme {
    FINISH_REGISTRATION("Предварительное одобрение"),
    CREATE_DOCUMENTS("Кредит одобрен"),
    SEND_DOCUMENTS("Документы для подписания"),
    SEND_SES("Код подтверждения"),
    CREDIT_ISSUED("Кредит выдан"),
    STATEMENT_DENIED("Отказ по заявке");

    private final String subject;
}
