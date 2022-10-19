package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import static ru.yandex.practicum.filmorate.validation.Validation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final List<Film> films = new ArrayList<>();
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private int filmId = 1;
    @GetMapping
    public List<Film> allFilms(){
        return films;
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film){
        log.info("Получен POST-запрос с объектом Film: {}", film);
        film.setId(filmId);
        filmValidate(film);
        films.add(film);
        log.info("Фильм {} c id={} добавлен, объект: {}", film.getName(), filmId, film);
        increaseFilmId();
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film){
        log.info("Получен PUT-запрос с объектом Film: {}", film);
        filmValidate(film);
        int filmId = film.getId();
        for (Film u : films) {
            if (u.getId() == filmId) {
                films.set(films.indexOf(u), film);
                log.info("Фильм c id={} обновлён", filmId);
                return film;
            }
        }
        throw new ValidationException("Фильм с id=" + filmId + " не найден");
    }

    private void increaseFilmId(){
        filmId++;
    }
}
