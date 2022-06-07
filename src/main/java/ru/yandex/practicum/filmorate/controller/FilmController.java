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
    private final Set<Film> films = new HashSet<>();

    @GetMapping
    public Collection<Film> findAll() {
        log.debug("Текущее количество фильмов: {}", films.size());
        return films;
    }

    @PostMapping
    public Film create(@RequestBody Film film) throws ValidationException {

        int len = film.getDescription().length();
        LocalDate realize = LocalDate.of(1985, 12, 28);

        if (film.getName().isEmpty() || len < 200 || film.getReleaseDate().isAfter(realize) || film.getDuration().toSeconds() > 0) {
            throw new ValidationException();
        }
        log.debug("Сохраняем фильм: {}", film);
        films.add(film);
        return film;
    }

    @PutMapping
    public Film saveFilm(@RequestBody Film film) {
        for (Film oldFilm : films) {
            if (oldFilm.getId() == film.getId()) {
                oldFilm.setName(film.getName());
                oldFilm.setDescription(film.getDescription());
                oldFilm.setReleaseDate(film.getReleaseDate());
                oldFilm.setDuration(film.getDuration());
                return oldFilm;
            }
        }
        return film;
    }
}
