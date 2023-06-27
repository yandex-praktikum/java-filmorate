package ru.yandex.practicum.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.exeptions.FilmAlreadyExistException;
import ru.yandex.practicum.exeptions.InvalidFilmeException;
import ru.yandex.practicum.model.Film;

import java.time.LocalDate;
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
    public Film create(@RequestBody Film film) throws InvalidFilmeException {
        if (films.contains(film)) {
            throw new InvalidFilmeException("Фильм уже есть в коллекции!");
        }
        else if (film.getName().isEmpty()) {
            throw new InvalidFilmeException("Название фильма не может быть пустым!");
        }
        else if (film.getDescription().length() > 200) {
            throw new InvalidFilmeException("Длинна описания превышает 200 символов!");
        }
        else if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new InvalidFilmeException("Дата релиза не должна быть раньше 28 декабря 1895 года!");
        }
        else if (film.getDuration().getSeconds() < 0) {
            throw new InvalidFilmeException("Продолжительность фильма должна быть положительной!");
        }
        else {
            films.add(film);
            return film;
        }
    }

    @PutMapping("/film")
    public Film update(@RequestBody Film film) throws InvalidFilmeException {
        if (film.getId() == null || film.getId() < 1) {
            throw new InvalidFilmeException();
        }
        films.add(film);
        return film;
    }
}