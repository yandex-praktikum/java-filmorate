package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final List<Film> films = new ArrayList<>();

    @GetMapping
    public List<Film> getFilms (){
        return films;
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film){
        log.info("Добавление фильма");
        films.add(film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film){
        log.info("Обновление фильма");
        films.add(film);
        return film;
    }

}
