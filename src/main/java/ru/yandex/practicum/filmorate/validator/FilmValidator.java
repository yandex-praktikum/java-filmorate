package ru.yandex.practicum.filmorate.validator;

import lombok.NonNull;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.exception.FilmValidatorException;

import java.time.LocalDate;

import static java.time.format.DateTimeFormatter.ISO_DATE;
import static ru.yandex.practicum.filmorate.validator.exception.FilmValidatorException.*;

public class FilmValidator {
    public static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);

    public static void validate(@NonNull Film film) throws FilmValidatorException {
        String name = film.getName();
        String description = film.getDescription();
        LocalDate releaseDate = film.getReleaseDate();
        int duration = film.getDuration();

        if (name == null || name.isBlank()) {
            throw new FilmValidatorException(
                    String.format(INCORRECT_NAME, name));
        }

        if (description.length() > 200) {
            throw new FilmValidatorException(
                    String.format(INCORRECT_DESCRIPTION, description));
        }

        if (releaseDate.isBefore(MIN_RELEASE_DATE)) {
            throw new FilmValidatorException(
                    String.format(INCORRECT_RELEASE_DATE, releaseDate.format(ISO_DATE)));
        }

        if (duration < 0) {
            throw new FilmValidatorException(
                    String.format(INCORRECT_DURATION, duration));
        }
    }
}