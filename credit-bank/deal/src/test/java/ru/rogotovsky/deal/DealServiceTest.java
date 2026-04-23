package ru.rogotovsky.deal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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
import ru.rogotovsky.deal.service.ClientService;
import ru.rogotovsky.deal.service.DealService;
import ru.rogotovsky.deal.service.StatementService;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DealServiceTest {

    @Mock
    private CalculatorClient calculatorClient;

    @Mock
    private StatementService statementService;

    @Mock
    private ClientService clientService;

    @Mock
    private CreditRepository creditRepository;

    @Mock
    private ScoringMapper scoringMapper;

    @Mock
    private CreditMapper creditMapper;

    private DealService dealService;

    @BeforeEach
    void setUp() {
        dealService = new DealService(
                calculatorClient, statementService,
                clientService, creditRepository,
                scoringMapper, creditMapper, null, null
        );
    }

    @Test
    void createLoanStatementSuccess() {
        LoanStatementRequestDto requestDto = mock(LoanStatementRequestDto.class);

        Client client = new Client();
        client.setClientId(UUID.randomUUID());

        Statement statement = new Statement();
        UUID statementId = UUID.randomUUID();
        statement.setStatementId(statementId);
        statement.setClient(client);

        List<LoanOfferDto> offers = List.of(new LoanOfferDto(), new LoanOfferDto(), new LoanOfferDto(), new LoanOfferDto());

        when(clientService.createClient(requestDto)).thenReturn(client);
        when(clientService.saveClient(client)).thenReturn(client);
        when(statementService.createStatement(client)).thenReturn(statement);
        when(statementService.save(statement)).thenReturn(statement);
        when(calculatorClient.requestLoanOffers(requestDto)).thenReturn(offers);

        List<LoanOfferDto> result = dealService.createLoanStatement(requestDto);

        assertThat(result).hasSize(4);
        assertThat(result).allMatch(o -> statementId.equals(o.getStatementId()));

        verify(calculatorClient).requestLoanOffers(requestDto);
        verify(clientService).createClient(requestDto);
        verify(clientService).saveClient(client);
        verify(statementService).save(statement);
        verify(statementService).createStatement(client);
    }

    @Test
    void applyLoanOffersSuccess() {
        LoanOfferDto offer = new LoanOfferDto();
        UUID statementId = UUID.randomUUID();
        offer.setStatementId(statementId);

        Statement statement = new Statement();

        when(statementService.getByIdForUpdate(statementId)).thenReturn(statement);
        when(statementService.updateStatus(statement, ApplicationStatus.APPROVED, ChangeType.AUTOMATIC))
                .thenReturn(statement);

        dealService.applyLoanOffer(offer);

        assertThat(statement.getAppliedOffer()).isEqualTo(offer);

        verify(statementService).getByIdForUpdate(statementId);
        verify(statementService).save(statement);
    }

    @Test
    void calculateCreditSuccess() {
        UUID statementId = UUID.randomUUID();

        FinishRegistrationRequestDto requestDto = mock(FinishRegistrationRequestDto.class);

        Statement statement = new Statement();
        Client client = new Client();
        statement.setClient(client);

        ScoringDataDto scoringDto = mock(ScoringDataDto.class);
        CreditDto creditDto = mock(CreditDto.class);
        Credit credit = new Credit();

        when(statementService.getById(statementId)).thenReturn(statement);

        when(clientService.updateClientInformation(client, requestDto)).thenReturn(client);
        when(clientService.saveClient(client)).thenReturn(client);

        when(scoringMapper.toScoringDataDto(statement, requestDto)).thenReturn(scoringDto);
        when(calculatorClient.calculate(scoringDto, statement)).thenReturn(creditDto);
        when(creditMapper.toCredit(creditDto)).thenReturn(credit);
        when(creditRepository.save(credit)).thenReturn(credit);

        when(statementService.updateStatus(statement, ApplicationStatus.CC_APPROVED, ChangeType.AUTOMATIC))
                .thenReturn(statement);

        dealService.calculateCredit(requestDto, statementId);

        assertThat(statement.getCredit()).isEqualTo(credit);

        verify(calculatorClient).calculate(scoringDto, statement);
        verify(creditRepository).save(credit);
        verify(statementService).save(statement);
    }
}
