package ru.rogotovsky.deal.mapper;

import org.springframework.stereotype.Component;
import ru.rogotovsky.deal.dto.EmploymentDto;
import ru.rogotovsky.deal.entity.Employment;

@Component
public class EmploymentMapper {

    public Employment toEntity(EmploymentDto dto) {
        return new Employment(
                null,
                dto.employmentStatus(),
                dto.employerINN(),
                dto.salary(),
                dto.position(),
                dto.workExperienceTotal(),
                dto.workExperienceCurrent()
        );
    }
}
