package ru.rogotovsky.deal.mapper;

import org.springframework.stereotype.Component;
import ru.rogotovsky.deal.dto.LoanStatementRequestDto;
import ru.rogotovsky.deal.entity.Client;
import ru.rogotovsky.deal.entity.Passport;

@Component
public class ClientMapper {

    public Client toClient(LoanStatementRequestDto dto) {
        Passport passport = new Passport();
        passport.setSeries(dto.passportSeries());
        passport.setNumber(dto.passportNumber());

        Client client = new Client();
        client.setFirstName(dto.firstName());
        client.setLastName(dto.lastName());
        client.setMiddleName(dto.middleName());
        client.setEmail(dto.email());
        client.setBirthDate(dto.birthdate());
        client.setPassport(passport);

        return client;
    }
}
