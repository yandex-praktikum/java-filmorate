package ru.yandex.practicum.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.exeptions.FilmAlreadyExistException;
import ru.yandex.practicum.exeptions.InvalidIdException;
import ru.yandex.practicum.model.Film;

import java.util.ArrayList;
import java.util.List;

@RestController
public class FilmController {

    private List<Film> films = new ArrayList<>();

    @GetMapping("/films")
    public List<Film> findAll() {
        return films;
    }

    @PostMapping("/film")
    public Film create(@RequestBody Film film) throws FilmAlreadyExistException {
        if (films.contains(film)) {
            throw new FilmAlreadyExistException();
        }
        else {
            films.add(film);
            return film;
        }
    }

    @PutMapping("/film")
    public Film update(@RequestBody Film film) throws InvalidIdException {
        if (film.getId() == null || film.getId() < 1) {
            throw new InvalidIdException();
        }
        films.add(film);
        return film;
    }
}