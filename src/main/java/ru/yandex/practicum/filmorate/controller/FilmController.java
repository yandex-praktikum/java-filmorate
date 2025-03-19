package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final Map<Long, Film> films = new HashMap<>();

    @GetMapping
    public Collection<Film> getAll() {
        log.info("Получение всех фильмов");
        return  films.values();
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        try {
            checkFilm(film);
            film.setId(getNextId());
            films.put(film.getId(), film);
            log.info("Создан новый фильм с id: {}", film.getId());
            return film;
        } catch (ValidationException e) {
            log.error("Невозможно создать фильм: {}", e.getMessage());
            throw e;
        }
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        Film existingFilm;
        if (film.getId() == null) {
            log.error("Не указан id");
            throw new ValidationException("Должен быть указан id");
        }
        if(films.containsKey(film.getId())) {
            try {
                checkFilm(film);
                existingFilm = films.get(film.getId());
                existingFilm.setDescription(film.getDescription());
                existingFilm.setDuration(film.getDuration());
                existingFilm.setName(film.getName());
                existingFilm.setReleaseDate(film.getReleaseDate());
                log.info("Обновлен фильм с id: {}", film.getId());
            } catch (ValidationException e) {
                log.error("Невозможно обновить фильм: {}", e.getMessage());
                throw e;
            }
        } else {
            log.error("Существует фильм со следующим id: {}", film.getId());
            throw new ValidationException("Фильм с id = " + film.getId() + " не найден");
        }
        return existingFilm;
    }

    private void checkFilm(Film film) {
        if(film.getName().isBlank() || film.getDescription().length() > 200 ||
                film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28)) ||
                film.getDuration() <= 0) {
            throw new ValidationException("Некорректные данные фильма");
        }
    }

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
