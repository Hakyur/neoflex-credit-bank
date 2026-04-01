package ru.rogotovsky.deal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.rogotovsky.deal.dto.EmploymentDto;
import ru.rogotovsky.deal.dto.FinishRegistrationRequestDto;
import ru.rogotovsky.deal.dto.LoanStatementRequestDto;
import ru.rogotovsky.deal.entity.Client;
import ru.rogotovsky.deal.entity.Employment;
import ru.rogotovsky.deal.entity.Passport;
import ru.rogotovsky.deal.enums.EmploymentPosition;
import ru.rogotovsky.deal.enums.EmploymentStatus;
import ru.rogotovsky.deal.enums.Gender;
import ru.rogotovsky.deal.enums.MaritalStatus;
import ru.rogotovsky.deal.mapper.ClientMapper;
import ru.rogotovsky.deal.mapper.EmploymentMapper;
import ru.rogotovsky.deal.repository.ClientRepository;
import ru.rogotovsky.deal.service.ClientService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ClientMapper clientMapper;

    @Mock
    private EmploymentMapper employmentMapper;

    private ClientService clientService;

    @BeforeEach
    public void setUp() {
        clientService = new ClientService(clientRepository, clientMapper, employmentMapper);
    }

    @Test
    void createClientSuccess() {
        LoanStatementRequestDto dto = new LoanStatementRequestDto(
                BigDecimal.valueOf(100000), 6, "Ivan",
                "Ivanov", "Ivanovich", "ivan@gmail.com",
                LocalDate.of(2000, 1, 1), "1234", "567890"
        );

        Passport passport = new Passport(null, dto.passportSeries(), dto.passportNumber(), null, null);

        Client expected = new Client(
                null, dto.lastName(), dto.firstName(),
                dto.middleName(), dto.birthdate(), dto.email(),
                null, null, null,
                passport, null, null
        );

        when(clientMapper.toClient(dto)).thenReturn(expected);
        Client actual = clientService.createClient(dto);

        assertEquals(expected, actual);
        verify(clientMapper).toClient(dto);
    }

    @Test
    void saveClientSuccess() {
        Client expected = new Client();

        when(clientRepository.save(expected)).thenReturn(expected);
        Client actual = clientService.saveClient(expected);

        assertEquals(expected, actual);
        verify(clientRepository).save(expected);
    }

    @Test
    void updateClientInformationSuccess() {
        Passport passport = new Passport(null, "1234", "567890", null, null);

        Client client = new Client(
                UUID.randomUUID(), "Ivanov", "Ivan",
                "Ivanovich", LocalDate.of(2000, 1, 1), "ivan@gmail.com",
                null, null, null,
                passport, null, null
        );

        EmploymentDto employmentDto = new EmploymentDto(
                EmploymentStatus.EMPLOYED, "11111111", BigDecimal.valueOf(45000),
                EmploymentPosition.WORKER, 25, 10);

        FinishRegistrationRequestDto dto = new FinishRegistrationRequestDto(
                Gender.MALE, MaritalStatus.MARRIED, 1, LocalDate.of(2026, 1, 1),
                "Ministry of Internal Affairs of Russia in the Voronezh region", employmentDto,
                "1253551");

        Employment employment = new Employment(
                UUID.randomUUID(), employmentDto.employmentStatus(), employmentDto.employerINN(),
                employmentDto.salary(), employmentDto.position(), employmentDto.workExperienceTotal(),
                employmentDto.workExperienceCurrent());

        Client expected = new Client(
                client.getClientId(), client.getLastName(), client.getFirstName(),
                client.getMiddleName(), client.getBirthDate(), client.getEmail(),
                dto.gender(), dto.maritalStatus(), dto.dependentAmount(),
                passport, employment, dto.accountNumber()
        );

        when(employmentMapper.toEmployment(employmentDto)).thenReturn(employment);

        Client actual = clientService.updateClientInformation(client, dto);

        assertEquals(expected, actual);
        verify(employmentMapper).toEmployment(employmentDto);
    }
}
