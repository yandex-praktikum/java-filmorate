package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
// Класс FilmController
public class FilmController {

    // Минимально допустимая дата релиза фильма
    private static final LocalDate MIN_RELEASE_DATE = LocalDate.of(1895, 12, 28);
    // Максимальная длина описания фильма
    private static final int MAX_DESCRIPTION_LENGTH = 200;
    // Хранилище фильмов в памяти приложения (ключ - id фильма, значение - объект Film)
    private final Map<Integer, Film> films = new HashMap<>();
    // Счётчик для генерации id фильмов
    private int currentId = 0;

    // Метод обрабатывает POST-запросы на /films
    @PostMapping
    public Film create(@RequestBody Film film) { // Метод создания фильма
        validate(film);
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Добавлен фильм: {}", film);
        return film;
    }

    // Метод обрабатывает PUT-запросы на /films
    @PutMapping
    public Film update(@RequestBody Film film) { // Метод обновления фильма
        validate(film);
        if (!films.containsKey(film.getId())) {
            throw new ValidationException("Фильм с id=" + film.getId() + " не найден");
        }
        films.put(film.getId(), film);
        log.info("Обновлён фильм: {}", film);
        return film;
    }

    // Метод обрабатывает GET-запросы на /films
    @GetMapping
    public Collection<Film> findAll() { // Метод получения всех фильмов
        return new ArrayList<>(films.values());
    }

    private int getNextId() { // Метод генерации нового id
        return ++currentId;
    }

    private void validate(Film film) { // Метод проверки корректности данных фильма
        if (film == null) {
            throw new ValidationException("Фильм не передан");
        }
        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("Название фильма не может быть пустым");
        }
        if (film.getDescription() != null && film.getDescription().length() > MAX_DESCRIPTION_LENGTH) {
            throw new ValidationException("Описание фильма слишком длинное");
        }
        if (film.getReleaseDate() == null || film.getReleaseDate().isBefore(MIN_RELEASE_DATE)) {
            throw new ValidationException("Дата релиза фильма раньше 28 декабря 1895 года");
        }
        if (film.getDuration() <= 0) {
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }
    }
}
