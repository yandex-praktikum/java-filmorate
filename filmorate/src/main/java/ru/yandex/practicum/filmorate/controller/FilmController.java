package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    private final HashMap<Integer, Film> films = new HashMap<>();
    private int id = 1;

    @GetMapping
    public List<Film> getFilm() {
        log.info("Получен GET-запрос к эндпоинту /films");
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film addFilm(@Validated @RequestBody Film film) {
        System.out.println("привет");
        if (film.getDescription().length() > 200) {
            throw new ValidationException("Описание фильма должно быть не более 200 символов");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза должна бить не раньше 28 декабря 1895 года");
        }
        if (film.getId() == 0) {
            assignId(film);
        } else {
            throw new ValidationException("Фильм должен быть передан без id");
        }
        films.put(film.getId(), film);
        log.info("Создан объект '{}'", film);
        return film;
    }



    @PutMapping
    public Film update(@Validated @RequestBody Film film) {
        if (film.getId() == 0) {
            throw new ValidationException("Введите id фильма, которого необходимо обновить");
        }
        films.put(film.getId(), film);
        log.info("Обновлен объект '{}'", film);
        return film;
    }

    private void assignId(Film film) {
        film.setId(id);
        id++;
    }
}
