package ru.rogotovsky.deal.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.rogotovsky.deal.dto.FinishRegistrationRequestDto;
import ru.rogotovsky.deal.dto.LoanStatementRequestDto;
import ru.rogotovsky.deal.entity.Client;
import ru.rogotovsky.deal.mapper.ClientMapper;
import ru.rogotovsky.deal.repository.ClientRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    public Client createClient(LoanStatementRequestDto requestDto) {
        log.info("Creating client from LoanStatementRequestDto: {}", requestDto);

        Client client = clientMapper.toClient(requestDto);

        log.info("Client successfully created: {}", client);
        return client;
    }

    public Client saveClient(Client client) {
        log.info("Saving client with id: {}", client.getClientId());

        Client savedClient = clientRepository.save(client);

        log.info("Client saved successfully with id: {}", savedClient.getClientId());
        return savedClient;
    }

    public Client updateClientInformation(Client client, FinishRegistrationRequestDto requestDto) {
        log.info("Updating client information for clientId: {}", client.getClientId());

        clientMapper.updateClientFromDto(requestDto, client);

        log.debug("Updated client data: {}", client);
        return client;
    }
}
