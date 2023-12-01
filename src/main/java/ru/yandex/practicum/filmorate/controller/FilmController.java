package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.validator.FilmValidator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {

    //хранение списка добавленных фильмов
      
    private final Map<Integer, Film> films = new HashMap<>();
    private int idFilm = 0;

    //получение списка всех фильмов
      
    @GetMapping
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    //добавление фильма в список
      
    @PostMapping()
    public Film addFilm(@RequestBody Film film) throws ValidationException {
        FilmValidator.isValidFilms(film);
        int id = generateIdFilms();
        film.setId(id);
        log.debug("Сохранили: {}", film);
        films.put(film.getId(), film);
        return film;
    }

    //обновление фильма в списке
      
    @PutMapping()
    public Film updateFilm(@RequestBody Film film) throws ValidationException {
        if (films.containsKey(film.getId())) {
            FilmValidator.isValidFilms(film);
            log.debug("Обновили: {}", film);
            films.put(film.getId(), film);
        } else {
            throw new ValidationException("Такого фильма нет в базе.");
        }
        return film;
    }

    //создание уникального id фильма
      
    private int generateIdFilms() {
        return ++idFilm;
    }
}