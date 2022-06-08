package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.ValidationException;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/films")
public class FilmController {

    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private final HashMap<Integer, Film> films = new HashMap<>();
    private int i = 0;

    public Film getFilm(int id) {
        return films.get(id);
    }

    @GetMapping
    public Collection<Film> findAll() {
        log.debug("Текущее количество фильмов: {}", films.size());
        return films.values();
    }

    @PostMapping
    public Film create(@RequestBody Film film) throws ValidationException {
        film.setId(i = i + 1);
        int len = film.getDescription().length();
        LocalDate realize = LocalDate.of(1895, 12, 28);
        if (film.getName().isEmpty() || len > 200 || film.getReleaseDate().isBefore(realize) || film.getDuration() < 0) {
            log.debug("Валидация не пройдена: {}", film);
            throw new ValidationException();
        }
        log.debug("Сохраняем новый фильм: {}", film);
        films.put(i, film);
        return film;
    }

    @PutMapping
    public Film saveFilm(@RequestBody Film film) throws ValidationException {
        if (film.getId() < 0) {
            log.debug("Валидация не пройдена: {}", film);
            throw new ValidationException();
        }
        for (Film oldFilm : films.values()) {
            if (oldFilm.getId() == film.getId()) {
                oldFilm.setName(film.getName());
                oldFilm.setDescription(film.getDescription());
                oldFilm.setReleaseDate(film.getReleaseDate());
                oldFilm.setDuration(film.getDuration());
                log.debug("Обновляем фильм: {}", oldFilm);
                return oldFilm;
            }
        }
        log.debug("Добовляем новый фильм: {}", film);
        return film;
    }
}
