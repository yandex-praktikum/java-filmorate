package ru.yandex.practicum.filmorate.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.yandex.practicum.filmorate.exceptions.ValidatorException;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class AfterIncludeValidator implements ConstraintValidator<AfterInclude, LocalDate> {

    private LocalDate startDate;

    @Override
    public void initialize(AfterInclude constraintAnnotation) {
        try {
            this.startDate = LocalDate.parse(constraintAnnotation.value());
        } catch (DateTimeParseException e) {
            throw new ValidatorException(
                    "Invalid @AfterInclude value: " + constraintAnnotation.value() +
                            ". Use ISO format YYYY-MM-DD."
            );
        }
    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return !value.isBefore(startDate);
    }
}
