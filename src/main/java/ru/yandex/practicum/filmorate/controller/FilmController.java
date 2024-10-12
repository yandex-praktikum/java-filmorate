package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
public class FilmController {

    private static final LocalDate BEGINNING_OF_THE_DATE = LocalDate.of(1895, 12, 28);

    private static Logger log = LoggerFactory.getLogger(FilmController.class);

    private Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public List<Film> allListFilm() {
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        log.info("Начало создание фильма");

        if (film.getName() == null || film.getName().isEmpty()) {
            log.debug("Наименование фильма {}", film.getName());
            throw new ValidationException("Имя не должно быть пустым");
        }
        if (film.getDescription().length() >= 200) {
            log.warn("Описание фильма {}", film.getDescription());
            throw new ValidationException("Максимальная длина описания — 200 символов");
        }
        if (film.getReleaseDate().isBefore(BEGINNING_OF_THE_DATE)) {
            log.warn("Дата релиза фильма {}", film.getReleaseDate());
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
        }
        if (film.getDuration() == null || film.getDuration().isNegative()) {
            log.warn("Продолжительность фильма {}", film.getDuration());
            throw new ValidationException("Продолжительность фильма должна быть положительным числом");
        }

        Film createFilm = Film.builder()
                .id(generationId())
                .name(film.getName())
                .description(film.getDescription())
                .releaseDate(film.getReleaseDate())
                .duration(film.getDuration())
                .build();

        log.debug("Фильм создан {}", createFilm);
        films.put(createFilm.getId(), film);
        log.debug("Фильм добавлен в хранилище {}", createFilm.getId());

        return createFilm;
    }

    @PutMapping
    public Film update(@RequestBody Film newFilm) {
        log.info("Начало обновление фильма");

        if (newFilm.getId() == null) {
            log.debug("Id не указан для обновления");
            throw new ValidationException("Id должен быть указан");
        }
        if (!films.containsKey(newFilm.getId())) {
            log.warn(" Фильм не найден с Id {}", newFilm.getId());
            throw new NotFoundException("Фильм не найден");
        }
        Film existingFilm = films.get(newFilm.getId());
        log.debug("Обновление фильма с Id: {}", existingFilm.getId());

        Film updateFilm = Film.builder()
                .id(existingFilm.getId())
                .name(newFilm.getName())
                .description(newFilm.getDescription())
                .releaseDate(newFilm.getReleaseDate())
                .duration(newFilm.getDuration())
                .build();
        films.put(updateFilm.getId(), updateFilm);
        log.debug("Фильм обновлён с Id {}", updateFilm.getId());
        return updateFilm;
    }

    private long generationId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}