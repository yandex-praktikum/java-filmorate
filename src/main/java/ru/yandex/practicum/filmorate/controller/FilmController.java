package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private int idGenerator = 1;

    @PostMapping
    public Film createFilm(@RequestBody Film film) {
        log.info("Пришёл запрос на добавление фильма '{}'", film.getName());
        validate(film);
        film.setId(idGenerator++);
        films.put(film.getId(), film);
        log.info("Фильм '{}' успешно добавлен.", film.getName());
        return film;
    }

    @GetMapping
    public List<Film> getAll() {
        return new ArrayList<>(films.values());
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film newFilm) {
        log.info("Пришёл запрос на обновление данных фильма '{}'", newFilm.getName());
        validate(newFilm);
        if (newFilm.getId() == null) {
            throw new RuntimeException("ID должен быть указан.");
        }
        log.info("Ищем фильм с ID '{}'", newFilm.getId());
        if (films.containsKey(newFilm.getId())) {
            Film oldFilm = films.get(newFilm.getId());
            log.info("Найден фильм с ID '{}'", newFilm.getId());
            oldFilm.setName(newFilm.getName());
            oldFilm.setDescription(newFilm.getDescription());
            oldFilm.setReleaseDate(newFilm.getReleaseDate());
            oldFilm.setDuration(newFilm.getDuration());
            log.info("Обновлены данные фильма с ID '{}'", newFilm.getId());
            return oldFilm;
        }
        throw new RuntimeException("Фильм с ID " + newFilm.getId() + " не найден.");
    }

    private void validate(Film film) {
        LocalDate minDate = LocalDate.of(1895, 12, 28);
        if (film.getName() == null || film.getName().isEmpty()) {
            throw new RuntimeException("Должно быть задано название фильма.");
        }
        if (film.getDescription().length() > 200) {
            throw new RuntimeException("Описание фильма не может быть длиннее 200 символов.");
        }
        if (film.getReleaseDate().isBefore(minDate)) {
            throw new RuntimeException("Фильм не может быть старше 28 декабря 1895 года.");
        }
        if (film.getDuration() < 0) {
            throw new RuntimeException("Длительность фильма может быть только положительной.");
        }
    }
}