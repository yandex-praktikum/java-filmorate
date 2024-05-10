package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;


import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
        log.debug("FilmController initialized with FilmService.");
    }


    @PostMapping
    public ResponseEntity<Film> addFilm(@RequestBody Film film) {
        log.info("Attempting to add a new film with title: {}", film.getName());
        validateReleaseDate(film.getReleaseDate());
        Film savedFilm = filmService.addFilm(film);
        log.info("Film added successfully with ID: {}", savedFilm.getId());
        return ResponseEntity.ok(savedFilm);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Film> updateFilm(@PathVariable Long id, @RequestBody Film film) {
        log.info("Attempting to update film with ID: {}", id);
        validateReleaseDate(film.getReleaseDate());
        Film updatedFilm = filmService.updateFilm(id, film);
        if (updatedFilm == null) {
            log.warn("Failed to find film with ID: {} for update", id);
            return ResponseEntity.notFound().build();
        }
        log.info("Film updated successfully with ID: {}", updatedFilm.getId());
        return ResponseEntity.ok(updatedFilm);
    }

    @GetMapping
    public ResponseEntity<List<Film>> getAllFilms() {
        log.debug("Fetching all films.");
        List<Film> films = filmService.getAllFilms();
        return ResponseEntity.ok(films);
    }

    private void validateReleaseDate(LocalDate releaseDate) {
        LocalDate earliestReleaseFilmDate = LocalDate.of(1895, 12, 28);
        if (releaseDate.isBefore(earliestReleaseFilmDate)) {
            log.error("Release date validation failed for date: {}. It must be on or after 28th December 1895.", releaseDate);
            throw new ValidationException("Дата релиза должна быть не раньше 28 декабря 1895 года.");
        }
    }
}
