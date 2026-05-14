package ru.rogotovsky.gateway.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import ru.rogotovsky.gateway.dto.ErrorResponse;
import ru.rogotovsky.gateway.dto.FinishRegistrationRequestDto;
import ru.rogotovsky.gateway.exception.GatewayServiceException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

import static ru.rogotovsky.gateway.util.DealClientConstants.CALCULATE_CREDIT;
import static ru.rogotovsky.gateway.util.DealClientConstants.SEND_DOCUMENTS;
import static ru.rogotovsky.gateway.util.DealClientConstants.SIGN_DOCUMENTS;
import static ru.rogotovsky.gateway.util.DealClientConstants.VERIFY_CODE;
import static ru.rogotovsky.gateway.util.ExceptionMessages.DEAL_SERVICE_UNAVAILABLE;
import static ru.rogotovsky.gateway.util.ExceptionMessages.PARSE_ERROR;
import static ru.rogotovsky.gateway.util.ExceptionMessages.PARSE_ERROR_MESSAGE;

@Component
@RequiredArgsConstructor
@Slf4j
public class DealClient {

    private final RestClient dealRestClient;
    private final ObjectMapper objectMapper;

    public void requestCalculateCredit
            (FinishRegistrationRequestDto requestDto,
             UUID statementId) {

        log.debug("Sending POST /deal/calculate/{} request: {}", statementId, requestDto);

        try {
            dealRestClient.post()
                    .uri(CALCULATE_CREDIT.formatted(statementId))
                    .body(requestDto)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, (req, res) -> {
                        log.error("Deal service returned error status: {}", res.getStatusCode());
                        throw new GatewayServiceException(
                                HttpStatus.valueOf(res.getStatusCode().value()),
                                readErrorResponse(res)
                        );
                    })
                    .toBodilessEntity();
            log.debug("Deal service successfully processed calculateCredit for statementId={}", statementId);
        } catch (GatewayServiceException e) {
            log.error("Deal service exception: status={}, error={}", e.getStatus(), e.getError());
            throw e;
        } catch (Exception e) {
            log.error("Deal service unavailable", e);
            throw new GatewayServiceException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    DEAL_SERVICE_UNAVAILABLE);
        }
    }

    public void requestSendDocuments(UUID statementId) {
        log.debug("Sending POST /deal/document/{}/send request", statementId);

        try {
            dealRestClient.post()
                    .uri(SEND_DOCUMENTS.formatted(statementId))
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, (req, res) -> {
                        log.error("Deal service returned error status: {}", res.getStatusCode());
                        throw new GatewayServiceException(
                                HttpStatus.valueOf(res.getStatusCode().value()),
                                readErrorResponse(res)
                        );
                    })
                    .toBodilessEntity();
            log.debug("Deal service successfully processed sendDocuments for statementId={}", statementId);
        } catch (GatewayServiceException e) {
            log.error("Deal service exception: status={}, error={}", e.getStatus(), e.getError());
            throw e;
        }  catch (Exception e) {
            log.error("Deal service unavailable", e);
            throw new GatewayServiceException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    DEAL_SERVICE_UNAVAILABLE
            );
        }
    }

    public void requestSignDocuments(UUID statementId, Boolean accepted) {
        log.debug("Sending POST /deal/document/{}/sign request, accepted={}", statementId, accepted);

        try {
            dealRestClient.post()
                    .uri(uriBuilder -> uriBuilder
                            .path(SIGN_DOCUMENTS.formatted(statementId))
                            .queryParam("accepted", accepted)
                            .build())
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, (req, res) -> {
                        log.error("Deal service returned error status: {}", res.getStatusCode());
                        throw new GatewayServiceException(
                                HttpStatus.valueOf(res.getStatusCode().value()),
                                readErrorResponse(res)
                        );
                    })
                    .toBodilessEntity();
            log.debug("Deal service successfully processed signDocuments for statementId={}", statementId);
        } catch (GatewayServiceException e) {
            log.error("Deal service exception: status={}, error={}", e.getStatus(), e.getError());
            throw e;
        }  catch (Exception e) {
            log.error("Deal service unavailable", e);
            throw new GatewayServiceException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    DEAL_SERVICE_UNAVAILABLE
            );
        }
    }

    public void requestVerifySesCode(UUID statementId, String code) {
        log.debug("Sending POST /deal/document/{}/code request, code={}", statementId, code);

        try {
            dealRestClient.post()
                    .uri(uriBuilder -> uriBuilder
                            .path(VERIFY_CODE.formatted(statementId))
                            .queryParam("code", code)
                            .build())
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, (req, res) -> {
                        log.error("Deal service returned error status: {}", res.getStatusCode());
                        throw new GatewayServiceException(
                                HttpStatus.valueOf(res.getStatusCode().value()),
                                readErrorResponse(res)
                        );
                    })
                    .toBodilessEntity();
            log.debug("Deal service successfully verified SES code for statementId={}", statementId);
        } catch (GatewayServiceException e) {
            log.error("Deal service exception: status={}, error={}", e.getStatus(), e.getError());
            throw e;
        }  catch (Exception e) {
            log.error("Deal service unavailable", e);
            throw new GatewayServiceException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    DEAL_SERVICE_UNAVAILABLE
            );
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
