package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.exeptions.ElementNotFoundException;
import ru.yandex.practicum.filmorate.exception.exeptions.ReleaseDateValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
@Validated
public class FilmController {

    public final Map<Long, Film> movies = new HashMap<>();

    private final LocalDate releaseDateBefore = LocalDate.of(1895, 12, 28);

    @GetMapping
    public Collection<Film> getAllFilms() {
        log.info("Getting films...");
        return movies.values();
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {

        if (film.getReleaseDate().isBefore(releaseDateBefore)) {
            log.debug("ReleaseDate {}", film.getReleaseDate());
            throw new ReleaseDateValidationException("Release date can not be earlier December 12, 1895");
        }

        log.info("Start addition of film...");
        film.setId(getNexId());
        movies.put(film.getId(), film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film newFilm) {
        log.trace("Вызван метод updateUser");
        Film film = movies.get(newFilm.getId());

        if (film == null) {
            log.error("Error: user not found");
            throw new ElementNotFoundException("User not found");
        }

        movies.put(newFilm.getId(), newFilm);
        return newFilm;
    }

    private Long getNexId() {
        long currentMaxId = movies.values().stream()
                .mapToLong(Film::getId)
                .max()
                .orElse(0);
        log.debug("currentFilmMaxId {}", currentMaxId);
        return ++currentMaxId;
    }
}