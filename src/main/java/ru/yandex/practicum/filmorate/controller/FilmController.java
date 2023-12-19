package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.BadRequestException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
@Slf4j
@Validated
public class FilmController {

    private static final LocalDate EARLIEST_FILM = LocalDate.of(1895, 12, 28);
    private final FilmService filmService;

    @GetMapping
    public List<Film> findAll() {
        log.debug("List off all films has been requested.");
        return filmService.findAll();
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film){
        validateFilmReleaseDate(film.getReleaseDate());

        Film createdFilm = filmService.create(film);

        log.debug("Film with id = {} has been created.", createdFilm.getId());
        return createdFilm;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        validateFilmReleaseDate(film.getReleaseDate());

        Film updatedFilm = filmService.update(film);

        log.debug("Film with id = {} has been updated.", updatedFilm.getId());
        return updatedFilm;
    }

    private void validateFilmReleaseDate(LocalDate releaseDate) {
        if (releaseDate.isBefore(EARLIEST_FILM)) {
            final String message = "Film release date should not be earlier that 28.12.1985.";
            log.warn(message);
            throw new BadRequestException(message);
        }
    }
}
