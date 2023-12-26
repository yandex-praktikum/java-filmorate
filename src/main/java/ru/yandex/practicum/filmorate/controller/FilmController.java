package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmService;
import ru.yandex.practicum.filmorate.model.ValidationException;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.List;

@RestController
@Getter
public class FilmController {
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private final FilmService filmService = new FilmService();
    private final LocalDate startReleaseDate = LocalDate.of(1895, 12, 28);

    @GetMapping("/films")
    public List<Film> findAll() {
        log.info("Request received!");
        log.info("Current number of films: {}", filmService.getFilms().size());
        return filmService.getFilms();
    }

    @PostMapping(value = "/films")
    public Film create(@RequestBody Film film) {
        filmValidate(film);
        log.info("Film " + film.getName() + " added");
        filmService.add(film);
        return film;
    }

    @PutMapping("/films")
    public Film saveFilm(@RequestBody Film film, HttpServletResponse response) {
        filmValidate(film);
        if (filmService.isAlreadyExists(film)) {
            response.setStatus(HttpServletResponse.SC_OK);
            log.info("Film updated" + film.getName());
            return filmService.update(film);
        } else if (!filmService.isAlreadyExists(film) && (film.getId() != 0)) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return film;
        } else {
            response.setStatus(HttpServletResponse.SC_OK);
            log.info("Film created" + film.getName());
            return filmService.add(film);
        }
    }

    private void filmValidate(Film film) {
        if (film.getName().isEmpty() || film.getName().isBlank()) {
            throw new ValidationException("The name is empty");
        }
        if (film.getDescription().length() > 200) {
            throw new ValidationException("The description length more than 200 symbols");
        }
        if (film.getReleaseDate().isBefore(startReleaseDate)) {
            throw new ValidationException("Release date earlier than 28-12-1895");
        }
        if (film.getDuration() < 0) {
            throw new ValidationException("Film duration is negative");
        }
    }
}
