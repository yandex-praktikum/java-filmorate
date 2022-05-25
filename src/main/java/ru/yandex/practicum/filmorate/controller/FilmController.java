package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.Film;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


@RestController
@Validated
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final Map<Integer,Film> films = new HashMap<>();

    @PostMapping
    public Film createFilm(@Valid @RequestBody(required = false) Film film) {
        if (films.containsKey(film.getId())) {
            log.info("Ошибка добавления данных: " + film.getName() + " Данные не добавлены.");
            return film;
        }
        films.put(film.getId(), film);
        return film;
    }

    @PutMapping
   public Film updateFilm(@Valid @RequestBody(required = false) Film film) {
        if (!films.containsKey(film.getId())) {
            log.info("Ошибка обновления данных: " + film.getName() + " Данные не обновлены.");
            return film;
        }
        films.put(film.getId(), film);
        return film;
    }

    @GetMapping
    public ArrayList<Film> findAllFilms() {
        return new ArrayList<>(films.values());
    }
}
