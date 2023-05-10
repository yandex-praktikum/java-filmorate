package ru.yandex.practicum.filmorate.model.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.service.IdCounter;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/films")
public class FilmController {

    Set<Film> films = new HashSet<>();

    @GetMapping
    public Set<Film> getFilms() { // получение всех фильмов.
        return films;
    }

    @PostMapping
    public Film createFilm(@RequestBody Film film) { // добавление фильма
        film.setId(IdCounter.createFilmId());
        if (films.contains(film.getId())) {
            throw new ValidationException("Фильм " + film + ", уже есть в коллекции.");
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // https://ibb.co/JKHSry5   https://stackoverflow.com/questions/16232833/how-to-respond-with-an-http-400-error-in-a-spring-mvc-responsebody-method-retur
        }
        if (checkFilmCorrection(film)) {
            films.add(film);
        }
        return film;
    }

    @PutMapping
    public Film putFilm(@RequestBody Film film) {
        Film result = null;
        if (checkFilmCorrection(film)) {
            for (Film f : films) {
                if (f.getId() == film.getId()) {
                    f.setName(film.getName());
                    f.setDescription(film.getDescription());
                    f.setReleaseDate(film.getReleaseDate());
                    f.setDuration(film.getDuration());
                    f.setRate(film.getRate());
                    result = f;
                }
            }
        }
        return result;
    }

    private boolean checkFilmCorrection(Film film) {
        if (film.getName().isBlank()) {
            throw new ValidationException("У фильма должно быть название. Name: " + film.getName());

        } else if (film.getDescription().length() > 200) {
            throw new ValidationException("Максимальное количество букв в названии фильма не должно превышать 200. " +
                    "film.getDescription(): " + film.getDescription());

        } else if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Некорректная дата фильма. Дата: " + film.getReleaseDate());

        } else if (film.getDuration() < 1) {
            throw new ValidationException("Продолжительность фильма должна быть положительной. Продолжительность: " + film.getDuration());

        } else if (film.getId() > films.size() + 1) {
            throw new ValidationException("Некорректный id фильма. Id: " + film.getId());
        }
        return true;
    }

}
