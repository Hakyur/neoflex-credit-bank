package ru.rogotovsky.gateway.controller;

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
import ru.rogotovsky.gateway.dto.ErrorResponse;
import ru.rogotovsky.gateway.dto.LoanOfferDto;
import ru.rogotovsky.gateway.dto.LoanStatementRequestDto;
import ru.rogotovsky.gateway.service.GatewayService;

import java.util.List;

@RestController
@RequestMapping("/statement")
@RequiredArgsConstructor
@Slf4j
@Tag(
        name = "Gateway statement controller",
        description = "Gateway API for loan application and offer selection"
)
public class GatewayStatementController {

    private final GatewayService gatewayService;

    @Operation(
            summary = "Apply for a loan and get offers",
            description = "Receives initial client data and forwards the request " +
                    "to Statement microservice. Returns a list of available loan offers"
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
                    description = "One of the services is unavailable",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @PostMapping
    public ResponseEntity<List<LoanOfferDto>> applyForLoan(@Valid @RequestBody LoanStatementRequestDto requestDto) {
        log.info("Received gateway /statement request: {}", requestDto);

        List<LoanOfferDto> response = gatewayService.applyForLoan(requestDto);

        log.info("Returning {} loan offers", response.size());
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Select a loan offer",
            description = "Forwards selected LoanOfferDto to Statement microservice"
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
                    description = "One of the services is unavailable",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @PostMapping("/offer")
    public ResponseEntity<Void> selectOffer(@RequestBody LoanOfferDto requestDto) {
        log.info("Received gateway /statement/offer request: {}", requestDto);

        gatewayService.selectOffer(requestDto);

        log.info("POST /statement/offer completed");
        return ResponseEntity.noContent().build();
    }
}
