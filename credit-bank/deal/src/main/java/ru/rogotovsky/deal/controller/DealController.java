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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.rogotovsky.deal.dto.ErrorResponse;
import ru.rogotovsky.deal.dto.FinishRegistrationRequestDto;
import ru.rogotovsky.deal.dto.LoanOfferDto;
import ru.rogotovsky.deal.dto.LoanStatementRequestDto;
import ru.rogotovsky.deal.service.DealService;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/deal")
@RequiredArgsConstructor
@Tag(
        name = "Deal controller",
        description = "API for handling loan statements, selected offers, and credit calculation in the Deal microservice"
)
public class DealController {

    private final DealService dealService;

    @Operation(
            summary = "Create loan statement and get offers",
            description = "Receives client loan request data, creates a Statement entity, and fetches 4 loan offers from Calculator microservice"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Loan offers successfully calculated",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = LoanOfferDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @PostMapping("/statement")
    public ResponseEntity<List<LoanOfferDto>> createLoanStatement(@RequestBody LoanStatementRequestDto requestDto) {
        log.info("Received /statement request: {}", requestDto);

        List<LoanOfferDto> response = dealService.createLoanStatement(requestDto);

        log.info("Returning loan offers: {}", response);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Select a loan offer",
            description = "Sets the selected LoanOfferDto as appliedOffer in the Statement and updates its status and history"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Offer successfully applied",
                    content = @Content
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
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @PostMapping("/offer/select")
    public ResponseEntity<Void> selectOffer(@RequestBody LoanOfferDto requestDto) {
        log.info("Received /offer/select request: {}", requestDto);

        dealService.applyLoanOffer(requestDto);

        log.info("POST /offer/select completed");
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Finalize registration and calculate credit",
            description = "Receives full client data, performs scoring via Calculator microservice," +
                    " calculates credit details, and stores Credit entity with status CALCULATED"
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
                    responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @PostMapping("/calculate/{statementId}")
    public ResponseEntity<Void> calculateCredit(@RequestBody FinishRegistrationRequestDto requestDto, @PathVariable UUID statementId) {
        log.info("Received /calculate/{statementId}: {}", requestDto);

        dealService.calculateCredit(requestDto, statementId);

        log.info("POST /deal/calculate/{} completed", statementId);
        return ResponseEntity.noContent().build();
    }
}
