package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.FilmDoesNotExistException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
public class FilmController {

    private Integer currentId = 0;
    private final Map<Integer, Film> films = new HashMap<>();

    @GetMapping
    public List<Film> findAll() {
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film create(@RequestBody Film film){
        film.setId(currentId);
        films.put(currentId, film);
        currentId++;
        return film;
    }

    @PutMapping("/{id}")
    public Film update(@RequestParam Integer id, @RequestBody Film film) {
        checkFilmExist(id);
        film.setId(id);
        films.put(id, film);

        return film;
    }

    private void checkFilmExist(Integer id) {
        if (!films.containsKey(id)) {
            throw new FilmDoesNotExistException(String.format("Film with id %d does not exist!", id));
        }
    }
}
