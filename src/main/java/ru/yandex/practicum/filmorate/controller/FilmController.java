package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final List<Film> films = new ArrayList<>();
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private final LocalDate MIN_DATE = LocalDate.of(1895,12,28);
    private int filmId = 1;

    private void increaseFilmId(){
        filmId++;
    }

    @GetMapping
    public List<Film> allFilms(){
        return films;
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) throws ValidationException{
        log.info("Получен POST-запрос с объектом Film: {}", film);
        film.setId(filmId);
        validate(film);
        films.add(film);
        log.info("Фильм {} c id={} добавлен, объект: {}", film.getName(), filmId, film);
        increaseFilmId();
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) throws ValidationException{
        log.info("Получен PUT-запрос с объектом Film: {}", film);
        validate(film);
        int filmId = film.getId();
        for (Film u : films) {
            if (u.getId() == filmId) {
                films.set(films.indexOf(u), film);
                log.info("Фильм c id={} обновлён", filmId);
                return film;
            }
        } 
        throw new ValidationException("Фильм с id=" + filmId + " не найден");
    }

    public void validate(Film film) throws ValidationException {
        if (StringUtils.hasLength(film.getName())){
            if (film.getDescription().length() < 201){
                if (film.getReleaseDate().isAfter(MIN_DATE)){
                    if (film.getDuration() <= 0) throw new ValidationException("Продолжительность фильма должна " +
                            "быть положительной");
                } else throw new ValidationException("Дата релиза должна быть больше 28.12.1895");
            } else throw new ValidationException("Описание не должно превышать 200 символов");
        } else throw new ValidationException("Название не может быть пустым или null");
    }
}
