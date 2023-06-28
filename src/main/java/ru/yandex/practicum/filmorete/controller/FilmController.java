package ru.yandex.practicum.filmorete.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorete.exeptions.ValidationFilmException;
import ru.yandex.practicum.filmorete.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
public class FilmController {

    private Map<Integer, Film> films = new HashMap();
    private Integer lastIdentification = 1;

    @GetMapping("/films")
    public Collection<Film> findAll() {
        return films.values();
    }

    @PostMapping("/film")
    public Film create(@RequestBody Film film) throws ValidationFilmException {
        if (films.containsKey(film.getId())) {
            throw new ValidationFilmException("Фильм уже есть в коллекции!");
        }
        else {
            validatorFilms(film);
            film.setId(getLastIdentification());
            films.put(film.getId(), film);
            return film;
        }
    }

    @PutMapping("/film")
    public Film update(@RequestBody Film film) throws ValidationFilmException {
        if (film.getId() == null || film.getId() < 1) {
            throw new ValidationFilmException("Не указан ID фильма!");
        }
        else {
            validatorFilms(film);
            films.put(film.getId(), film);
            return film;
        }
    }

    private void validatorFilms(Film film) throws ValidationFilmException {
        if (film.getName().isEmpty()) {
            throw new ValidationFilmException("Название фильма не может быть пустым!");
        }
        if (film.getDescription().length() > 200) {
            throw new ValidationFilmException("Длинна описания превышает 200 символов!");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationFilmException("Дата релиза не должна быть раньше 28 декабря 1895 года!");
        }
        if (film.getDuration().getSeconds() < 0) {
            throw new ValidationFilmException("Продолжительность фильма должна быть положительной!");
        }
    }

    private Integer getLastIdentification() {
        return lastIdentification++;
    }
}