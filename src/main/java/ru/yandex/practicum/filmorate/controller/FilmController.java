package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.HashMap;

@Slf4j
@RestController
public class FilmController {
    private final HashMap<Integer, Film> films = new HashMap<>();
    private final LocalDate FILM_DATE = LocalDate.of(1985, 12, 28);

    @GetMapping("/films")
    public HashMap<Integer, Film> getFilms() {
        log.info("There is '{}' movies in a library now", films.size());
        return films;
    }

    @PostMapping(value = "/films")
    public Film create(@Valid @RequestBody Film film) {
        if (film.getDuration() <= 0 || film.getReleaseDate().isBefore(FILM_DATE)
                || film.getDescription().length() > 200 || film.getName().isEmpty()) {
            throw new ValidationException("Incorrect data");
        } else {
            if (film.getId() == 0) {
                film.setId(1);
            }
            log.info("'{}' movie was added to a library, the identifier is '{}'", film.getName(), film.getId());
            films.put(film.getId(), film);
        }
        return film;
    }

    @PutMapping("/films")
    public Film update(@Valid @RequestBody Film film) {
        if (film.getDuration() <= 0 || film.getReleaseDate().isBefore(FILM_DATE)
                || film.getDescription().length() > 200) {
            throw new ValidationException("Incorrect data");
        }
        log.info("'{}' movie was updated in a library, the identifier is '{}'", film.getName(), film.getId());
        films.put(film.getId(), film);
        return film;
    }
}
