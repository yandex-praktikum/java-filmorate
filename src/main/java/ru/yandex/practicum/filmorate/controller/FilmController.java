package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class FilmController {
    private static final int MAX_DESCRIPTION_SIZE = 200;
    private static final LocalDate FIRST_EVER_FILM = LocalDate.of(1895, 12, 28);
    private final Map<Integer, Film> films;

    public FilmController() {
        films = new HashMap<>();
    }

    @PostMapping("/films")
    public Film addFilm(@RequestBody Film film) throws ValidationException {
        if (isValidate(film)) {
            films.put(film.getId(), film);
            log.info("Добавлен фильм: {}", film.getName());
        }
        return film;
    }

    @PutMapping("/films")
    public Film updateFilm(@RequestBody Film film) throws ValidationException {
        if (isValidate(film)) {
            films.put(film.getId(), film);
            log.info("Отредактирован фильм {}", film.getName());
        }
        return film;
    }

    @GetMapping("/films")
    public Map<Integer, Film> getAllFilms() {
        return films;
    }

    private boolean isValidate(Film film) throws ValidationException {
        if (film.getName().isBlank()) {
            log.error("Название фильма не может быть пустым");
            throw new ValidationException("invalid film name");
        } else if (film.getDescription().length() > MAX_DESCRIPTION_SIZE || film.getDescription().isBlank()) {
            log.error(String.format("Максимальная длина описания — %s символов", MAX_DESCRIPTION_SIZE));
            throw new ValidationException("invalid description");
        } else if (film.getReleaseDate().isBefore(FIRST_EVER_FILM)) {
            log.error("Дата релиза не может быть раньше 28 декабря 1895 года");
            throw new ValidationException("invalid release date");
        } else if (film.getDuration().isNegative()) {
            log.error("Продолжительность фильма должна быть положительной");
            throw new ValidationException("invalid duration");
        } else {
            return true;
        }
    }
}