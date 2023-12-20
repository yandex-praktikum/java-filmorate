package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
@Slf4j
@Validated
public class FilmController {

    private final FilmService filmService;
    private final FilmValidator filmValidator;

    @GetMapping
    public List<Film> findAll() {
        log.debug("List off all films has been requested.");
        return filmService.findAll();
    }

    @GetMapping("/{id}")
    public Film findById(@PathVariable Integer id) {
        log.debug("Film with id = {} has been requested.", id);

        return filmService.findById(id);
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film){
        filmValidator.validateFilmReleaseDate(film.getReleaseDate());

        Film createdFilm = filmService.create(film);

        log.debug("Film with id = {} has been created.", createdFilm.getId());
        return createdFilm;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        filmValidator.validateFilmReleaseDate(film.getReleaseDate());

        Film updatedFilm = filmService.update(film);

        log.debug("Film with id = {} has been updated.", updatedFilm.getId());
        return updatedFilm;
    }
}
