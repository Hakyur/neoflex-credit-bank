package ru.rogotovsky.deal.mapper;

import org.springframework.stereotype.Component;
import ru.rogotovsky.deal.dto.CreditDto;
import ru.rogotovsky.deal.entity.Credit;
import ru.rogotovsky.deal.enums.CreditStatus;

@Component
public class CreditMapper {

    public Credit toEntity(CreditDto creditDto) {
        Credit credit = new Credit();

        credit.setAmount(creditDto.amount());
        credit.setTerm(creditDto.term());
        credit.setMonthlyPayment(creditDto.monthlyPayment());
        credit.setRate(creditDto.rate());
        credit.setPsk(creditDto.psk());
        credit.setPaymentSchedule(creditDto.paymentSchedule());
        credit.setInsuranceEnabled(creditDto.isInsuranceEnabled());
        credit.setSalaryClient(creditDto.isSalaryClient());
        credit.setCreditStatus(CreditStatus.CALCULATED);

        return credit;
    }
}
