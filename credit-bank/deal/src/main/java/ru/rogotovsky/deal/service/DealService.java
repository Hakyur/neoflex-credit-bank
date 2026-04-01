package ru.rogotovsky.deal.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.rogotovsky.deal.client.CalculatorClient;
import ru.rogotovsky.deal.dto.*;
import ru.rogotovsky.deal.entity.Client;
import ru.rogotovsky.deal.entity.Credit;
import ru.rogotovsky.deal.entity.Statement;
import ru.rogotovsky.deal.enums.ApplicationStatus;
import ru.rogotovsky.deal.enums.ChangeType;
import ru.rogotovsky.deal.mapper.CreditMapper;
import ru.rogotovsky.deal.mapper.ScoringMapper;
import ru.rogotovsky.deal.repository.CreditRepository;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class DealService {

    private final CalculatorClient calculatorClient;
    private final StatementService statementService;
    private final ClientService clientService;
    private final CreditRepository creditRepository;
    private final ScoringMapper scoringMapper;
    private final CreditMapper creditMapper;

    @Transactional
    public List<LoanOfferDto> getLoanOffers(LoanStatementRequestDto requestDto) {
        log.debug("Saving client information");
        Client client = clientService.saveClient(clientService.createClient(requestDto));

        log.debug("Creating statement for client id={}", client.getClientId());
        Statement statement = statementService.save(statementService.createStatement(client));

        log.debug("Calling calculator for loan offers");
        List<LoanOfferDto> offers = calculatorClient.getOffers(requestDto);

        offers.forEach(offer -> offer.setStatementId(statement.getStatementId()));

        log.info("Generated {} loan offers for statementId={}", offers.size(), statement.getStatementId());
        return offers;
    }

    @Transactional
    public void applyLoanOffer(LoanOfferDto requestDto) {
        log.debug("Fetching statement id={}", requestDto.getStatementId());
        Statement statement = statementService.getById(requestDto.getStatementId());

        statement.setAppliedOffer(requestDto);
        statement = statementService.updateStatus(statement, ApplicationStatus.APPROVED, ChangeType.AUTOMATIC);
        log.debug("Statement status has been changed to {}", statement.getStatus());

        statementService.save(statement);
        log.info("Loan offer applied for statementId={}", statement.getStatementId());
    }

    @Transactional
    public void calculateCredit(FinishRegistrationRequestDto requestDto, UUID statementId) {
        log.debug("Fetching statement id={}", statementId);
        Statement statement = statementService.getById(statementId);

        log.debug("Update client information");
        Client client = clientService.saveClient(
                clientService.updateClientInformation(statement.getClient(), requestDto));

        log.debug("Mapping scoring data");
        ScoringDataDto scoringDto = scoringMapper.toScoringDataDto(statement, requestDto);

        log.debug("Calling calculator for credit calculation");
        CreditDto creditDto = calculatorClient.calculate(scoringDto, statement);

        log.debug("Saving credit entity");
        Credit credit = creditRepository.save(creditMapper.toCredit(creditDto));

        statement.setCredit(credit);
        statement = statementService.updateStatus(statement, ApplicationStatus.CC_APPROVED, ChangeType.AUTOMATIC);
        log.debug("Statement status has been changed to {}", statement.getStatus());

        statementService.save(statement);
        log.info("Credit calculated and saved for statementId={}", statementId);
    }
}
