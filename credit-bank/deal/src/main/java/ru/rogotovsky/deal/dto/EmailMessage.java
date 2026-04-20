package ru.rogotovsky.deal.dto;

import ru.rogotovsky.deal.enums.Theme;

import java.util.UUID;

public record EmailMessage(
        String address,
        Theme theme,
        UUID statementId,
        String text
) {}
