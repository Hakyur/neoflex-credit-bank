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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.rogotovsky.gateway.dto.ErrorResponse;
import ru.rogotovsky.gateway.dto.StatementDto;
import ru.rogotovsky.gateway.service.GatewayService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/deal/admin")
@RequiredArgsConstructor
@Slf4j
@Tag(
        name = "Gateway deal admin controller",
        description = "Gateway API for administrative access to loan statements"
)
public class GatewayDealAdminController {

    private final GatewayService gatewayService;

    @Operation(
            summary = "Get statement by id",
            description = "Returns full statement information from Deal service"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Statement successfully returned",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StatementDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Statement not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
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
    @GetMapping("/statement/{statementId}")
    public ResponseEntity<StatementDto> getStatement(@PathVariable UUID statementId) {
        log.info("Received /deal/admin/statement/{} request", statementId);

        StatementDto response = gatewayService.getStatementById(statementId);

        log.info("Returned statement id={}, status={}", response.statementId(), response.status());
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Get all statements",
            description = "Returns all loan statements from Deal service"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Statements successfully returned",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StatementDto.class)
                    )
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
    @GetMapping("/statement")
    public ResponseEntity<List<StatementDto>> getStatements() {
        log.info("Received /deal/admin/statement request");

        List<StatementDto> response = gatewayService.getAllStatements();

        log.info("Returned {} statements", response.size());
        return ResponseEntity.ok(response);
    }
}
