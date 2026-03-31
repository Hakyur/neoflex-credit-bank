package ru.rogotovsky.deal.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.rogotovsky.deal.dto.FinishRegistrationRequestDto;
import ru.rogotovsky.deal.dto.LoanStatementRequestDto;
import ru.rogotovsky.deal.entity.Client;
import ru.rogotovsky.deal.entity.Passport;
import ru.rogotovsky.deal.mapper.ClientMapper;
import ru.rogotovsky.deal.mapper.EmploymentMapper;
import ru.rogotovsky.deal.repository.ClientRepository;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;
    private final EmploymentMapper employmentMapper;

    public Client createClient(LoanStatementRequestDto requestDto) {
        return clientMapper.toClient(requestDto);
    }

    public Client saveClient(Client client) {
        return clientRepository.save(client);
    }

    public Client updateClientInformation(Client client, FinishRegistrationRequestDto requestDto) {
        Passport passport = client.getPassport();
        passport.setIssueDate(requestDto.passportIssueDate());
        passport.setIssueBranch(requestDto.passportIssueBranch());

        client.setPassport(passport);
        client.setGender(requestDto.gender());
        client.setMaritalStatus(requestDto.maritalStatus());
        client.setDependentAmount(requestDto.dependentAmount());
        client.setAccountNumber(requestDto.accountNumber());
        client.setEmployment(employmentMapper.toEmployment(requestDto.employment()));

        return client;
    }
}
