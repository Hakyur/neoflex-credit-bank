package ru.rogotovsky.gateway.controller;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.rogotovsky.gateway.dto.ErrorResponse;
import ru.rogotovsky.gateway.dto.FinishRegistrationRequestDto;
import ru.rogotovsky.gateway.service.GatewayService;

import java.util.UUID;

@RestController
@RequestMapping("/deal")
@RequiredArgsConstructor
@Slf4j
@Tag(
        name = "Gateway deal controller",
        description = "Gateway API for credit calculation, document processing and SES confirmation"
)
public class GatewayDealController {

    private final GatewayService gatewayService;

    @Operation(
            summary = "Calculate credit",
            description = "Forwards credit calculation request to Deal microservice"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Credit calculated and saved successfully",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid data or scoring failed",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "503",
                    description = "One of the services is unavailable",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @PostMapping("/calculate/{statementId}")
    public ResponseEntity<Void> calculateCredit(
            @RequestBody FinishRegistrationRequestDto requestDto,
            @PathVariable UUID statementId) {

        log.info("Received /deal/calculate/{} request: {}", statementId, requestDto);

        gatewayService.calculateCredit(requestDto, statementId);

        log.info("POST /deal/calculate/{} completed", statementId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Send documents",
            description = "Forwards document generation request to Deal microservice"
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
            ),
            @ApiResponse(
                    responseCode = "503",
                    description = "Deal service is unavailable",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @PostMapping("/document/{statementId}/send")
    public ResponseEntity<Void> sendDocument(@PathVariable UUID statementId) {
        log.info("Received /deal/document/{}/send request", statementId);

        gatewayService.sendDocuments(statementId);

        log.info("POST /deal/document/{}/send completed", statementId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Sign documents",
            description = "Forwards signing decision to Deal microservice"
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
            ),
            @ApiResponse(
                    responseCode = "503",
                    description = "Deal service is unavailable",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @PostMapping("/document/{statementId}/sign")
    public ResponseEntity<Void> signDocument(@PathVariable UUID statementId, @RequestParam Boolean accepted) {
        log.info("Received /deal/document/{}/sign request: accepted={}", statementId, accepted);

        gatewayService.signDocuments(statementId, accepted);

        log.info("POST /deal/document/{}/sign completed", statementId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Verify SES code",
            description = "Forwards SES code verification request to Deal microservice"
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
            ),
            @ApiResponse(
                    responseCode = "503",
                    description = "Deal service is unavailable",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @PostMapping("/document/{statementId}/code")
    public ResponseEntity<Void> verifySesCode(@PathVariable UUID statementId, @RequestBody String code) {
        log.info("Received /deal/document/{}/code request", statementId);

        gatewayService.verifySesCode(statementId, code);

        log.info("POST /deal/document/{}/code completed", statementId);
        return ResponseEntity.noContent().build();
    }
}
