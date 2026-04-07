package ru.rogotovsky.statement.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.Period;

public class AgeVerificationValidator implements ConstraintValidator<AgeVerification, LocalDate> {
    @Override
    public boolean isValid(LocalDate birthdate, ConstraintValidatorContext constraintValidatorContext) {
        int age = Period.between(birthdate, LocalDate.now()).getYears();

        return age >= 18;
    }
}
