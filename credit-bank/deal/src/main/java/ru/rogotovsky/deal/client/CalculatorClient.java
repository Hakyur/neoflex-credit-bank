package ru.rogotovsky.deal.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import ru.rogotovsky.deal.dto.*;
import ru.rogotovsky.deal.entity.Statement;
import ru.rogotovsky.deal.exception.CalculatorServiceException;
import ru.rogotovsky.deal.exception.ScoringException;
import ru.rogotovsky.deal.service.StatementService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CalculatorClient {

    private final RestClient restClient;
    private final ObjectMapper objectMapper;
    private final StatementService statementService;

    public List<LoanOfferDto> getOffers(LoanStatementRequestDto requestDto) {
        try {
            return restClient.post()
                    .uri("/offers")
                    .body(requestDto)
                    .retrieve()
                    .onStatus(HttpStatusCode::is5xxServerError, (req, res) -> {
                        throw new CalculatorServiceException(readErrorResponse(res));
                    })
                    .body(new ParameterizedTypeReference<>() {});
        } catch (CalculatorServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new CalculatorServiceException("Calculator service unavailable");
        }
    }

    public CreditDto calculate(ScoringDataDto requestDto, Statement statement) {
        try {
            return restClient.post()
                    .uri("/calc")
                    .body(requestDto)
                    .retrieve()
                    .onStatus(status -> status.value() == 400, (req, res) -> {
                        statementService.updateStatusToDenied(statement);
                        throw new ScoringException(readErrorResponse(res));
                    })
                    .onStatus(HttpStatusCode::is5xxServerError, (req, res) -> {
                        throw new CalculatorServiceException(readErrorResponse(res));
                    })
                    .body(CreditDto.class);
        } catch (ScoringException | CalculatorServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new CalculatorServiceException("Calculator service unavailable");
        }
    }

    private ErrorResponse readErrorResponse(ClientHttpResponse res) {
        try {
            return objectMapper.readValue(res.getBody(), ErrorResponse.class);
        } catch (IOException e) {
            return new ErrorResponse(
                    "Cannot parse error response",
                    "PARSE_ERROR",
                    LocalDateTime.now()
            );
        }
    }
}
