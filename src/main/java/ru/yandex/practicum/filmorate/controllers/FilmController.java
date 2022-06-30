package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.*;

@RestController
@Slf4j
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();

    @GetMapping("/films")
    public List<Film> getFilms() {
        log.info("Получен запрос, количество фильмов: " + films.size());
        return new ArrayList<>(films.values());
    }

    @PostMapping("/films")
    public Film postFilm(@Valid @RequestBody Film film) {
        film.generateId();
        log.debug("Добавлен фильм: " + film);
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping("/films")
    public Film putFilm(@Valid @RequestBody Film film) {
        if (films.containsKey(film.getId())) {
            log.debug("Обновлён фильм: " + film);
            films.put(film.getId(), film);
        } else {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Фильм не найден");
        }
        return film;
    }
}
