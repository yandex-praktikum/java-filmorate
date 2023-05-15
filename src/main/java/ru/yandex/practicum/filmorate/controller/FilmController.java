package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.controller.exception.FilmControllerException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.FilmValidator;
import ru.yandex.practicum.filmorate.validator.exception.ValidatorException;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.yandex.practicum.filmorate.controller.exception.FilmControllerException.FILM_ALREADY_EXISTS;
import static ru.yandex.practicum.filmorate.controller.exception.FilmControllerException.FILM_NOT_FOUND;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private Map<Long, Film> films = new HashMap<>();
    private long uniqueID = 1;

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        try {
            FilmValidator.validate(film);

            if (films.containsKey(film.getId())) {
                throw new FilmControllerException(
                        String.format(FILM_ALREADY_EXISTS, film));
            }

            film.setId(uniqueID++);
            films.put(film.getId(), film);
            log.trace("Успешно добавлен фильм: {}.", film);
        } catch (ValidatorException e) {
            log.warn("Фильм не добавлен: {}.", e.getMessage());
            throw new RuntimeException("Ошибка валидации: " + e.getMessage(), e);
        } catch (FilmControllerException e) {
            log.warn("Фильм не добавлен: {}.", e.getMessage());
            throw new RuntimeException("Ошибка контроллера:" + e.getMessage(), e);
        } finally {
            log.debug("Количество фильмов: {}.", films.size());
        }
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        try {
            FilmValidator.validate(film);

            if (!films.containsKey(film.getId())) {
                throw new FilmControllerException(
                        String.format(FILM_NOT_FOUND, film));
            }

            films.put(film.getId(), film);
            log.trace("Фильм успешно обновлён: {}.", film);
        } catch (ValidatorException e) {
            log.warn("Не удалось обновить фильм: {}.", e.getMessage());
            throw new RuntimeException("Ошибка валидации: " + e.getMessage(), e);
        } catch (FilmControllerException e) {
            log.warn("Не удалось обновить фильм: {}.", e.getMessage());
            throw new RuntimeException("Ошибка контроллера:" + e.getMessage(), e);
        } finally {
            log.debug("Количество фильмов: {}.", films.size());
        }

        return film;
    }

    @GetMapping
    public List<Film> findAll() {
        log.trace("Возвращены все фильмы.");
        return new ArrayList<>(films.values());
    }
}