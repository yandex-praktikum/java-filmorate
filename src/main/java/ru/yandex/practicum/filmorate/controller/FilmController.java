package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.InMemoryFilmService;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final InMemoryFilmService filmService;

    public FilmController(InMemoryFilmService filmDBService) {
        this.filmService = filmDBService;
    }

    @PostMapping
    public Film createFilm(@RequestBody Film film) throws ValidateException {
        filmService.createFilm(film);
        log.info("добавлен фильм: {}", film.toString());
        return film;
    }

    @PutMapping
    public Optional<Film> updateFilm(@RequestBody Film film) throws ValidateException {
        log.info("обновлен фильм: {}", film.toString());
        return filmService.updateFilm(film);
    }

    @GetMapping
    public List<Film> getFilms() {
        log.info("запрошены все фильмы в количестве {}", filmService.getAll().size());
        return filmService.getAll();
    }

    public int getCountFilms() {
        return filmService.getAll().size();
    }

    @DeleteMapping
    public void deleteFilms() {
        filmService.deleteAll();
        log.info("удалены все фильмы");
    }

    @DeleteMapping("/{id}")
    public void deleteFilm(@PathVariable Long id) {
        filmService.deleteFilm(id);
        log.info("удален фильм {}", id);
    }

    @GetMapping("/{id}")
    public Optional<Film> getFilm(@PathVariable Long id) {
        log.info("запрошен фильм {}", id);
        return filmService.getById(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable Long id, @PathVariable Long userId) {
        filmService.addLike(id, userId);
        log.info("лайкнут фильм {}", id, " , пользователем {}", userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable Long id, @PathVariable Long userId) {
        filmService.removeLike(id, userId);
        log.info("отозван лайк фильма {}", id, " , пользователем {}", userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "0") Integer count) {
        log.info("запрошены популярные фильмы в количестве 10");
        return filmService.getMaxRating(count);
    }

}
