package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.ValidationException;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private static final LocalDate FILM_EPOCH = LocalDate.of(1895, 12, 28);
    private final Map<Integer, Film> films;
    private int id;

    public FilmController() {
        films = new HashMap<>();
        id = 0;
    }
    private int setId() {
        return ++id;
    }

    @GetMapping
    public Collection<Film> getFilms() {
        log.info("Запрошен текущий список фильмов: " + films);
        return films.values();
    }
    @PostMapping
    public Film createFilm(@RequestBody Film film) throws ValidationException {
        validateFilm(film);
        int filmId = setId();
        film.setId(filmId);
        films.put(filmId, film);
        log.info("Создан фильм " + film);
        return film;
    }
    @PutMapping
    public Film updateFilm(@RequestBody Film film) throws ValidationException {
        checkFilmInBase(film);
        validateFilm(film);
        int filmId = film.getId();
        Film oldFilm = films.get(filmId);
        films.put(filmId, film);
        log.info("Фильм " + oldFilm + " изменен на " + film);
        return film;
    }

    private void checkFilmInBase(Film film) throws ValidationException {
        if (!films.containsKey(film.getId())) {
            throw new ValidationException("Фильм с id " + film.getId() + "не найден.");
        }
    }
    private void findFilm(Film film) throws ValidationException {
        for (Film f: films.values()) {
            if (f.equals(film)) {
                throw new ValidationException("Такой фильм уже имеется с id " + f.getId());
            }
        }
    }
    private void validateFilm(Film film) throws ValidationException {
        if (film.getReleaseDate() == null) {
            throw new ValidationException("Не задана дата фильма.");
        } else if (film.getReleaseDate().isBefore(FILM_EPOCH)) {
            throw new ValidationException("Слишком старая дата фильма.");
        } else if (film.getName().isBlank()) {
            throw new ValidationException("Наименование фильма не может быть пустым.");
        } else if (film.getDuration() < 1) {
            throw new ValidationException("Продолжительность фильма должна быть положительной.");
        } else if (film.getDescription().length() > 200) {
            throw new ValidationException("Максимальная длина описания — 200 символов.");
        }
        findFilm(film);
    }

}
