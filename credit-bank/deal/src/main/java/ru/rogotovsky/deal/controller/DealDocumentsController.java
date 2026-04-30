package ru.rogotovsky.deal.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.rogotovsky.deal.dto.ErrorResponse;
import ru.rogotovsky.deal.service.DealDocumentsService;

import java.util.UUID;

@RestController
@RequestMapping("/deal/document")
@RequiredArgsConstructor
@Slf4j
@Tag(
        name = "Deal documents controller",
        description = "API for document generation, signing decision and SES code confirmation"
)
public class DealDocumentsController {

    private final DealDocumentsService dealDocumentsService;

    @Operation(
            summary = "Generate loan documents",
            description = "Changes statement status to document preparation and sends generated " +
                    "documents to the client email"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Documents successfully generated and sent"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Statement not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PostMapping("/{statementId}/send")
    public ResponseEntity<Void> sendDocuments(@PathVariable UUID statementId) {
        log.info("Received /document/{statementId}/send: {}", statementId);

        dealDocumentsService.sendDocuments(statementId);

        log.info("POST /document/{}/select completed", statementId);
        return ResponseEntity.noContent().build();
    }


    @Operation(
            summary = "Process signing decision",
            description = "Processes client decision to accept or reject loan conditions. " +
                    "If accepted, sends SES code to email. If rejected, updates statement status."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Signing decision processed"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Statement not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PostMapping("/{statementId}/sign")
    public ResponseEntity<Void> signDocuments(@PathVariable UUID statementId, @RequestParam Boolean accepted) {
        log.info("Received /document/{statementId}/sign: {}, accepted={}", statementId, accepted);

        dealDocumentsService.processSigningDecision(statementId, accepted);

        log.info("POST /document/{}/sign completed", statementId);
        return ResponseEntity.noContent().build();
    }


    @Operation(
            summary = "Confirm SES code",
            description = "Validates SES code sent by the client and issues the credit if the code is correct"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "SES code verified and credit issued"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid SES code",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Statement not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PostMapping("/{statementId}/code")
    public ResponseEntity<Void> verifySesCode(@PathVariable UUID statementId, @RequestParam String code) {
        log.info("Received /document/{statementId}/code: {}", statementId);

        dealDocumentsService.confirmSesCode(statementId, code);

        log.info("POST /document/{}/code completed", statementId);
        return ResponseEntity.noContent().build();
    }
}
