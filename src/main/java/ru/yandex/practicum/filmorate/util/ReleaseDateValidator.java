package ru.yandex.practicum.filmorate.util;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class ReleaseDateValidator implements ConstraintValidator<ReleaseDateValidation, LocalDate> {
    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {
        final LocalDate FIRST_RELEASE_DATE = LocalDate.of(1895, 12, 28);
        return localDate.isAfter(FIRST_RELEASE_DATE);
    }
}
