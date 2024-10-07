package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private int currentId = 1;
    private final List<Film> films = new ArrayList<>();

    @PostMapping
    public ResponseEntity<Film> addFilm(@RequestBody Film film) {
        try {
            validateFilm(film);
            film.setId(currentId++);
            films.add(film);
            log.info("Фильм добавлен: {}", film);
            return new ResponseEntity<>(film, HttpStatus.CREATED);
        } catch (ValidationException e) {
            log.error("Ошибка валидации при добавлении фильма: {}", e.getMessage());
            throw e;
        }
    }

    @PutMapping
    public ResponseEntity<Object> updateFilm(@RequestBody Film updatedFilm) {
        try {
            validateFilm(updatedFilm);
            for (Film film : films) {
                if (film.getId() == updatedFilm.getId()) {
                    film.setName(updatedFilm.getName());
                    film.setDescription(updatedFilm.getDescription());
                    film.setReleaseDate(updatedFilm.getReleaseDate());
                    film.setDuration(updatedFilm.getDuration());
                    log.info("Фильм обновлён: {}", film);
                    return new ResponseEntity<>(film, HttpStatus.OK);
                }
            }
            log.warn("Фильм с ID {} не найден для обновления.", updatedFilm.getId());
            return new ResponseEntity<>(new ErrorResponse("Фильм не найден"), HttpStatus.NOT_FOUND);
        } catch (ValidationException e) {
            log.error("Ошибка валидации при обновлении фильма: {}", e.getMessage());
            return new ResponseEntity<>(new ErrorResponse("Ошибка валидации: " + e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<List<Film>> getAllFilms() {
        return new ResponseEntity<>(films, HttpStatus.OK);
    }

    private void validateFilm(Film film) {
        if (film.getName() == null || film.getName().isEmpty()) {
            throw new ValidationException("Название фильма не может быть пустым.");
        }
        if (film.getDescription().length() > 200) {
            throw new ValidationException("Максимальная длина описания — 200 символов.");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года.");
        }
        if (film.getDuration() <= 0) {
            throw new ValidationException("Продолжительность фильма должна быть положительным числом.");
        }
    }
}
