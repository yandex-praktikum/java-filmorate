package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.HashMap;

@RestController
@RequestMapping("/films")
public class FilmController {
    HashMap<Integer, Film> filmHashMap = new HashMap<>();

    @GetMapping
    public HashMap<Integer, Film> getAllFIlms() {

        return filmHashMap;
    }

    @PostMapping
    public ResponseEntity<String> saveFilm(@Valid @RequestBody Film film) {
        filmHashMap.put(film.getId(), film);
        return ResponseEntity.ok("valid");
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        Film oldFilm = filmHashMap.get(film.getId());
        if (oldFilm ==null) {
            return  null;
        } else {
            filmHashMap.remove(oldFilm);
            filmHashMap.put(film.getId(), film);
            return film;
        }
    }
}
