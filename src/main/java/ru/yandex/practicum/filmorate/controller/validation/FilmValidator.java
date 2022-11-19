package ru.yandex.practicum.filmorate.controller.validation;

import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class FilmValidator implements ConstraintValidator<FilmValid, Film> {

    @Override
    public boolean isValid(Film film, ConstraintValidatorContext constraintValidatorContext) {
        return film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))
                && film.getReleaseDate().isAfter(LocalDate.now());
    }
}

