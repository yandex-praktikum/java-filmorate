package ru.yandex.practicum.filmorate.validator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

@Slf4j
public class FilmValidator {
    // валидация фильмов - исправление возможных ошибок при внесении
       
    public static void isValidFilms(@RequestBody Film film) throws ValidationException {
        if (film.getName().isBlank()) {
            log.warn("Ошибка в названии: {}", film);
            throw new ValidationException("Фильм не соответствует условиям: " +
                    "название не должно быть пустым");
        }
        if (film.getDescription().length() > 200) {
            log.warn("Ошибка в описании: {}", film);
            throw new ValidationException("Фильм не соответствует условиям: " +
                    "длина описания не может быть больше 200 символов");
        }
        if (film.getReleaseDate().isBefore(LocalDate.parse("1895-12-27"))) {
            log.warn("Ошибка в дате релиза: {}", film);
            throw new ValidationException("Фильм не соответствует условиям: " +
                    "дата релиза не может быть раньше 28 декабря 1895 года");
        }
        if (film.getDuration() < 0) {
            log.warn("Ошибка в продолжительности: {}", film);
            throw new ValidationException("Фильм не соответствует условиям: " +
                    "продолжительность фильма не может быть отрицательной");
        }
    }
}
