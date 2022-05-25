package ru.yandex.practicum.filmorate.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class AfterValidator implements ConstraintValidator<After, LocalDate> {

    private LocalDate date;

    @Override
    public void initialize(After annotation) {
        date = LocalDate.parse(annotation.value());
    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {

        boolean valid = true;
        if (value != null) {
            if (!value.isAfter(date)) {
                valid = false;
            }
        }
        return valid;
    }
}