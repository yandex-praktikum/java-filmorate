package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.List;

@Validated
@RestController
@Slf4j
public class FilmController {

    private final FilmService filmService;
    private final FilmStorage filmStorage;

    @Autowired
    public FilmController(FilmService filmService, FilmStorage filmStorage) {
        this.filmService = filmService;
        this.filmStorage = filmStorage;
    }

    @GetMapping("/films") // получение списка фильмов
    public List<Film> findAll() {
        return filmStorage.findAll();
    }

    @PostMapping(value = "/films") // добавление фильма
    public Film create(@RequestBody @Valid Film film) throws ValidationException {
        return filmStorage.create(film);
    }

    @PutMapping(value = "/films") // обновление фильма/films/{id}/like/{userId}
    public Film update(@RequestBody @Valid Film film) throws ValidationException {
        return filmStorage.update(film);
    }

    @GetMapping("/films/{id}") // получение пользователя по ID
    public Film findFilm(@PathVariable int id) {
        return filmService.findFilm(id);
    }

    @PutMapping(value = "/films/{id}/like/{userId}") // добавление лайка
    public void setLike(@PathVariable int id, @PathVariable int userId) {
        filmService.setLike(id, userId);
    }

    @DeleteMapping(value = "/films/{id}/like/{userId}") // удаление лайка
    public void deleteLike(@PathVariable int id, @PathVariable int userId) {
        filmService.deleteLike(id, userId);
    }

    @GetMapping("/films/popular")
    public List<Film> getPopularFilms(@RequestParam(value = "count", defaultValue = "10", required = false)
                                      String count) {
        return filmService.getPopularFilms(Integer.parseInt(count));
    }


}
