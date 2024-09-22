package ru.yandex.practicum.filmorate.validators;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;


import java.time.LocalDate;

@Slf4j
public class FilmValidator {
    private final static int MAX_DESCRIPTION_LENGTH = 200;
    private final static LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 12);

    public static boolean isValidate(Film film) {
        return (checkName(film) && checkDescription(film) && checkDuration(film) && checkReleaseDate(film));
    }

    private static boolean checkName(Film film) {
        if (!film.getName().isBlank() || film.getName().equals(null))
            return true;
        else
            throw new ValidationException("Название фильма не может быть пустым");
    }

    private static boolean checkDescription(Film film) {
        if (film.getDescription().length() <= MAX_DESCRIPTION_LENGTH)
            return true;
        else
            throw new ValidationException("Описание фильма не может быть длиннее 200 символов");
    }

    private static boolean checkReleaseDate(Film film) {
        if (film.getReleaseDate().isAfter(MIN_RELEASE_DATE))
            return true;
        else
            throw new ValidationException("Некорректная дата выпуска");
    }

    private static boolean checkDuration(Film film) {
        if (film.getDuration() > 0) {
            return true;
        }
        else
            throw new ValidationException("Длительность фильма не может быть отрицательной");
    }
}