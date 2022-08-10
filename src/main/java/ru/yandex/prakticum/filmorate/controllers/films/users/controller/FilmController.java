package ru.yandex.prakticum.filmorate.controllers.films.users.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.prakticum.filmorate.controllers.films.users.model.Film;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class FilmController {
    private Integer id = 0;
    private Map<Integer,Film> films = new HashMap<>();

    @PostMapping("/films")
    private Film addFilm(@RequestBody Film film){
        if (FilmCheck.filmCheck(film)){
            id++;
            film.setId(id);
            log.trace("Добавлен фильм" + film);
            films.put(film.getId(), film);
        }
        return film;
    }
    @PutMapping("/films")
    private Film updateFilm(@RequestBody Film film){
        films.replace(film.getId(),film);
        return film;
    }

    @GetMapping("/films")
    private List getAllFilm(){
        return List.of(films.values());
    }
}
