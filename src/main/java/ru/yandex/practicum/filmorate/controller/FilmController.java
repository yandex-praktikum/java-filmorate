package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmIdGenerator;
import ru.yandex.practicum.filmorate.model.ValidationException;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Component
@RestController
@RequestMapping("/films")
public class FilmController {

    private final LocalDate EARLIEST_RELEASE_DATE = LocalDate.of(1895, 12, 28);
    private final Map<Integer, Film> films = new HashMap<>();
    private final FilmIdGenerator filmIdGenerator;

    @Autowired
    public FilmController(FilmIdGenerator filmIdGenerator) {
        this.filmIdGenerator = filmIdGenerator;
    }

    public Film getFilm(int id) {
        return films.get(id);
    }

    @GetMapping
    public Collection<Film> findAll() {
        log.info("Текущее количество фильмов: {}", films.size());
        return films.values();
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        int newId = filmIdGenerator.generate();
        film.setId(newId);
        validation(film);
        log.info("Сохраняем новый фильм: {}", film);
        films.put(newId, film);
        return film;
    }

    @PutMapping
    public Film saveFilm(@RequestBody Film film) {
        if (film.getId() < 0) {
            log.error("Ошибка, валидация не пройдена. Id не может быть отрицательным: {}", film.getId());
            throw new ValidationException();
        }
        for (Film oldFilm : films.values()) {
            if (oldFilm.getId() == film.getId()) {
                oldFilm.setName(film.getName());
                oldFilm.setDescription(film.getDescription());
                oldFilm.setReleaseDate(film.getReleaseDate());
                oldFilm.setDuration(film.getDuration());
                log.info("Обновляем фильм: {}", oldFilm);
                return oldFilm;
            }
        }
        log.info("Добовляем новый фильм: {}", film);
        return film;
    }

    private void validation(Film film) {
        int len = film.getDescription().length();

        if (film.getName().isEmpty()) {
            log.error("Ошибка, валидация не пройдена. Название фильма не может быть пустым: {}", film.getName());
            throw new ValidationException();
        }
        if (len > 200) {
            log.error("Ошибка, валидация не пройдена. Максимальная длина описания должна быть не больше 200 символов: {}", film.getDescription());
            throw new ValidationException();
        }
        if (film.getReleaseDate().isBefore(EARLIEST_RELEASE_DATE)) {
            log.error("Ошибка, валидация не пройдена. Дата релиза должна быть не раньше 28 декабря 1895 года: {}", film.getReleaseDate());
            throw new ValidationException();
        }
        if (film.getDuration() < 0) {
            log.error("Ошибка, валидация не пройдена. Продолжительность фильма должна быть положительной: {}", film.getDuration());
            throw new ValidationException();
        }
    }
}
