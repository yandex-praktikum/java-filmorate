package ru.yandex.prakticum.filmorate.controllers.films.users.controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.prakticum.filmorate.controllers.films.users.model.Film;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class FilmController {
    private Map<Integer,Film> films;

    @GetMapping("/films/")
    private void addFilm(@RequestBody Film film){
        if (FilmCheck.filmCheck(film)){
            if (films.containsKey(film.getId())){
                log.trace("Добавлен фильм" + film);
                films.replace(film.getId(), film);
            }
            else {
                log.trace("Изменен фильм" + film);
                films.put(film.getId(), film);
            }
        }
    }

    @GetMapping("/films")
    private List getAllFilm(){
        log.trace("Отправлен список фильмов");
        return List.of(films.values());
    }
}
