package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validation.ValidationException;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private int filmId = 1;

    private int generatorId() {
        return filmId++;
    }

    @GetMapping
    public List<Film> findAll() {
        log.info("Фильмов в коллекции: {}", films.size());
        return List.copyOf(films.values());
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) throws ValidationException {
        log.debug("Попытка добавить фильм: {}", film);

        validateFilms(film);
        film.setId(generatorId());
        films.put(film.getId(), film);
        log.debug("Фильм успешно добавлен: {}", film);

        return film;
    }

    @PutMapping
    public Film put(@Valid @RequestBody Film film) throws ValidationException {
        log.debug("Попытка обновить фильм: {}", film);
        validateFilms(film);

        if (films.get(film.getId()) != null) {
            films.replace(film.getId(), film);
            log.debug("Фильм успешно обновлен: {}", film);
        } else {
            log.debug("Такого фильма не существует");
            throw new ValidationException("Такого фильма не существует");
        }
        return film;
    }

    public Film validateFilms(Film film) throws ValidationException {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("Валидация не пройдена. Причина - дата релиза фильма");
            throw new ValidationException("Дата релиза не может быть раньше чем 28.12.1895");
        }
        return film;
    }
}