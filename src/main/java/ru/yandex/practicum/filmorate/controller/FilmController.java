package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.IncorrectCountException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.ArrayList;
import java.util.List;

@RestController
@Slf4j
public class FilmController {
    FilmStorage filmStorage;
    FilmService filmService;

    @Autowired
    public FilmController(FilmStorage filmStorage, FilmService filmService) {
        this.filmStorage = filmStorage;
        this.filmService = filmService;
    }

    @PostMapping("/films")
    public Film addFilm(@RequestBody Film film) throws ValidationException {
        return filmStorage.addFilm(film);
    }

    @PutMapping("/films")
    public Film updateFilm(@RequestBody Film film) throws ValidationException {
        return filmStorage.updateFilm(film);
    }

    @GetMapping("/films")
    public List<Film> getAllFilms() {
        return new ArrayList<>(filmStorage.getAllFilms().values());
    }

    @GetMapping("/films/{id}")
    public Film getFilm(@PathVariable Long id) {
        return filmStorage.getFilm(id);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public Film putLike(@PathVariable Long id, @PathVariable Long userId) {
        return filmService.putLike(id, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public Film deleteLike(@PathVariable Long id, @PathVariable Long userId) {
        return filmService.deleteLike(id, userId);
    }

    @GetMapping("/films/popular")
    public List<Film> getFilmsByCountLikes(@RequestParam(defaultValue = "10") Integer count){
        if (count <= 0) {
            throw new IncorrectCountException("Параметр count имеет отрицательное значение.");
        }
        return filmService.getFilmsByCountLikes(count);
    }
}