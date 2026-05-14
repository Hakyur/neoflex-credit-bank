package ru.rogotovsky.gateway.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.rogotovsky.gateway.client.DealClient;
import ru.rogotovsky.gateway.client.StatementClient;
import ru.rogotovsky.gateway.dto.FinishRegistrationRequestDto;
import ru.rogotovsky.gateway.dto.LoanOfferDto;
import ru.rogotovsky.gateway.dto.LoanStatementRequestDto;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class GatewayService {

    private final StatementClient statementClient;
    private final DealClient dealClient;

    public List<LoanOfferDto> applyForLoan(LoanStatementRequestDto requestDto) {
        log.debug("Processing loan application request");

        List<LoanOfferDto> offers = statementClient.requestLoanOffers(requestDto);

        log.debug("Loan application processed successfully, received {} offers", offers.size());
        return offers;
    }

    public void selectOffer(LoanOfferDto requestDto) {
        log.debug("Processing loan offer selection, statementId={}", requestDto.statementId());

        statementClient.requestOfferSelection(requestDto);

        log.debug("Loan offer selected successfully, statementId={}", requestDto.statementId());
    }

    public void calculateCredit(FinishRegistrationRequestDto requestDto, UUID statementId) {
        log.debug("Processing credit calculation, statementId={}", statementId);

        dealClient.requestCalculateCredit(requestDto, statementId);

        log.debug("Credit calculation completed, statementId={}", statementId);
    }

    public void sendDocuments(UUID statementId) {
        log.debug("Sending documents request, statementId={}", statementId);

        dealClient.requestSendDocuments(statementId);

        log.debug("Documents request completed, statementId={}", statementId);
    }

    public void signDocuments(UUID statementId, Boolean accepted) {
        log.debug("Processing document signing decision, statementId={}, accepted={}", statementId, accepted);

        dealClient.requestSignDocuments(statementId, accepted);

        log.debug("Document signing decision processed, statementId={}", statementId);
    }

    public void verifySesCode(UUID statementId, String code) {
        log.debug("Verifying SES code, statementId={}", statementId);

        dealClient.requestVerifySesCode(statementId, code);

        log.debug("SES code verification completed, statementId={}", statementId);
    }
}
