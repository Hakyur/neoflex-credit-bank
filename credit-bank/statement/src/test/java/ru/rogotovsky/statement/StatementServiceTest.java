package ru.rogotovsky.statement;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.rogotovsky.statement.client.DealClient;
import ru.rogotovsky.statement.dto.LoanOfferDto;
import ru.rogotovsky.statement.dto.LoanStatementRequestDto;
import ru.rogotovsky.statement.service.StatementService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StatementServiceTest {

    @Mock
    private DealClient dealClient;

    @InjectMocks
    private StatementService statementService;


    public LoanStatementRequestDto createRequest() {
        return new LoanStatementRequestDto(
                BigDecimal.valueOf(100000),
                12,
                "Ivan",
                "Ivanov",
                "Ivanovich",
                "test@mail.com",
                LocalDate.of(2000, 1, 1),
                "1234",
                "123456"
        );
    }

    public LoanOfferDto createOffer() {
        return new LoanOfferDto(
                UUID.randomUUID(),
                BigDecimal.valueOf(100000),
                BigDecimal.valueOf(100000),
                12,
                BigDecimal.valueOf(9263.45),
                BigDecimal.valueOf(20),
                false,
                false
        );
    }

    @Test
    void processLoanApplication_shouldReturnOffers() {
        LoanStatementRequestDto request = createRequest();
        List<LoanOfferDto> expected = List.of(createOffer());

        when(dealClient.requestLoanOffers(request)).thenReturn(expected);

        List<LoanOfferDto> result = statementService.processLoanApplication(request);

        assertEquals(expected, result);
        verify(dealClient).requestLoanOffers(request);
    }

    @Test
    void selectLoanOffer_shouldCallDealClient() {
        LoanOfferDto offer = createOffer();

        statementService.selectLoanOffer(offer);

        verify(dealClient).requestOfferSelection(offer);
    }
}
