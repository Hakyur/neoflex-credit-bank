package ru.rogotovsky.deal.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import ru.rogotovsky.deal.dto.CreditDto;
import ru.rogotovsky.deal.dto.ErrorResponse;
import ru.rogotovsky.deal.dto.LoanOfferDto;
import ru.rogotovsky.deal.dto.LoanStatementRequestDto;
import ru.rogotovsky.deal.dto.ScoringDataDto;
import ru.rogotovsky.deal.entity.Statement;
import ru.rogotovsky.deal.exception.CalculatorServiceException;
import ru.rogotovsky.deal.exception.ScoringException;
import ru.rogotovsky.deal.service.StatementService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static ru.rogotovsky.deal.util.CalculatorClientConstants.*;
import static ru.rogotovsky.deal.util.ExceptionMessages.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class CalculatorClient {

    private final RestClient restClient;
    private final ObjectMapper objectMapper;
    private final StatementService statementService;

    public List<LoanOfferDto> requestLoanOffers(LoanStatementRequestDto requestDto) {
        log.debug("Sending request to calculator /offers");

        try {
            return restClient.post()
                    .uri(OFFERS_URI)
                    .body(requestDto)
                    .retrieve()
                    .onStatus(HttpStatusCode::is5xxServerError, (req, res) -> {
                        throw new CalculatorServiceException(readErrorResponse(res));
                    })
                    .body(new ParameterizedTypeReference<>() {});
        } catch (CalculatorServiceException e) {
            log.error("Calculator service unavailable", e);
            throw e;
        } catch (Exception e) {
            log.error("Calculator service unavailable", e);
            throw new CalculatorServiceException(SERVICE_UNAVAILABLE);
        }
    }

    public CreditDto calculate(ScoringDataDto requestDto, Statement statement) {
        log.debug("Sending request to calculator /calc for statementId={}", statement.getStatementId());

        try {
            return restClient.post()
                    .uri(CALC_URI)
                    .body(requestDto)
                    .retrieve()
                    .onStatus(status -> status.value() == BAD_REQUEST, (req, res) -> {
                        statementService.updateStatusToDenied(statement.getStatementId());
                        throw new ScoringException(readErrorResponse(res));
                    })
                    .onStatus(HttpStatusCode::is5xxServerError, (req, res) -> {
                        throw new CalculatorServiceException(readErrorResponse(res));
                    })
                    .body(CreditDto.class);
        } catch (ScoringException | CalculatorServiceException e) {
            log.warn("Scoring failed for statementId={}", statement.getStatementId());
            throw e;
        } catch (Exception e) {
            log.error("Calculator service unavailable", e);
            throw new CalculatorServiceException(SERVICE_UNAVAILABLE);
        }
    }

    private ErrorResponse readErrorResponse(ClientHttpResponse res) {
        try {
            return objectMapper.readValue(res.getBody(), ErrorResponse.class);
        } catch (IOException e) {
            return new ErrorResponse(
                    PARSE_ERROR_MESSAGE,
                    PARSE_ERROR,
                    LocalDateTime.now()
            );
        }
    }
}
