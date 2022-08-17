package ru.yandex.practicum.filmorate.controllers;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.model.Film;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private List<Film> films = new ArrayList<>();

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        films.add(film);
        log.info("add film");
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        films.add(film);
        log.info("update film");
        return film;
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return films;
    }
}
