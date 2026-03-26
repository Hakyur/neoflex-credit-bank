package ru.rogotovsky.deal.client;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import ru.rogotovsky.deal.dto.CreditDto;
import ru.rogotovsky.deal.dto.LoanOfferDto;
import ru.rogotovsky.deal.dto.LoanStatementRequestDto;
import ru.rogotovsky.deal.dto.ScoringDataDto;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CalculatorClient {

    private final RestClient restClient;

    public List<LoanOfferDto> getOffers(LoanStatementRequestDto requestDto) {
        return restClient.post()
                .uri("/offers")
                .body(requestDto)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    public CreditDto calculate(ScoringDataDto requestDto) {
        return restClient.post()
                .uri("/calc")
                .body(requestDto)
                .retrieve()
                .body(CreditDto.class);
    }
}
