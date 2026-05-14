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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.rogotovsky.deal.dto.ErrorResponse;
import ru.rogotovsky.deal.dto.StatementDto;
import ru.rogotovsky.deal.service.StatementService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/deal/admin")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Deal admin controller", description = "Administrative API for viewing loan statements")
public class DealAdminController {

    private final StatementService statementService;

    @Operation(
            summary = "Get statement by id",
            description = "Returns full information about a statement"
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
            )
    })
    @GetMapping("/statement/{statementId}")
    public ResponseEntity<StatementDto> getStatement(@PathVariable UUID statementId) {
        log.info("Received /admin/statement/{statementId}: {}", statementId);

        StatementDto response = statementService.getStatementById(statementId);

        log.info("Returned statement id={}, status={}", response.statementId(), response.status());
        return ResponseEntity.ok(response);
    }


    @Operation(
            summary = "Get all statements",
            description = "Returns all loan statements"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Statements successfully returned",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = StatementDto.class)
                    )
            )
    })
    @GetMapping("/statement")
    public ResponseEntity<List<StatementDto>> getAllStatements() {
        log.info("Received /admin/statement");

        List<StatementDto> response = statementService.getAllStatements();

        log.info("Returned {} statements", response.size());
        return ResponseEntity.ok(response);
    }
}
