package ru.rogotovsky.dossier.dto;

import ru.rogotovsky.dossier.enums.Theme;

import java.util.UUID;

public record EmailMessage(
        String address,
        Theme theme,
        UUID statementId,
        String text
) {}
