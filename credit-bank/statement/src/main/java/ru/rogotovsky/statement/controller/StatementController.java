package ru.rogotovsky.statement.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.rogotovsky.statement.dto.ErrorResponse;
import ru.rogotovsky.statement.dto.LoanOfferDto;
import ru.rogotovsky.statement.dto.LoanStatementRequestDto;
import ru.rogotovsky.statement.service.StatementService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/statement")
@RequiredArgsConstructor
@Tag(
        name = "Statement controller",
        description = "API for handling loan application requests and selecting loan offers"
)
public class StatementController {

    private final StatementService statementService;

    @Operation(
            summary = "Apply for a loan and get offers",
            description = "Receives initial client data, performs prescoring, and requests loan offers " +
                    "from Deal microservice. Returns a list of available loan offers"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Loan offers successfully received",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LoanOfferDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation error in request data",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "503",
                    description = "Deal service unavailable",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @PostMapping
    public ResponseEntity<List<LoanOfferDto>> applyForLoan(@Valid @RequestBody LoanStatementRequestDto requestDto) {
        log.info("Received /statement request: {}", requestDto);

        List<LoanOfferDto> offers = statementService.processLoanApplication(requestDto);

        log.info("Returning loan offers: {}", offers);
        return ResponseEntity.ok(offers);
    }

    @Operation(
            summary = "Select a loan offer",
            description = "Sends selected LoanOfferDto to Deal microservice to apply it to the statement"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Offer successfully selected",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request data",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
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
                    description = "Deal service unavailable",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @PostMapping("/offer")
    public ResponseEntity<Void> selectOffer(@RequestBody LoanOfferDto requestDto) {
        log.info("Received /statement/offer request: {}", requestDto);

        statementService.selectLoanOffer(requestDto);

        log.info("POST /statement/offer completed");
        return ResponseEntity.noContent().build();
    }
}
