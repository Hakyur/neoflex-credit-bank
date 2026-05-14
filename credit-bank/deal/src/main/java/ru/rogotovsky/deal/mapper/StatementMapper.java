package ru.rogotovsky.deal.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.rogotovsky.deal.dto.StatementDto;
import ru.rogotovsky.deal.entity.Statement;

@Mapper(componentModel = "spring", uses = CreditMapper.class)
public interface StatementMapper {

    @Mapping(target = "clientId", source = "client.clientId")
    StatementDto toDto(Statement statement);
}
