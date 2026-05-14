package ru.rogotovsky.gateway.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import ru.rogotovsky.gateway.dto.ErrorResponse;
import ru.rogotovsky.gateway.dto.LoanOfferDto;
import ru.rogotovsky.gateway.dto.LoanStatementRequestDto;
import ru.rogotovsky.gateway.exception.GatewayServiceException;
import ru.rogotovsky.gateway.util.StatementClientConstants;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static ru.rogotovsky.gateway.util.ExceptionMessages.PARSE_ERROR;
import static ru.rogotovsky.gateway.util.ExceptionMessages.PARSE_ERROR_MESSAGE;
import static ru.rogotovsky.gateway.util.ExceptionMessages.STATEMENT_SERVICE_UNAVAILABLE;
import static ru.rogotovsky.gateway.util.StatementClientConstants.SELECT_OFFER_URI;

@Component
@RequiredArgsConstructor
@Slf4j
public class StatementClient {

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public List<LoanOfferDto> requestLoanOffers(LoanStatementRequestDto requestDto) {
        log.debug("Sending POST /statement request: {}", requestDto);

        try {
            List<LoanOfferDto> response = restClient.post()
                    .body(requestDto)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, (req, res) -> {
                        log.error("Statement service returned error status: {}", res.getStatusCode());
                        throw new GatewayServiceException(
                                HttpStatus.valueOf(res.getStatusCode().value()),
                                readErrorResponse(res)
                        );
                    })
                    .body(new ParameterizedTypeReference<>() {});
            log.debug("Received response from statement service: {}", response);
            return response;
        } catch (GatewayServiceException e) {
            log.error("Statement service exception: status={}, error={}", e.getStatus(), e.getError());
            throw e;
        } catch (Exception e) {
            log.error("Statement service unavailable", e);
            throw new GatewayServiceException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    STATEMENT_SERVICE_UNAVAILABLE);
        }
    }

    public void requestOfferSelection(LoanOfferDto requestDto) {
        log.debug("Sending POST /statement/offer request: {}", requestDto);

        try {
            restClient.post()
                    .uri(SELECT_OFFER_URI)
                    .body(requestDto)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, (req, res) -> {
                        log.error("Statement service returned error status: {}", res.getStatusCode());
                        throw new GatewayServiceException(
                                HttpStatus.valueOf(res.getStatusCode().value()),
                                readErrorResponse(res)
                        );
                    })
                    .toBodilessEntity();
            log.debug("Statement service successfully processed offer selection");
        } catch (GatewayServiceException e) {
            log.error("Statement service exception: status={}, error={}", e.getStatus(), e.getError());
            throw e;
        } catch (Exception e) {
            log.error("Statement service unavailable", e);
            throw new GatewayServiceException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    STATEMENT_SERVICE_UNAVAILABLE);
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
