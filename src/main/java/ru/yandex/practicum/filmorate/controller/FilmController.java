package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ApiError;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private static int currentId = 0;
    private static final LocalDate FIRST_TIME = LocalDate.of(1895, 12, 28);
    private static final int MAX_DESCRIPTION = 200;

    @GetMapping
    public List<Film> findAll() {
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film create(@RequestBody Film film, HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());
        if (films.containsKey(film.getId())) {
            log.info("Фильм  " + film.getId() + " уже есть в базе.");
            throw new ValidationException("Фильм  " + film.getId() + " уже есть в базе");
        }
        validate(film);
        int id = ++currentId;
        film.setId(id);
        films.put(id, film);
        log.info("Фильм  " + film.getId() + " добавлен в базу");
        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film film, HttpServletRequest request) {
        log.info("Получен запрос к эндпоинту: '{} {}', Строка параметров запроса: '{}'",
                request.getMethod(), request.getRequestURI(), request.getQueryString());
        if (!films.containsKey(film.getId())) {
            log.error("Нет такого фильма");
            throw ValidationException.createNotFoundException("Нет такого фильма");
        }
        validate(film);
        films.put(film.getId(), film);
        log.info("Фильм  " + film.getId() + " обновлён в базе");
        return film;
    }

    private void validate(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            log.info("У фильма нет названия");
            throw new ValidationException("У фильма нет названия");
        }
        if (film.getDescription() == null) {
            log.info("Опишите фильм");
            throw new ValidationException("Опишите фильм");
        }
        if (film.getDescription().length() > MAX_DESCRIPTION) {
            log.info("Максимальная длина описания - 200 символов");
            throw new ValidationException("Максимальная длина описания - 200 символов");
        }
        if (film.getReleaseDate() == null) {
            log.info("Дата выхода фильма отсутствует");
            throw new ValidationException("Дата выхода фильма отсутствует");
        }
        if (film.getReleaseDate().isBefore(FIRST_TIME)) {
            log.info("Исправьте дату на более позднюю. До 28 декабря 1895 года не выпускали фильмы");
            throw new ValidationException("Исправьте дату на более позднюю. До 28 декабря 1895 года не выпускали фильмы");
        }
        if (film.getDuration() <= 0) {
            log.info("Продолжительность фильма должна быть больше нуля");
            throw new ValidationException("Продолжительность фильма должна быть больше нуля");
        }
    }

    @ExceptionHandler(value = ValidationException.class)
    public ResponseEntity<ApiError> handleValidationException(ValidationException validationException) {
        return new ResponseEntity<>(new ApiError(validationException.getMessage()), validationException.getHttpStatus());
    }
}