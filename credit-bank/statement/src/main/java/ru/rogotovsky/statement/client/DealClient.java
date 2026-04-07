package ru.rogotovsky.statement.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import ru.rogotovsky.statement.exception.DealServiceException;
import ru.rogotovsky.statement.dto.ErrorResponse;
import ru.rogotovsky.statement.dto.LoanOfferDto;
import ru.rogotovsky.statement.dto.LoanStatementRequestDto;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DealClient {

    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public List<LoanOfferDto> requestLoanOffers(LoanStatementRequestDto requestDto) {
        try {
            return restClient.post()
                    .uri("/statement")
                    .body(requestDto)
                    .retrieve()
                    .onStatus(HttpStatusCode::is5xxServerError, (req, res) -> {
                        throw new DealServiceException(readErrorResponse(res));
                    })
                    .body(new ParameterizedTypeReference<>() {});
        } catch (DealServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new DealServiceException("Deal service unavailable");
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
