package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmDBService;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmDBService filmDBService;

    public FilmController(FilmDBService filmDBService) {
        this.filmDBService = filmDBService;
    }

    @PostMapping
    public Film createFilm(@RequestBody Film film) throws ValidateException {
        filmDBService.createFilm(film);
        log.info("добавлен фильм: {}", film.toString());
        return film;
    }

    @PutMapping
    public Optional<Film> updateFilm(@RequestBody Film film) throws ValidateException {
        log.info("обновлен фильм: {}", film.toString());
        return filmDBService.updateFilm(film);
    }

    @GetMapping
    public List<Film> getFilms() {
        log.info("запрошены все фильмы в количестве {}", filmDBService.getAll().size());
        return filmDBService.getAll();
    }

    public int getCountFilms() {
        return filmDBService.getAll().size();
    }

    @DeleteMapping
    public void deleteFilms() {
        filmDBService.deleteAll();
        log.info("удалены все фильмы");
    }

    @DeleteMapping("/{id}")
    public void deleteFilm(@PathVariable Long id) {
        filmDBService.deleteFilm(id);
        log.info("удален фильм {}", id);
    }

    @GetMapping("/{id}")
    public Optional<Film> getFilm(@PathVariable Long id) {
        log.info("запрошен фильм {}", id);
        return filmDBService.getById(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable Long id, @PathVariable Long userId) {
        filmDBService.addLike(id, userId);
        log.info("лайкнут фильм {}", id, " , пользователем {}", userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable Long id, @PathVariable Long userId) {
        filmDBService.removeLike(id, userId);
        log.info("отозван лайк фильма {}", id, " , пользователем {}", userId);
    }

    @GetMapping("/popular")
    public List<Optional<Film>> getPopularFilms(@RequestParam(defaultValue = "0") Integer count) {
        log.info("запрошены популярные фильмы в количестве {}", ((count == 0) || (count == null) ? 10 : count));
        return filmDBService.getMaxRating(count);
    }

}
