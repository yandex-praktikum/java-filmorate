package ru.yandex.practicum.filmorate.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class BeforeValidator implements ConstraintValidator<Before, LocalDate> {

    private LocalDate date;
    @Override
    public void initialize(Before annotation) {
        date = LocalDate.now();
    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        boolean valid = true;
        if (value != null) {
            if (!value.isBefore(date)) {
                valid = false;
            }
        }
        return valid;
    }
}
