package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;


import java.time.LocalDate;
import java.util.Collections;
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
    public ResponseEntity<Object> addFilm(@Valid @RequestBody Film film, BindingResult bindingResult) {
        log.info("Attempting to add a new film with title: {}", film.getName());

        if (bindingResult.hasErrors()) {
            log.warn("Validation addFilm errors occurred: {}", bindingResult.getAllErrors());
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Validation error: " + bindingResult.getAllErrors()));
        }

        try {
            validateReleaseDate(film.getReleaseDate());
        } catch (ValidationException e) {
            log.error("Error creating film: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error",
                    "Error creating film due to invalid input: " + e.getMessage()));
        }
        Film savedFilm = filmService.addFilm(film);
        log.info("Film added successfully with ID: {}", savedFilm.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedFilm);
    }


    @PutMapping("/{id}")
    public ResponseEntity<Object> updateFilm(@PathVariable Long id, @Valid @RequestBody Film film,
                                             BindingResult bindingResult) {
        log.info("Attempting to update film with ID: {}", id);
        if (bindingResult.hasErrors()) {
            log.warn("Validation updateFilm errors occurred: {}", bindingResult.getAllErrors());
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Validation error: "
                    + bindingResult.getAllErrors()));
        }
        try {
            validateReleaseDate(film.getReleaseDate());
        } catch (ValidationException e) {
            log.error("Error creating film: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error",
                    "Error updating film due to invalid input: " + e.getMessage()));
        }
        Film updatedFilm = filmService.updateFilm(id, film);
        if (updatedFilm == null) {
            log.warn("Failed to find film with ID: {} for update", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error",
                    "Film not found with ID: " + id));
        }
        log.info("Film updated successfully with ID: {}", updatedFilm.getId());
        return ResponseEntity.status(HttpStatus.OK).body(updatedFilm);
    }

    @GetMapping
    public ResponseEntity<List<Film>> getAllFilms() {
        log.debug("Fetching all films.");
        List<Film> films = filmService.getAllFilms();
        return ResponseEntity.status(HttpStatus.OK).body(films);
    }

    private void validateReleaseDate(LocalDate releaseDate) {
        LocalDate earliestReleaseFilmDate = LocalDate.of(1895, 12, 28);
        if (releaseDate.isBefore(earliestReleaseFilmDate)) {
            log.error("Release date validation failed for date: {}. It must be on or after 28th December 1895.", releaseDate);
            throw new ValidationException("Дата релиза должна быть не раньше 28 декабря 1895 года.");
        }
    }
}
