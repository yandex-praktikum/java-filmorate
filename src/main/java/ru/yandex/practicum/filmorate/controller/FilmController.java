package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static ru.yandex.practicum.filmorate.validator.Validator.validate;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private int filmId = 0;
    private final HashMap<Integer, Film> films = new HashMap<>();

    @GetMapping
    public List<Film> filmList() {
        log.debug("Количество фильмов в списке " + films.size());
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film createFilm(@RequestBody Film film) throws ValidationException {
        if (films.containsKey(film.getId())){
            log.debug("Фильм уже добавлен - " + films.get(film.getId()));
            throw new ValidationException("Фильм с таким Id уже добавлен");
        }else {
            validate(film);
            filmId++;
            film.setId(filmId);
            films.put(film.getId(), film);
            log.debug("добавлен новый фильм" + film);
            return film;
        }
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) throws ValidationException {
        if (films.containsKey(film.getId())) {
            validate(film);
            films.put(film.getId(), film);
            log.debug("Фильм обновлен: " + film);
            return film;
        } else {
            log.debug("Id отсутствует в списке - " + film.getId());
            throw new ValidationException("Фильм с ID " + film.getId() + "отсутствует" );
        }
    }
}
