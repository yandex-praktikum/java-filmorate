package ru.yandex.practicum.filmorate.controllers;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
@Getter
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();

    FilmValidator filmValidator;

//    public FilmController() {
//        this.filmValidator = new FilmValidator();
//
//    }

    @GetMapping
    public Collection<Film> findAll() {
        log.info("findAll films");
        return films.values();
    }


    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        filmValidator.validate(film);
        films.put(film.getId(), film);
        log.info("Добавляемый film: {}", film);
        return film;
    }

    @PutMapping
    public Film put(@Valid @RequestBody Film film) {
        filmValidator.validate(film);
        films.put(film.getId(), film);
        log.info("Изменяемый film: {}", film);
        return film;
    }


}
