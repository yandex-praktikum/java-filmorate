package ru.yandex.practicum.filmorate.controller;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exceptions.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private static int id = 0;

    private final Map<Integer, Film> films;

    public FilmController() {
        films = new HashMap<>();
    }

    @GetMapping
    public List<Film> findAll() {
        log.debug("Current number of films: {}", films.size());
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film create(@RequestBody Film film)  {
        validateFilm(film);
        film.setId(++id);
        films.put(film.getId(), film);
        log.debug("Film created: "+ film);
        return film;
    }

    @PutMapping
    public Film update (@RequestBody Film film)  {
        if (!films.containsKey(film.getId())) {
            throw new ValidationException("Film with id " + id +" not found");
        }
        try {
            validateFilm(film);
            films.put(film.getId(), film);
            log.debug("Film updated: " + film);
        } catch (ValidationException ex) {
            System.out.println(ex.getMessage());
        }
        return film;
    }

    public void validateFilm(Film film) {
        if (film.getName().isBlank() || film.getName() == null) {
            throw new ValidationException("Film name should not be blank");
        } else if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Film release date not be earlier than 28-12-1895");
        } else if (film.getDescription().length() > 200) {
            throw new ValidationException("Film description should not be more than 200 symbols");
        } else if (film.getDuration() <= 0) {
            throw new ValidationException("Film duration should not be more less than 1 minute");
        }
    }
}




