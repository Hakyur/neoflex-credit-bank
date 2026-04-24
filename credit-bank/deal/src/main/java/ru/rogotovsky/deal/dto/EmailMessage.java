package ru.rogotovsky.deal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.rogotovsky.deal.enums.Theme;

import java.util.UUID;

@Schema(description = "Message payload for email notification")
public record EmailMessage(

        @Schema(description = "Recipient email address", example = "client@gmail.com")
        String address,

        @Schema(description = "Email notification theme", example = "FINISH_REGISTRATION")
        Theme theme,

        @Schema(description = "Unique identifier of loan statement", example = "4b3d9bc7-d9c2-416f-8158-5c8dbab41747")
        UUID statementId,

        @Schema(description = "Email message body text")
        String text
) {}
