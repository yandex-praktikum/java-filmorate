package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validators.FilmValidator;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        if (FilmValidator.isValidate(film)) {
            film.setId(getNextId());
            films.put(film.getId(), film);
        } else
            throw new ValidationException("Непредвиденная ошибка..");
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        if (FilmValidator.isValidate(film)) {
            if (films.containsKey(film.getId())) {
                films.replace(film.getId(), film);
            } else throw new ValidationException("Фильма с таким id не существует");
        } else
            throw new ValidationException("Непредвиденная ошибка");
        return film;
    }

    @GetMapping
    public Collection<Film> getFilms() {
        return films.values();
    }

    private Integer getNextId() {
        Integer currentMaxId = films.keySet()
                .stream()
                .mapToInt(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}