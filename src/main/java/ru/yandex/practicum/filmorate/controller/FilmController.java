package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.FilmValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final HashMap<Integer, Film> films = new HashMap<>();
    private static int filmId = 0;

    @GetMapping()
    public Collection<Film> getAllFilms() {
        log.info("Получен список всех фильмов.");
        return films.values();
    }

    @PostMapping()
    public Film createNewFilm(@Valid @RequestBody Film film) {
        if (films.containsValue(film)) {
            throw new FilmValidationException("Фильм " + film.getName() + " уже добавлен.");
        }

        validateFilmData(film);
        film.setId(++filmId);
        films.put(film.getId(), film);
        log.info("Добавлен новый фильм: " + film.getName());

        return film;
    }

    @PutMapping()
    public Film updateFilm(@Valid @RequestBody Film film) {
        if (films.containsKey(film.getId())) {
            validateFilmData(film);
            films.put(film.getId(), film);
            log.info("Обновлена информация по фильму: " + film.getName());
        } else {
            throw new FilmValidationException("Фильм не найден.");
        }

        return film;
    }

    public void validateFilmData(Film film) throws FilmValidationException {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new FilmValidationException("Дата релиза должна быть не раньше 28 декабря 1895 года.");
        }
    }
}