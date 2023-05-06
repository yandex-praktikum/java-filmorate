package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import jakarta.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final Map<Integer, Film> films = new HashMap<>();
    private int filmId = 1;

    @GetMapping
    public List<Film> getFilm() {
        return new ArrayList<>(films.values());
    }


    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        film.setId(filmId);
        films.put(filmId, film);
        filmId++;
        log.info("The film has been added", film);
        return film;
    }

    @PutMapping
    public Film updated(@Valid @RequestBody Film film) {
        int id = film.getId();
        if (films.containsKey(id)) {
            films.put(id, film);
            log.info("The film has been updated", film);
        } else {
            log.warn("This film doesn't existed");
            throw new ValidationException("This film doesn't existed");
        }
        return film;
    }


}
