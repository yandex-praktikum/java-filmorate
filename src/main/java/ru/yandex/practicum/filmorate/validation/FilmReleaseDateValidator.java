package ru.yandex.practicum.filmorate.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class FilmReleaseDateValidator implements ConstraintValidator<FilmReleaseDate, LocalDate> {

    private static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    @Override
    public boolean isValid(LocalDate releaseDate, ConstraintValidatorContext constraintValidatorContext) {
        boolean isValidReleaseDate = false;
        if (releaseDate != null && !releaseDate.isBefore(MIN_RELEASE_DATE)) {
            isValidReleaseDate = true;
        }
        return isValidReleaseDate;
    }
}
