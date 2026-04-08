package ru.rogotovsky.statement.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import ru.rogotovsky.statement.dto.ErrorResponse;
import ru.rogotovsky.statement.dto.LoanOfferDto;
import ru.rogotovsky.statement.dto.LoanStatementRequestDto;
import ru.rogotovsky.statement.exception.DealServiceException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static ru.rogotovsky.statement.util.DealClientConstants.SELECT_OFFER_URI;
import static ru.rogotovsky.statement.util.DealClientConstants.STATEMENT_URI;
import static ru.rogotovsky.statement.util.ExceptionMessages.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class DealClient {

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public List<LoanOfferDto> requestLoanOffers(LoanStatementRequestDto requestDto) {
        log.debug("Sending POST /deal/statement request: {}", requestDto);

        try {
            List<LoanOfferDto> response = restClient.post()
                    .uri(STATEMENT_URI)
                    .body(requestDto)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, (req, res) -> {
                        log.error("Deal service returned error status: {}", res.getStatusCode());
                        throw new DealServiceException(
                                HttpStatus.valueOf(res.getStatusCode().value()),
                                readErrorResponse(res));
                    })
                    .body(new ParameterizedTypeReference<>() {});
            log.debug("Received response from deal service: {}", response);
            return response;
        } catch (DealServiceException e) {
            log.error("Deal service exception: status={}, error={}", e.getStatus(), e.getError());
            throw e;
        } catch (Exception e) {
            log.error("Deal service unavailable", e);
            throw new DealServiceException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "Deal service unavailable");
        }
    }

    public void requestOfferSelection(LoanOfferDto requestDto) {
        log.debug("Sending POST /deal/offer/select request: {}", requestDto);

        try {
            restClient.post()
                    .uri(SELECT_OFFER_URI)
                    .body(requestDto)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, (req, res) -> {
                        log.error("Deal service returned error status: {}", res.getStatusCode());
                        throw new DealServiceException(
                                HttpStatus.valueOf(res.getStatusCode().value()),
                                readErrorResponse(res));
                    })
                    .toBodilessEntity();
            log.debug("Deal service successfully processed offer selection");
        } catch (DealServiceException e) {
            log.error("Deal service exception: status={}, error={}", e.getStatus(), e.getError());
            throw e;
        } catch (Exception e) {
            log.error("Deal service unavailable", e);
            throw new DealServiceException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    SERVICE_UNAVAILABLE);
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
