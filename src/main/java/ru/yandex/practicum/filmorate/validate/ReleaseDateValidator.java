package ru.yandex.practicum.filmorate.validate;

import ru.yandex.practicum.filmorate.validate.annotation.ReleaseValidation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class ReleaseDateValidator implements ConstraintValidator<ReleaseValidation, LocalDate> {

    private final LocalDate minDate = LocalDate.of(1895, 12, 28);

    @Override
    public boolean isValid(LocalDate releaseDate, ConstraintValidatorContext constraintValidatorContext) {
        return minDate.isBefore(releaseDate);
    }
}
