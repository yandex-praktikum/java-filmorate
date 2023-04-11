package ru.yandex.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.model.Film;
import ru.yandex.practicum.service.FilmService;
import ru.yandex.practicum.storage.film.FilmStorage;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * Контроллер фильмов
 */
@RestController
@RequiredArgsConstructor
public class FilmController {
    private final FilmStorage storage;
    private final FilmService service;

    /**
     * Получение всех фильмов
     */
    @GetMapping("/films")
    public List<Film> findAll() {
        return storage.findAll();
    }

    /**
     * Добавление фильма
     */
    @PostMapping(value = "/films")
    public Film add(@Valid @RequestBody Film film) {
        return storage.add(film);
    }

    /**
     * Обновление фильма
     */
    @PutMapping(value = "/films")
    public Film update(@Valid @RequestBody Film film) {
        return storage.update(film);
    }

    /**
     * Получение фильма по id
     */
    @GetMapping("/films/{id}")
    public Film findFilm(@PathVariable("id") Integer id) {
        return storage.findFilmById(id);
    }

    /**
     * Установка лайка фильму
     */
    @PutMapping("/films/{id}/like/{userId}")
    public Film putLike(@PathVariable Integer id, @PathVariable Integer userId) {
        return service.putLike(storage.findFilmById(id), userId);
    }

    /**
     * Удаление лайка у фильма
     */
    @DeleteMapping("/films/{id}/like/{userId}")
    public Film deleteLike(@PathVariable("id") Integer id, @PathVariable("userId") Integer userId) {
        return service.deleteLike(id, userId);
    }

    /**
     * Получение списка популярных фильмов
     */
    @GetMapping("/films/popular")
    public List<Film> getPopularFilms(@RequestParam(required = false) Optional<Integer> count) {
        return service.findBest(count.orElse(10));
    }
}
