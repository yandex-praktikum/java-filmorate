package ru.yandex.practicum.controllers;

import lombok.Data;
import ru.yandex.practicum.exceptions.ValidationException;
import ru.yandex.practicum.model.Film;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@RestController
public class FilmController {
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private final List<Film> films = new ArrayList<>();
    private static int id = 1;

    @GetMapping("/films")
    public List<Film> findAll() {
        return films;
    }

    @PostMapping(value = "/films")
    public void create(@Valid @RequestBody Film film) {

        if (film.getName().isBlank() || film.getName() == null) {
            log.info("Название фильма не может быть пустым");
            throw new ValidationException("Название фильма не может быть пустым");
        }

        if (film.getDescription().length() > 200) {
            log.info("Максимальная длина описания - 200 символов.");
            throw new ValidationException("Максимальная длина описания - 200 символов.");
        }

        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.info("Дата релиза не может быть раньше 28 декабря 1895 года.");
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года.");
        }

        if (film.getDuration().toMinutes() < 0) {
            log.info("Продолжительность фильма должна быть положительной.");
            throw new ValidationException("Продолжительность фильма должна быть положительной.");
        }

        // преобразую переданное значение в минуты
        film.setDuration(Duration.ofMinutes(film.getDuration().toSeconds()));
        film.setId(id);
        id++;
        films.add(film);
        log.info("Добавлен фильм {}", film.toString());
    }

    @PutMapping(value = "/films")
    public void edit(@RequestBody Film film) {
        if (films.size() > 0) {
            Film delFilm = films.stream().filter(s -> s.getId() == film.getId()).collect(Collectors.toList()).get(0);
            films.remove(delFilm);
        }
        films.add(film);
        log.info("Обновлен фильм {}", film.toString());
    }
}
