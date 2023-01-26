package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private int id = 1;

    private final Map<Integer, Film> films = new HashMap<>();

    @PostMapping
    public Film create(@RequestBody Film film) {
        log.info("добавление фильма");
        validateFilm(film);

        film.setId(id++);
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        log.info("Обновление фильма");
        validateFilm(film);
        if (!films.containsKey(film.getId())) {
            log.warn("Фильм с таким id не существует");
            throw new ValidationException("Фильм с таким id не существует");
        }

        films.replace(film.getId(), film);
        return film;
    }

    @GetMapping
    public List<Film> findAll() {
        log.info("получение всех фильмов");
        return new ArrayList<>(films.values());
    }

    private void validateFilm(Film film) {
        if (film == null) {
            log.warn("фильм не может быть null");
            throw new ValidationException("фильм не может быть null");
        }
        if (film.getName() == null || film.getName().isBlank()) {
            log.warn("название не может быть пустым");
            throw new ValidationException("название не может быть пустым");
        }
        if (film.getDescription().length() > 200) {
            log.warn("Максимальная длина описания - 200 символов");
            throw new ValidationException("Максимальная длина описания - 200 символов");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, Month.DECEMBER,28))) {
            log.warn("Дата релиза не должно быть раньше 28 декабря 1895 года");
            throw new ValidationException("Дата релиза не должно быть раньше 28 декабря 1895 года");
        }
        if (film.getDuration() <= 0) {
            log.warn("Продолжительность фильма должна быть положительной");
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }
    }

}
