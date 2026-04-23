package ru.rogotovsky.calculator.controller;

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
import ru.rogotovsky.calculator.dto.*;
import ru.rogotovsky.calculator.service.CreditService;
import ru.rogotovsky.calculator.service.LoanOfferService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/calculator")
@RequiredArgsConstructor
@Tag(
        name = "Credit calculator",
        description = "API for loan offer generation and full credit calculation"
)
public class CalculatorController {

    private final LoanOfferService loanOfferService;
    private final CreditService creditService;

    @Operation(
            summary = "Calculate loan offers",
            description = "Generates possible loan offers based on the loan request data. " +
                    "The system returns several offers with different combinations of insurance " +
                    "and salary client options"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Loan offers calculated successfully",
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
    @PostMapping("/offers")
    public ResponseEntity<List<LoanOfferDto>> calculateLoanOffers(@RequestBody LoanStatementRequestDto requestDto) {
        log.info("Received /offers request: {}", requestDto);

        List<LoanOfferDto> offers = loanOfferService.calculateLoanOffers(requestDto);

        log.info("Returning loan offers: {}", offers);
        return ResponseEntity.ok(offers);
    }

    @Operation(
            summary = "Calculate credit conditions",
            description = "Performs full credit calculation using detailed scoring data. " +
                    "The system calculates interest rate, monthly payment, total credit cost (PSK) " +
                    "and generates a full payment schedule."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Credit calculated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CreditDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid scoring data or scoring validation failed",
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
    @PostMapping("/calc")
    public ResponseEntity<CreditDto> calculateCredit(@RequestBody ScoringDataDto requestDto) {
        log.info("Received /calc request: {}", requestDto);

        CreditDto creditDto = creditService.calculateCredit(requestDto);

        log.info("Credit calculation result: {}", creditDto);
        return ResponseEntity.ok(creditDto);
    }
}
