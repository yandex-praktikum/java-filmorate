package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private int id;

    public FilmController() {
        id = 0;
    }

    @GetMapping
    public List<Film> getFilms() {
        log.info("Отправлен перечень фильмов {}", films);
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        if (films.containsKey(film.getId())) {
            throw new ValidationException("Фильм " + film.getId() + " уже существует");
        }

        updateID();
        film.setId(id);
        films.put(film.getId(), film);
        log.info("Добавлен фильм {}", film);

        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        if (!films.containsKey(film.getId())) {
            throw new ValidationException("Невозможно обновить. Пользователь " + film + " не существует");
        }

        films.put(film.getId(), film);
        log.info("Обновлен фильм {}", film);

        return film;
    }

    private void updateID() {
        id++;
    }
}
