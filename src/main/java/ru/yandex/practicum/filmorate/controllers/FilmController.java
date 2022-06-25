package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.FilmValidationException;
import ru.yandex.practicum.filmorate.exceptions.FriendNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.RatingMPA;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.*;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;
    private final UserService userService;

    @Autowired
    public FilmController(FilmService filmService, UserService userService) {
        this.filmService = filmService;
        this.userService = userService;
    }

    @GetMapping()
    public List<Film> getAllFilms() {
        log.info("Выполнен запрос getAllFilms.");
        return filmService.getAllFilms();
    }

    @GetMapping("{id}")
    public Film getFilm(@PathVariable Long id) {
        Film film = filmService.getFilm(id);
        if (film == null)
            throw new FilmNotFoundException();
        log.info("Выполнен запрос getUser.");
        return film;
    }

    @PostMapping()
    public Film create(@Valid @RequestBody Film film) {
        if (film.getId() != null)
            throw new FilmValidationException("id");

        Film filmForSave = filmService.create(film);
        log.info("Выполнен запрос createFilm. Текущее количество фильмов: " + filmService.getAllFilms().size());
        return filmForSave;
    }

    @PutMapping()
    public Film update(@Valid @RequestBody Film film) {
        if (film.getId() == null)
            throw new FilmValidationException("id");

        if (filmService.getFilm(film.getId()) == null)
            throw new FilmNotFoundException();

        Film filmForSave = filmService.update(film);
        log.info("Выполнен запрос updateFilm.");
        return filmForSave;
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable Long id, @PathVariable Long userId) {
        if (filmService.getFilm(id) == null)
            throw new FriendNotFoundException();

        if (userService.getUser(userId) == null)
            throw new UserNotFoundException();

        log.info("Выполнен запрос addLike.");
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable Long id, @PathVariable Long userId) {
        if (filmService.getFilm(id) == null)
            throw new FriendNotFoundException();

        if (userService.getUser(userId) == null)
            throw new UserNotFoundException();

        log.info("Выполнен запрос removeLike.");
        filmService.removeLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getFilmsWithMostLikes(@RequestParam Optional<Integer> count) {
        log.info("Выполнен запрос getFilmsWithMostLikes.");
        return filmService.getFilmsWithMostLikes(count.orElse(10));
    }
}