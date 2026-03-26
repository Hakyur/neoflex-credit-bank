package ru.rogotovsky.deal.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rogotovsky.deal.client.CalculatorClient;
import ru.rogotovsky.deal.dto.*;
import ru.rogotovsky.deal.entity.Client;
import ru.rogotovsky.deal.entity.Credit;
import ru.rogotovsky.deal.entity.Statement;
import ru.rogotovsky.deal.enums.ApplicationStatus;
import ru.rogotovsky.deal.enums.ChangeType;
import ru.rogotovsky.deal.mapper.ClientMapper;
import ru.rogotovsky.deal.mapper.CreditMapper;
import ru.rogotovsky.deal.mapper.ScoringMapper;
import ru.rogotovsky.deal.repository.ClientRepository;
import ru.rogotovsky.deal.repository.CreditRepository;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DealService {

    private final CalculatorClient calculatorClient;
    private final StatementService statementService;
    private final ClientRepository clientRepository;
    private final CreditRepository creditRepository;
    private final ScoringMapper scoringMapper;
    private final CreditMapper creditMapper;
    private final ClientMapper clientMapper;

    @Transactional
    public List<LoanOfferDto> getLoanOffers(LoanStatementRequestDto requestDto) {
        Client client = clientRepository.save(clientMapper.toClient(requestDto));
        Statement statement = statementService.save(statementService.createStatement(client));

        List<LoanOfferDto> offers = calculatorClient.getOffers(requestDto);

        offers.forEach(offer -> offer.setStatementId(statement.getStatementId()));

        return offers;
    }

    @Transactional
    public void applyLoanOffer(LoanOfferDto requestDto) {
        Statement statement = statementService.getById(requestDto.getStatementId());

        statement.setAppliedOffer(requestDto);
        statement = statementService.updateStatus(statement, ApplicationStatus.APPROVED, ChangeType.MANUAL);

        statementService.save(statement);
    }

    @Transactional
    public void calculateCredit(FinishRegistrationRequestDto requestDto, UUID statementId) {
        Statement statement = statementService.getById(statementId);

        ScoringDataDto scoringDto = scoringMapper.toScoringDataDto(statement, requestDto);

        CreditDto creditDto = calculatorClient.calculate(scoringDto, statement);

        Credit credit = creditRepository.save(creditMapper.toEntity(creditDto));

        statement.setCredit(credit);
        statement = statementService.updateStatus(statement, ApplicationStatus.CC_APPROVED, ChangeType.MANUAL);

        statementService.save(statement);
    }
}
