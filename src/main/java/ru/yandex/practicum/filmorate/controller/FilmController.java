package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import javax.servlet.http.HttpServletRequest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private int id;
    private final Map<Integer, Film> films = new HashMap<>();

    @GetMapping
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public ResponseEntity<?> createFilm(@Valid @RequestBody Film film, HttpServletRequest request) {
        logRequest(request);
        if (isFilmNotValid(film)) {
            log.debug("В запросе переданы некорректные данные для добавления фильма.");
            throw new ValidationException("Некорректные данные для добавления фильма.");
        } else {
            newId();
            film.setId(id);
            films.put(film.getId(), film);
            log.debug("Добавлен новый фильм: " + film);
            return ResponseEntity.ok(film);
        }
    }

    @PutMapping
    public ResponseEntity<?> updateFilm(@Valid @RequestBody Film film, HttpServletRequest request) {
        logRequest(request);
        if (isFilmNotValid(film)) {
            log.debug("В запросе переданы некорректные данные для обновления фильма.");
            throw new ValidationException("Некорректные данные для обновления фильма.");
        } else if (!films.containsKey(film.getId())) {
            log.debug("В запросе передан некорректный id для обновления фильма.");
            throw new ValidationException("Некорректный id для обновления фильма.");
        } else {
            films.put(film.getId(), film);
            log.debug("Фильм в коллекции обновлен: " + film);
            return ResponseEntity.ok(film);
        }
    }

    private boolean isFilmNotValid(Film film) {
        return (film.getName().isBlank()
                || (film.getDescription().length() > 200)
                || !film.getReleaseDate().isAfter(LocalDate.of(1895, 12, 27))
                || (film.getDuration() <= 0));
    }

    private void newId() {
        ++id;
    }

    private void logRequest(HttpServletRequest request) {
        log.debug("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());
    }
}
