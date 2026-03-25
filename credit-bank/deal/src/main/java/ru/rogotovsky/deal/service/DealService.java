package ru.rogotovsky.deal.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import ru.rogotovsky.deal.dto.LoanOfferDto;
import ru.rogotovsky.deal.dto.LoanStatementRequestDto;
import ru.rogotovsky.deal.entity.Client;
import ru.rogotovsky.deal.entity.Statement;
import ru.rogotovsky.deal.entity.StatusHistory;
import ru.rogotovsky.deal.enums.ApplicationStatus;
import ru.rogotovsky.deal.enums.ChangeType;
import ru.rogotovsky.deal.mapper.ClientMapper;
import ru.rogotovsky.deal.repository.ClientRepository;
import ru.rogotovsky.deal.repository.StatementRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DealService {

    private final StatementRepository statementRepository;
    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;
    private final RestClient restClient;

    @Transactional
    public List<LoanOfferDto> getLoanOffers(LoanStatementRequestDto requestDto) {
        Client client = clientRepository.save(clientMapper.toClient(requestDto));
        Statement statement = statementRepository.save(createStatement(client));

        List<LoanOfferDto> offers = getOffersFromCalculator(requestDto);

        offers.forEach(offer -> offer.setStatementId(statement.getStatementId()));

        return offers;
    }

    private Statement createStatement(Client client) {
        LocalDateTime time = LocalDateTime.now();
        Statement statement = new Statement();
        statement.setClient(client);
        statement.setStatus(ApplicationStatus.PREAPPROVAL);
        statement.setCreationDate(time);
        statement.setStatusHistory(List.of(
                new StatusHistory(ApplicationStatus.PREAPPROVAL, time, ChangeType.AUTOMATIC)
        ));
        return statement;
    }

    private List<LoanOfferDto> getOffersFromCalculator(LoanStatementRequestDto requestDto) {
        return restClient.post()
                .uri("/offers")
                .body(requestDto)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (req, res) -> {
                    throw new RuntimeException("Calculator returned error: " + res.getStatusCode());
                })
                .body(new ParameterizedTypeReference<List<LoanOfferDto>>() {
                });
    }
}
